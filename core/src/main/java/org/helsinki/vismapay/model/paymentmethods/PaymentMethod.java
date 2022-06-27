package org.helsinki.vismapay.model.paymentmethods;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class PaymentMethod implements Serializable {
	private String name;
	private String group;
	private String img;

	@SerializedName(value = "img_timestamp")
	private String imgTimestamp;

	@SerializedName(value = "min_amount")
	private BigInteger minAmount;

	@SerializedName(value = "max_amount")
	private BigInteger maxAmount;

	@SerializedName(value = "selected_value")
	private String selectedValue;

	private String[] currency;
}
