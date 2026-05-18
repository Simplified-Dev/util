package dev.simplified.stream;

import dev.simplified.stream.exception.CompressionException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GzipCompressionTest {

    // -----------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------

    /**
     * Reference inflate via a growable {@link ByteArrayOutputStream}. Mirrors the legacy
     * {@code Compression.decompress} GZIP path; used by {@code LegacyEquivalence} tests as the
     * byte-for-byte comparison target without coupling to {@code Compression}'s internals.
     */
    private static byte[] referenceInflate(byte[] gzipped) throws IOException {
        try (
            ByteArrayInputStream in = new ByteArrayInputStream(gzipped);
            GZIPInputStream gz = new GZIPInputStream(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {
            byte[] buf = new byte[8192];
            int n;
            while ((n = gz.read(buf)) > 0)
                out.write(buf, 0, n);
            return out.toByteArray();
        }
    }

    /**
     * Reference deflate via the stock {@link GZIPOutputStream}. Bytes-comparison is intentionally
     * skipped (gzip output varies by JDK version / Deflater level / mtime) - tests should
     * round-trip instead.
     */
    private static byte[] referenceDeflate(byte[] data) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (GZIPOutputStream gz = new GZIPOutputStream(out)) {
            gz.write(data);
        }
        return out.toByteArray();
    }

    // -----------------------------------------------------------------
    // Round-trip parity
    // -----------------------------------------------------------------

    @Nested
    class RoundTrip {

        @Test
        void emptyArrayRoundTrips() {
            byte[] empty = new byte[0];
            byte[] gzipped = GzipCompression.compress(empty);
            assertNotNull(gzipped);
            byte[] back = GzipCompression.decompress(gzipped);
            assertArrayEquals(empty, back);
        }

        @Test
        void singleByteRoundTrips() {
            byte[] one = { (byte) 0x42 };
            byte[] back = GzipCompression.decompress(GzipCompression.compress(one));
            assertArrayEquals(one, back);
        }

        @Test
        void asciiTextRoundTrips() {
            byte[] data = "the quick brown fox jumps over the lazy dog 0123456789".repeat(20).getBytes(StandardCharsets.US_ASCII);
            byte[] back = GzipCompression.decompress(GzipCompression.compress(data));
            assertArrayEquals(data, back);
        }

        @Test
        void binaryPayloadRoundTrips() {
            byte[] data = new byte[256];
            for (int i = 0; i < 256; i++)
                data[i] = (byte) i;

            byte[] back = GzipCompression.decompress(GzipCompression.compress(data));
            assertArrayEquals(data, back);
        }

        @Test
        void repeatingPatternRoundTrips() {
            // High compression ratio - stresses the ISIZE pre-size against an output much larger
            // than the input.
            byte[] data = new byte[64 * 1024];
            for (int i = 0; i < data.length; i++)
                data[i] = (byte) ('A' + (i % 8));

            byte[] back = GzipCompression.decompress(GzipCompression.compress(data));
            assertArrayEquals(data, back);
        }

        @Test
        void incompressibleRandomRoundTrips() {
            // Low compression ratio - stresses the deflateBound formula against output close to
            // input size.
            byte[] data = new byte[1024 * 1024];
            new Random(42).nextBytes(data);

            byte[] back = GzipCompression.decompress(GzipCompression.compress(data));
            assertArrayEquals(data, back);
        }

        @Test
        void offsetSliceCompressRoundTrips() throws IOException {
            byte[] full = new byte[100];
            new Random(7).nextBytes(full);
            byte[] slice = new byte[80];
            System.arraycopy(full, 16, slice, 0, 80);

            byte[] gzipped = GzipCompression.compress(full, 16, 80);
            byte[] back = GzipCompression.decompress(gzipped);
            assertArrayEquals(slice, back);
        }

        @Test
        void compressWithoutOffsetMatchesOffsetZero() {
            byte[] data = "round trip".getBytes(StandardCharsets.US_ASCII);
            byte[] back1 = GzipCompression.decompress(GzipCompression.compress(data));
            byte[] back2 = GzipCompression.decompress(GzipCompression.compress(data, 0, data.length));
            assertArrayEquals(back1, back2);
            assertArrayEquals(data, back1);
        }

    }

    // -----------------------------------------------------------------
    // Trailer-read guard fallback
    // -----------------------------------------------------------------

    @Nested
    class Guard {

        @Test
        void decompressNullReturnsNull() {
            assertNull(GzipCompression.decompress(null));
        }

        @Test
        void decompressEmptyReturnsEmpty() {
            byte[] empty = new byte[0];
            assertArrayEquals(empty, GzipCompression.decompress(empty));
        }

        @Test
        void decompressTooShortFallsBackCleanly() {
            // Gzip magic but only 3 bytes - shorter than GZIP_MIN_LENGTH (18). Must NOT
            // AIOOBE on the trailer read; should surface as CompressionException via the fallback.
            byte[] tiny = { (byte) 0x1F, (byte) 0x8B, (byte) 0x08 };
            assertThrows(CompressionException.class, () -> GzipCompression.decompress(tiny));
        }

        @Test
        void decompressNonGzipMagicThrowsCompressionException() {
            byte[] not_gzip = { 0, 1, 2, 3, 4, 5 };
            CompressionException ex = assertThrows(CompressionException.class, () -> GzipCompression.decompress(not_gzip));
            assertNotNull(ex.getMessage());
        }

        @Test
        void decompressTruncatedGzipThrowsCompressionException() throws IOException {
            byte[] data = "round trip".repeat(1000).getBytes(StandardCharsets.US_ASCII);
            byte[] full = referenceDeflate(data);
            // Strip the trailing 4 bytes (ISIZE field) to force a truncated read. The shortened
            // bytes still pass the >= 18 length check but the inflater will error mid-stream.
            byte[] truncated = new byte[full.length - 8];
            System.arraycopy(full, 0, truncated, 0, truncated.length);
            assertThrows(CompressionException.class, () -> GzipCompression.decompress(truncated));
        }

        @Test
        void decompressMultiMemberGzipReturnsConcatenatedPayload() throws IOException {
            byte[] part1 = "first member ".repeat(50).getBytes(StandardCharsets.US_ASCII);
            byte[] part2 = "second member ".repeat(50).getBytes(StandardCharsets.US_ASCII);
            byte[] g1 = referenceDeflate(part1);
            byte[] g2 = referenceDeflate(part2);
            byte[] concat = new byte[g1.length + g2.length];
            System.arraycopy(g1, 0, concat, 0, g1.length);
            System.arraycopy(g2, 0, concat, g1.length, g2.length);

            byte[] expected = new byte[part1.length + part2.length];
            System.arraycopy(part1, 0, expected, 0, part1.length);
            System.arraycopy(part2, 0, expected, part1.length, part2.length);

            // Fast path under-allocates (ISIZE only describes the last member); the runtime
            // multi-member guard must bail and the fallback must drain both members.
            byte[] back = GzipCompression.decompress(concat);
            assertArrayEquals(expected, back);
        }

        @Test
        void decompressHandCraftedNegativeIsizeFallsBack() throws IOException {
            byte[] data = "round trip payload".getBytes(StandardCharsets.US_ASCII);
            byte[] gzipped = referenceDeflate(data);
            // Overwrite the trailing ISIZE field with 0xFFFFFFFF so readIsizeTrailer returns -1.
            // The guard must reject the fast path and use the growable fallback, which doesn't
            // consult ISIZE at all and produces the correct uncompressed bytes.
            int l = gzipped.length;
            gzipped[l - 4] = (byte) 0xFF;
            gzipped[l - 3] = (byte) 0xFF;
            gzipped[l - 2] = (byte) 0xFF;
            gzipped[l - 1] = (byte) 0xFF;

            // The growable path still decodes correctly because gzip's CRC validation happens
            // before ISIZE; the GZIPInputStream will reject this stream because the trailer
            // checksum no longer matches. So we expect a CompressionException, not silent success.
            assertThrows(CompressionException.class, () -> GzipCompression.decompress(gzipped));
        }

    }

    // -----------------------------------------------------------------
    // Legacy-path equivalence
    // -----------------------------------------------------------------

    @Nested
    class LegacyEquivalence {

        private void assertFastMatchesReference(byte[] payload) throws IOException {
            byte[] gzipped = referenceDeflate(payload);
            byte[] fast = GzipCompression.decompress(gzipped);
            byte[] reference = referenceInflate(gzipped);
            assertArrayEquals(reference, fast);
            assertArrayEquals(payload, fast);
        }

        @Test
        void fastPathMatchesGrowablePath_small() throws IOException {
            assertFastMatchesReference("hello world".getBytes(StandardCharsets.US_ASCII));
        }

        @Test
        void fastPathMatchesGrowablePath_medium() throws IOException {
            assertFastMatchesReference("the quick brown fox ".repeat(2000).getBytes(StandardCharsets.US_ASCII));
        }

        @Test
        void fastPathMatchesGrowablePath_oneMegabyte() throws IOException {
            byte[] data = new byte[1024 * 1024];
            new Random(13).nextBytes(data);
            assertFastMatchesReference(data);
        }

    }

    // -----------------------------------------------------------------
    // Compression dispatch wiring
    // -----------------------------------------------------------------

    @Nested
    class CompressionDispatch {

        @Test
        void compressionDecompressGzipDelegatesToFastPath() throws IOException {
            byte[] data = "dispatched via Compression.decompress".getBytes(StandardCharsets.US_ASCII);
            byte[] gzipped = referenceDeflate(data);
            byte[] back = Compression.decompress(gzipped);
            assertArrayEquals(data, back);
        }

        @Test
        void compressionCompressGzipDelegatesToBoundedDeflate() {
            byte[] data = "dispatched via Compression.compress".getBytes(StandardCharsets.US_ASCII);
            byte[] gzipped = Compression.compress(data, Compression.GZIP);
            byte[] back = Compression.decompress(gzipped);
            assertArrayEquals(data, back);
        }

        @Test
        void compressionDecompressZlibStillUsesGrowablePath() {
            // ZLIB path untouched - verify round-trip still works.
            byte[] data = "zlib round trip".getBytes(StandardCharsets.US_ASCII);
            byte[] zlibbed = Compression.compress(data, Compression.ZLIB);
            byte[] back = Compression.decompress(zlibbed);
            assertArrayEquals(data, back);
        }

        @Test
        void compressionCompressZlibStillUsesGrowablePath() {
            byte[] data = new byte[2048];
            new Random(99).nextBytes(data);
            byte[] zlibbed = Compression.compress(data, Compression.ZLIB);
            byte[] back = Compression.decompress(zlibbed);
            assertEquals(data.length, back.length);
            assertArrayEquals(data, back);
        }

    }

}
