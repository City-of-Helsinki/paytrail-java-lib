package org.helsinki.paytrail.request.payload.trait;

public interface OrderIdentifiable<T extends OrderIdentifiable<T>> extends OrderIdentified {

	T setOrderNumber(String orderNumber);
}