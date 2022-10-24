package org.helsinki.paytrail.model.payments;

import lombok.Data;
import org.helsinki.paytrail.constants.PaymentMethodGroup;

@Data
public class PaymentMethodGroupData {
    private PaymentMethodGroup id;
    private String name;
    private String icon;
    private String svg;
}
