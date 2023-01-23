package org.helsinki.paytrail.model.tokenization;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Customer {

    @JsonProperty("network_address")
    private String networkAddress;

    @JsonProperty("country_code")
    private String countryCode;
}
