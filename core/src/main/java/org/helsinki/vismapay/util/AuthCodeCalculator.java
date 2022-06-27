package org.helsinki.vismapay.util;

import lombok.NonNull;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class AuthCodeCalculator {

	public static String calcAuthCode(@NonNull String secretKey, @NonNull String data) {
		return toHexString(calcHmacSha256(
				secretKey.getBytes(StandardCharsets.UTF_8),
				data.getBytes(StandardCharsets.UTF_8)
		)).toUpperCase();
	}

	private static String toHexString(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
	
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
	
		return hexString.toString();
	}

	private static byte[] calcHmacSha256(byte[] secretKey, byte[] data) {
		byte[] hmacSha256;

		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
			mac.init(secretKeySpec);
			hmacSha256 = mac.doFinal(data);
		} catch (Exception e) {
			throw new RuntimeException("Failed to calculate hmac-sha256", e);
		}
		return hmacSha256;
	}
}
