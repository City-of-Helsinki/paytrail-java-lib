package org.helsinki.paytrail.response.payment;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.paytrail.response.VismaPayResponse;

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