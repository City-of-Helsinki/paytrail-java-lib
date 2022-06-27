package org.helsinki.vismapay.response.payment;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChargeCardTokenResponse extends CardTokenResponse {
	private Byte settled;
	private Verify verify;
	private String[] errors;

	@Data
	public static class Verify {
		private String token;
		private String type;
	}
}
