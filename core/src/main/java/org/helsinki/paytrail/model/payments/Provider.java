package org.helsinki.paytrail.model.payments;

import lombok.Data;
import org.helsinki.paytrail.constants.PaymentMethodGroup;

import java.util.List;

@Data
public class Provider {
    private String url;
    private String icon;
    private String svg;
    private PaymentMethodGroup group;
    private String name;
    private String id;
    private List<FormField> parameters;
}
