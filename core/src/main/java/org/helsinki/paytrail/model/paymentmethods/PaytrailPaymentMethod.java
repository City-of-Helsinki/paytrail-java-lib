package org.helsinki.paytrail.model.paymentmethods;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class PaytrailPaymentMethod implements Serializable {
	private String id;
	private String name;
	private String group;
	private String icon;
	private String svg;
}
