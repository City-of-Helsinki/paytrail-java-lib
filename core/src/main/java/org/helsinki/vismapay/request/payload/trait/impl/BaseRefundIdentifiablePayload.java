package org.helsinki.vismapay.request.payload.trait.impl;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import org.helsinki.vismapay.request.payload.trait.RefundIdentifiable;

@EqualsAndHashCode(callSuper = true)
public class BaseRefundIdentifiablePayload<T extends VismaPayPayload<T> & RefundIdentifiable<T>>
		extends VismaPayPayload<T>
		implements RefundIdentifiable<T> {

	@SerializedName("refund_id")
	private Long refundId;

	public Long getRefundId() {
		return refundId;
	}

	public T setRefundId(Long refundId) {
		this.refundId = refundId;
		return (T) this;
	}
}
