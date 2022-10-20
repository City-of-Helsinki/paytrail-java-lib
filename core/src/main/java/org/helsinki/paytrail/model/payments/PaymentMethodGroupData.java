package org.helsinki.paytrail.model.payments;

import lombok.Data;
import org.helsinki.paytrail.constants.PaymentMethodGroup;

@Data
public class PaymentMethodGroupData {

    public PaymentMethodGroup id;
    public String name;
    public String icon;
    public String svg;
}
