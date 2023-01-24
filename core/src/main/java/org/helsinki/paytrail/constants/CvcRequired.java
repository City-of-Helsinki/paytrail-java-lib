package org.helsinki.paytrail.constants;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CvcRequired {
    YES("yes"),
    NO("no"),
    NOT_TESTED("not_tested");

    @JsonValue
    private final String value;

    CvcRequired(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
