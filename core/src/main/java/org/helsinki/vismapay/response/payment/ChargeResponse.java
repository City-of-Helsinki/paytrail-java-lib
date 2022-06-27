package org.helsinki.vismapay.response.payment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.vismapay.response.VismaPayResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChargeResponse extends VismaPayResponse {
	private String token;
	private String type;
	private String[] errors;
}
