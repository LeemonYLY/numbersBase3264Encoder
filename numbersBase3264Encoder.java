package numbersBase3264Encoder;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

// Enter two numbers
// encode the sum of the two values with a certain probability
public class numbersBase3264Encoder {
	private static final char[] BASE32_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567".toCharArray();

	public static void main(String[] args) {
		// Both parameters range [0,100000]
		double a = Double.parseDouble(args[0]);
		double b = Double.parseDouble(args[1]);

		double probability = 0.35;

		int result = encodeWithProbability(a, b, probability);
		result = scale(result);

		System.out.print(result);
	}

	public static String base32Encode(byte[] data) {
		StringBuilder encoded = new StringBuilder();

		int bitBuffer = 0;
		int bitCount = 0;

		for (byte b : data) {
			bitBuffer = (bitBuffer << 8) | (b & 0xFF);
			bitCount += 8;

			while (bitCount >= 5) {
				int index = (bitBuffer >>> (bitCount - 5)) & 0x1F;
				encoded.append(BASE32_CHARS[index]);
				bitCount -= 5;
			}
		}

		// Handles the remaining bits of less than 5 bits
		if (bitCount > 0) {
			int index = (bitBuffer << (5 - bitCount)) & 0x1F;
			encoded.append(BASE32_CHARS[index]);
		}

		return encoded.toString();
	}

	public static int encodeWithProbability(double a, double b, double probability) {
		double random = Math.random();

		int result = 0;

		String base64encodedString = null;
		try {
			base64encodedString = Base64.getEncoder().encodeToString(String.valueOf(a + b).getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String base32encodedString = null;
		try {
			base32encodedString = base32Encode(String.valueOf(a + b).getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (random < probability) {
			// Use base64 encoding
			result = base64encodedString.hashCode();
		} else {
			// Use base32 encoding
			result = base32encodedString.hashCode();
		}

		return result;
	}

	// When the absolute value of the number is too large, keep dividing by 2 until
	// the absolute value <=1000
	public static int scale(int value) {
		while (value > 1000 || value < -1000) {
			value /= 2;
		}
		return value;
	}
}
