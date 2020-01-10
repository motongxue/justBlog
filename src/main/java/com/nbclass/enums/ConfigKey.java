package com.nbclass.enums;

public enum ConfigKey {
    CONFIG_STORAGE("CONFIG_STORAGE","云存储配置"),
    CONFIG_EMAIL("CONFIG_EMAIL","邮件配置"),
    SITE_HOST("SITE_HOST","网站域名"),
    SITE_CDN("SITE_CDN","网站CDN域名"),
    SITE_NAME("SITE_NAME","网站名称"),
    SITE_KWD("SITE_KWD","网站关键字"),
    SITE_DESC("SITE_DESC","网站描述"),
    SITE_ICON("SITE_ICON","网站图标"),
    SITE_LOGO("SITE_LOGO","网站LOGO"),
    EDITOR_TYPE("EDITOR_TYPE","编辑器类型"),
    SYSTEM_PAGE_VIEW("SYSTEM_PAGE_VIEW","系统访问数"),
    SYSTEM_CREATE_TIME("SYSTEM_CREATE_TIME","系统建立时间"),
    SYSTEM_IS_SET("SYSTEM_IS_SET","系统是否设置"),
    RESET_PWD_TYPE("RESET_PWD_TYPE","重置密码方式"),
    SECURITY_CODE("SECURITY_CODE","安全码"),
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