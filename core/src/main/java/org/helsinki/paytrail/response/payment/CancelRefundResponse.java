package org.helsinki.paytrail.response.payment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.paytrail.response.VismaPayResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class CancelRefundResponse extends VismaPayResponse {
	private String[] errors;
}