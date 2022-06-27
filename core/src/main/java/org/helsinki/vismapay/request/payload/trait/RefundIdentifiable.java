package org.helsinki.vismapay.request.payload.trait;

public interface RefundIdentifiable<T extends RefundIdentifiable<T>> extends RefundIdentified {

	T setRefundId(Long refundId);
}