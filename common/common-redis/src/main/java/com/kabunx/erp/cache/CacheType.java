package com.kabunx.erp.cache;

public enum CacheType {
    CAPTCHA("erp:captcha:"),
    SMS_CODE("erp:sms-code:");
    private final String type;

    CacheType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
