package org.helsinki.paytrail.model.tokenization;

import lombok.Data;

@Data
public class PaytrailTokenResponse {

    private String token;
    private Card card;
    private Customer customer;
}
