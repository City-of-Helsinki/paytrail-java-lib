package org.helsinki.paytrail.response.payment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.paytrail.model.payment.Source;
import org.helsinki.paytrail.response.VismaPayResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class CardTokenResponse extends VismaPayResponse {
	private Source source;
}
