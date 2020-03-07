package com.nbclass.enums;

public enum CacheKeyPrefix {
    ARTICLE_LOOK("JUST_ARTICLE_LOOK_"),ARTICLE_LOVE("JUST_ARTICLE_LOVE_"),COMMENT_LOVE("JUST_COMMENT_LOVE_"),
    COMMENT_FLOOR("JUST_COMMENT_FLOOR_"),CURRENT_THEME("JUST_CURRENT_THEME"),THEMES("JUST_THEMES"),THEME("JUST_THEME_"),
    SYS_CONFIG("JUST_SYS_CONFIG"),SYS_USER("JUST_SYS_USER_"),SYS_PAGE_VIEW("JUST_SYS_PAGE_VIEW"),RESET_("JUST_RESET_")
    ;

    String prefix;

    CacheKeyPrefix(String prefix){
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
