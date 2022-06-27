package org.helsinki.vismapay.response.payment;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.vismapay.response.VismaPayResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateRefundResponse  extends VismaPayResponse {
	private String type;

	@SerializedName("refund_id")
	private Long refundId;

	@SerializedName("refund_url")
	private String refundUrl;

	private String[] errors;
}