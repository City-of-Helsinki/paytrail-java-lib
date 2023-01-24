package org.helsinki.paytrail.response.tokenization;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.helsinki.paytrail.model.tokenization.PaytrailTokenResponse;
import org.helsinki.paytrail.response.PaytrailResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class PaytrailGetTokenResponse extends PaytrailResponse  {
    private PaytrailTokenResponse tokenResponse;
    private String[] errors;
}
