package org.helsinki.paytrail.model.tokenization;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.helsinki.paytrail.constants.CardCategory;
import org.helsinki.paytrail.constants.CardFunding;
import org.helsinki.paytrail.constants.CvcRequired;

@Data
public class Card {
    private String type;
    private String bin;
    private CardFunding funding;
    private CardCategory category;

    @JsonProperty("partial_pan")
    private String partialPan;

    @JsonProperty("expire_year")
    private String expireYear;

    @JsonProperty("expire_month")
    private String expireMonth;

    @JsonProperty("cvc_required")
    private CvcRequired cvcRequired;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("pan_fingerprint")
    private String panFingerprint;

    @JsonProperty("card_fingerprint")
    private String cardFingerprint;
}
