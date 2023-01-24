package org.helsinki.paytrail.constants;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CardCategory {
    BUSINESS("business"),
    PREPAID("prepaid"),
    UNKNOWN("unknown");

    @JsonValue
    private final String category;

    CardCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
