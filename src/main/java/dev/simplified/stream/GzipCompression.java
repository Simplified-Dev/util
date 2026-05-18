package dev.simplified.stream;

import dev.simplified.stream.exception.CompressionException;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Low-allocation gzip compress and decompress helpers that pre-size buffers from gzip framing
 * metadata.
 *
 * <p>Both fast paths bypass the growable {@link ByteArrayOutputStream} accumulator that the
 * generic {@link Compression#decompress(byte[])} / {@link Compression#compress(byte[], Compression)}
 * paths use, eliminating the {@link java.util.Arrays#copyOf} doubling sequence the default
 * 32-byte initial capacity triggers on every non-trivial payload.
 *
 * <p><b>Inflate.</b> Gzip's trailer carries the uncompressed payload size in its last four bytes
 * (the {@code ISIZE} field, little-endian, mod {@code 2^32}) per
 * <a href="https://www.rfc-editor.org/rfc/rfc1952#section-2.3.1">RFC 1952 &sect;2.3.1</a>.
 * Reading it lets us allocate the output buffer at exactly the right size with a single
 * allocation, then drive {@link GZIPInputStream} to fill it. No growable accumulator and no trim
 * copy at the end - the buffer returned to the caller is the buffer the inflater wrote into.
 *
 * <p><b>Deflate.</b> Exact output size is not knowable upfront (that is what compression buys us),
 * but zlib's {@code deflateBound} formula tightly upper-bounds it given the input size. Pre-sizing
 * a {@link ByteArrayOutputStream} to that bound eliminates growth copies during deflate. The final
 * {@link ByteArrayOutputStream#toByteArray} call still trims the unused tail, so one trim copy
 * remains - smaller win than inflate, but still real.
 *
 * <p>Both fast paths degrade gracefully to the original growable path under any of the following
 * conditions (the trailer-read guard):
 *
 * <ul>
 *   <li><b>Input too short</b> - fewer than 18 bytes (the gzip framing minimum: 10-byte header
 *       plus 8-byte trailer). Reading the {@code ISIZE} field would fall off the array.</li>
 *   <li><b>{@code ISIZE} wraps negative</b> - the field is {@code mod 2^32}, so an uncompressed
 *       size at or above 2 GiB reads as a negative {@code int}, which cannot be used as a
 *       {@code byte[]} allocation length.</li>
 *   <li><b>{@code ISIZE} exceeds the DEFLATE ratio cap</b> - the theoretical maximum DEFLATE
 *       compression ratio is roughly 1032:1; a trailer that implies a higher ratio (e.g. when
 *       the stream is truncated and the last 4 bytes are random deflate-symbol bytes that
 *       happen to decode as a huge positive int) is treated as untrustworthy.</li>
 *   <li><b>Multi-member gzip</b> - RFC 1952 allows concatenating gzip members in a single stream;
 *       the trailing {@code ISIZE} only describes the last member, so a fast-path read would
 *       under-allocate. Detected by reading one extra byte after the buffer is filled - if data
 *       remains, the input is multi-member.</li>
 *   <li><b>Truncated</b> - {@link GZIPInputStream#read} returns end-of-stream before {@code ISIZE}
 *       bytes have been delivered. The fallback surfaces a more accurate error.</li>
 * </ul>
 *
 * <p>Most callers should go through {@link Compression}, which auto-detects format and dispatches
 * here for gzip. Direct calls are fine when the format is known to be gzip in advance.
 */
@UtilityClass
public final class GzipCompression {

	/**
	 * Minimum size of a valid gzip member: 10-byte header plus 8-byte trailer. Inputs shorter than
	 * this cannot carry an {@code ISIZE} field and are routed to the fallback path so the
	 * trailer-read never falls off the array.
	 */
	private static final int GZIP_MIN_LENGTH = 18;

	/**
	 * Inflater read-buffer size matching {@link Compression#wrap(java.io.InputStream)}'s GZIP
	 * branch - large enough to amortize the JNI boundary on multi-KiB payloads.
	 */
	private static final int GZIP_INFLATE_BUFFER_BYTES = 65_536;

	/**
	 * Inflate-fallback read-loop scratch size matching {@link Compression#decompress(byte[])}'s
	 * legacy loop - small enough that the scratch lives in L1 cache without crowding out the
	 * inflater's working set.
	 */
	private static final int FALLBACK_LOOP_BUFFER_BYTES = 8_192;

	/**
	 * Theoretical maximum DEFLATE compression ratio (per RFC 1951's LZ77 limits, citing the zlib
	 * author Mark Adler's analysis). Used as a sanity cap on the {@code ISIZE} trailer read - a
	 * value implying a higher ratio than this means the trailer is almost certainly garbage
	 * (e.g. a truncated stream whose last 4 bytes happen to decode as a large positive int), and
	 * the fast path bails to the fallback rather than risk an arbitrary-size buffer allocation.
	 */
	private static final int MAX_DEFLATE_RATIO = 1032;

	/**
	 * Gzip magic byte 0 ({@code 0x1F}). Mirrors {@link Compression#GZIP}'s magic table entry.
	 */
	private static final int GZIP_MAGIC_0 = 0x1F;

	/**
	 * Gzip magic byte 1 ({@code 0x8B}). Mirrors {@link Compression#GZIP}'s magic table entry.
	 */
	private static final int GZIP_MAGIC_1 = 0x8B;

	/**
	 * Decompresses a gzip-framed byte array into the uncompressed payload.
	 *
	 * <p>Tries the {@code ISIZE}-pre-sized fast path first. When the trailer-read guard rejects
	 * the input (too short, {@code ISIZE} wrapped negative) or the inflater detects a fast-path
	 * assumption violation at runtime (truncated, multi-member), falls back to the legacy
	 * {@link GZIPInputStream}-plus-{@link ByteArrayOutputStream} growable path. Either path
	 * produces byte-identical output.
	 *
	 * <p>Mirrors {@link Compression#decompress(byte[])}'s null and empty handling - both return
	 * the input unchanged. Inputs that lack the gzip magic ({@code 0x1F 0x8B}) raise
	 * {@link CompressionException}, since this method assumes the caller already established the
	 * format.
	 *
	 * @param gzipped gzip-compressed bytes; {@code null} or empty is returned unchanged
	 * @return the uncompressed bytes, or {@code gzipped} when {@code null} / empty
	 * @throws CompressionException if {@code gzipped} is non-empty but does not start with the
	 *     gzip magic, or if an {@link IOException} escapes the underlying inflater
	 */
	public static byte @Nullable [] decompress(byte @Nullable [] gzipped) throws CompressionException {
		if (gzipped == null || gzipped.length == 0)
			return gzipped;

		if (!hasGzipMagic(gzipped))
			throw new CompressionException("Input does not have a gzip magic header (0x1F 0x8B)");

		// Trailer-read guard: only attempt the ISIZE pre-size when there's enough room for it,
		// the field decodes to a non-negative int, AND the apparent compression ratio is within
		// the theoretical DEFLATE bound. A truncated stream whose final 4 bytes happen to read
		// as a huge positive int would otherwise trigger a multi-GiB allocation here.
		if (gzipped.length >= GZIP_MIN_LENGTH) {
			int isize = readIsizeTrailer(gzipped);
			long isizeCeiling = (long) gzipped.length * MAX_DEFLATE_RATIO;

			if (isize >= 0 && isize <= isizeCeiling) {
				try {
					byte[] fast = inflateFastPath(gzipped, isize);

					if (fast != null)
						return fast;
				} catch (IOException exception) {
					throw new CompressionException(exception, "Gzip inflate failed");
				}
			}
		}

		try {
			return inflateFallback(gzipped);
		} catch (IOException exception) {
			throw new CompressionException(exception, "Gzip inflate failed");
		}
	}

	/**
	 * Convenience overload equivalent to {@code compress(data, 0, data.length)}.
	 *
	 * @param data the uncompressed payload
	 * @return the gzip-compressed bytes
	 * @throws CompressionException if an {@link IOException} escapes the underlying deflater
	 */
	public static byte @NotNull [] compress(byte @NotNull [] data) throws CompressionException {
		return compress(data, 0, data.length);
	}

	/**
	 * Compresses a slice of a byte array into a gzip-framed output.
	 *
	 * <p>Pre-sizes the {@link ByteArrayOutputStream} accumulator to zlib's {@code deflateBound}
	 * upper bound plus 18 bytes of gzip framing, so no growth copies occur during deflate. The
	 * final {@link ByteArrayOutputStream#toByteArray} call still trims the buffer to the actual
	 * compressed size, paying one trim copy.
	 *
	 * <p>The bound formula
	 * ({@code length + (length >> 12) + (length >> 14) + (length >> 25) + 13 + 18}) mirrors zlib's
	 * {@code deflateBound} for raw deflate plus 18 bytes for the gzip header (10) and trailer (8).
	 * For zero-length input it returns 31 bytes (vs the actual 20-byte empty gzip member) - a
	 * tight but provably safe upper bound.
	 *
	 * @param data the buffer containing the uncompressed payload
	 * @param offset the first valid byte in {@code data}
	 * @param length the number of valid bytes starting at {@code offset}
	 * @return the gzip-compressed bytes
	 * @throws CompressionException if an {@link IOException} escapes the underlying deflater
	 * @throws IndexOutOfBoundsException if {@code offset} or {@code length} address bytes outside
	 *     {@code data}, or either is negative
	 */
	public static byte @NotNull [] compress(byte @NotNull [] data, int offset, int length) throws CompressionException {
		java.util.Objects.checkFromIndexSize(offset, length, data.length);

		int bound = worstCaseDeflateBound(length);
		ByteArrayOutputStream baos = new ByteArrayOutputStream(bound);

		try (GZIPOutputStream gz = new GZIPOutputStream(baos)) {
			gz.write(data, offset, length);
		} catch (IOException exception) {
			throw new CompressionException(exception, "Gzip deflate failed");
		}

		return baos.toByteArray();
	}

	/**
	 * Returns {@code true} when {@code gzipped} starts with the 2-byte gzip magic
	 * ({@code 0x1F 0x8B}). Callers in {@link Compression} already dispatch on this; the
	 * defensive check here lets {@code GzipCompression} be safely called standalone.
	 */
	private static boolean hasGzipMagic(byte @NotNull [] gzipped) {
		return gzipped.length >= 2
			&& (gzipped[0] & 0xFF) == GZIP_MAGIC_0
			&& (gzipped[1] & 0xFF) == GZIP_MAGIC_1;
	}

	/**
	 * Reads the gzip {@code ISIZE} trailer (the last four bytes of {@code gzipped}, little-endian)
	 * and returns it as a signed {@code int}. The caller must have verified
	 * {@code gzipped.length >= 4} - this method does no bounds checking.
	 *
	 * <p>A negative result means the uncompressed payload is at least 2 GiB (gzip's {@code ISIZE}
	 * wraps at {@code 2^32}); callers should fall back to the growable path because Java arrays
	 * cannot be allocated at signed-negative sizes.
	 */
	private static int readIsizeTrailer(byte @NotNull [] gzipped) {
		int l = gzipped.length;
		return (gzipped[l - 4] & 0xFF)
			| ((gzipped[l - 3] & 0xFF) << 8)
			| ((gzipped[l - 2] & 0xFF) << 16)
			| ((gzipped[l - 1] & 0xFF) << 24);
	}

	/**
	 * Computes the worst-case gzip deflate output size for an input of {@code length} bytes.
	 *
	 * <p>Mirrors zlib's {@code deflateBound} formula exactly, then adds 18 bytes of gzip framing
	 * (10-byte header plus 8-byte trailer). The bound is a strict upper limit for the stock
	 * {@link java.util.zip.Deflater} compression at any level; the actual deflate output is
	 * smaller for any input that compresses at all.
	 */
	private static int worstCaseDeflateBound(int length) {
		return length + (length >> 12) + (length >> 14) + (length >> 25) + 13 + 18;
	}

	/**
	 * Inflates {@code gzipped} into a pre-allocated {@code byte[isize]} buffer in a single shot.
	 *
	 * <p>Returns {@code null} when the fast-path assumption turns out to be wrong at runtime:
	 *
	 * <ul>
	 *   <li>{@link GZIPInputStream#read} returns end-of-stream before {@code isize} bytes have
	 *       been delivered (truncated input).</li>
	 *   <li>{@link GZIPInputStream#read} returns a non-negative byte count after the buffer is
	 *       full (multi-member input - another gzip member begins after the first one ends).</li>
	 * </ul>
	 *
	 * <p>The caller must fall back to the growable path on a {@code null} return. The growable
	 * path handles both cases correctly - multi-member by draining all members concatenated, and
	 * truncated by surfacing a precise {@link IOException}.
	 */
	private static byte @Nullable [] inflateFastPath(byte @NotNull [] gzipped, int isize) throws IOException {
		byte[] out = new byte[isize];

		try (
			ByteArrayInputStream in = new ByteArrayInputStream(gzipped);
			GZIPInputStream gz = new GZIPInputStream(in, GZIP_INFLATE_BUFFER_BYTES)
		) {
			int read = 0;

			while (read < isize) {
				int n = gz.read(out, read, isize - read);

				if (n < 0)
					return null;

				read += n;
			}

			// Drain one more byte to detect a second gzip member; a non-negative result means
			// the ISIZE-derived pre-size was wrong and the caller must fall back to growable.
			if (gz.read() >= 0)
				return null;
		}

		return out;
	}

	/**
	 * Legacy growable inflate path - mirrors {@link Compression#decompress(byte[])}'s GZIP arm
	 * byte-for-byte. Used as the universal fallback when the fast path's pre-size assumptions
	 * cannot be verified or have been violated mid-inflate.
	 */
	private static byte @NotNull [] inflateFallback(byte @NotNull [] gzipped) throws IOException {
		try (
			ByteArrayInputStream in = new ByteArrayInputStream(gzipped);
			GZIPInputStream gz = new GZIPInputStream(in, GZIP_INFLATE_BUFFER_BYTES);
			ByteArrayOutputStream out = new ByteArrayOutputStream(Math.max(gzipped.length * 2, FALLBACK_LOOP_BUFFER_BYTES))
		) {
			byte[] buffer = new byte[FALLBACK_LOOP_BUFFER_BYTES];
			int n;

			while ((n = gz.read(buffer)) > 0)
				out.write(buffer, 0, n);

			return out.toByteArray();
		}
	}

}
