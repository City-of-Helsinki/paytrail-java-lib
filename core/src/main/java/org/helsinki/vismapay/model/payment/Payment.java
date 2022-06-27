package org.helsinki.vismapay.model.payment;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class Payment implements Serializable {

	private Long id;
	private BigDecimal amount;
	private String currency;
	private Short status;

	@SerializedName("order_number")
	private String orderNumber;

	private Source source;

	/**
	 * Format: "YYYY-MM-DD HH-MM-SS" and in UTC-time.
	 * TODO: datetime conversion?
	 */
	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("refund_type")
	private String refundType;

	private Customer customer;
	private Refund[] refunds;

	@SerializedName("payment_products")
	private Product[] paymentProducts;
}
