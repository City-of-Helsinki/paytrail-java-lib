package org.helsinki.paytrail.model.payments;

import lombok.Data;
import org.helsinki.paytrail.constants.PaymentMethodGroup;

import java.util.List;

@Data
public class Provider {

    public String url;
    public String icon;
    public String svg;
    public PaymentMethodGroup group;
    public String name;
    public String id;
    public List<FormField> parameters;
}
