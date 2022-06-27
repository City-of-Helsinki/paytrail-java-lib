package org.helsinki.vismapay.request.payload.trait;

public interface OrderIdentifiable<T extends OrderIdentifiable<T>> extends OrderIdentified {

	T setOrderNumber(String orderNumber);
}