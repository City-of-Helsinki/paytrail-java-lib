package org.helsinki.vismapay.model.payment;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Customer implements Serializable {

	private static final long serialVersionUID = -892648357982L;
	private String firstname;
	private String lastname;
	private String email;

	@SerializedName(value = "address_street")
	private String addressStreet;

	@SerializedName(value = "address_city")
	private String addressCity;

	@SerializedName(value = "address_zip")
	private String addressZip;

	@SerializedName(value = "address_country")
	private String addressCountry;

	@SerializedName(value = "shipping_firstname")
	private String shippingFirstname;

	@SerializedName(value = "shipping_lastname")
	private String shippingLastname;

	@SerializedName(value = "shipping_email")
	private String shippingEmail;

	@SerializedName(value = "shipping_address_street")
	private String shippingAddressStreet;

	@SerializedName(value = "shipping_address_city")
	private String shippingAddressCity;

	@SerializedName(value = "shipping_address_zip")
	private String shippingAddressZip;

	@SerializedName(value = "shipping_address_country")
	private String shippingAddressCountry;
}
