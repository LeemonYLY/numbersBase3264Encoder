package numbersBase3264Encoder;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

//输入两个数，以一定概率对两个值的和进行base64或base32编码
public class numbersBase3264Encoder {
	private static final char[] BASE32_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567".toCharArray();

	public static void main(String[] args) {
		// 2个参数范围均为[0,100000]
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

		// 处理剩余的不足5位的位
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
			// 使用base64编码
			result = base64encodedString.hashCode();
		} else {
			// 使用base32编码
			result = base32encodedString.hashCode();
		}

		return result;
	}

	// 当数字绝对值太大时，不断除以2，直到绝对值<=5000
	public static int scale(int value) {
		while (value > 1000 || value < -1000) {
			value /= 2;
		}
		return value;
	}
}
