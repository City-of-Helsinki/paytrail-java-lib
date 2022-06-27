package org.helsinki.vismapay.model.payment;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class Product implements Serializable {
	private static final long serialVersionUID = -823447845648L;

	private String id;
	private String title;
	private Integer count;
	private Integer tax;

	@SerializedName(value = "pretax_price")
	private BigDecimal pretaxPrice;

	private BigDecimal price;
	private Integer type;

	@SerializedName(value = "merchant_id")
	private Long merchantId;

	/**
	 * Product's id in Visma Pay's system
	 */
	@SerializedName(value = "product_id")
	private Long productId;

	private String cp;

	@Override
	public final int hashCode() {
		return (id != null ? id.hashCode() : System.identityHashCode(this));
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!this.getClass().isAssignableFrom(o.getClass())) {
			return false;
		}

		final Product that = (Product) o;

		if (id == null || that.id == null) {
			return false;
		}
		return id.equals(that.id);
	}
}
