package com.nbclass.enums;

public enum ConfigKey {
    CLOUD_STORAGE_CONFIG("CLOUD_STORAGE_CONFIG","云存储配置"),
    SITE_HOST("SITE_HOST","网站域名"),
    SITE_CDN("SITE_CDN","网站CDN域名"),
    SITE_NAME("SITE_NAME","网站名称"),
    SITE_DESC("SITE_DESC","网站描述"),
    SITE_KWD("SITE_KWD","网站关键字"),
    EDITOR_TYPE("EDITOR_TYPE","编辑器类型"),
    ;

    private String value;
    private String describe;

    private ConfigKey(String value, String describe) {
        this.value = value;
        this.describe = describe;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescribe() {
        return this.describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

}