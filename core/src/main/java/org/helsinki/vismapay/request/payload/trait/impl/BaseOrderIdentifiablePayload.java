package org.helsinki.vismapay.request.payload.trait.impl;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import org.helsinki.vismapay.request.payload.trait.OrderIdentifiable;

@EqualsAndHashCode(callSuper = true)
public class BaseOrderIdentifiablePayload<T extends VismaPayPayload<T> & OrderIdentifiable<T>>
		extends VismaPayPayload<T>
		implements OrderIdentifiable<T> {

	@SerializedName("order_number")
	private String orderNumber;

	public String getOrderNumber() {
		return orderNumber;
	}

	public T setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
		return (T) this;
	}
}
