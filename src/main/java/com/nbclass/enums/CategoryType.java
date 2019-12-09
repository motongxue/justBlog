package com.nbclass.enums;

/**
 * ResponseStatus
 *
 * @version V1.0
 * @date 2019/10/10
 * @author nbclass
 */
public enum CategoryType {

    CATEGORY(0, "目录"),
    ARTICLE_CATEGORY(1, "文章分类"),
    PAGE(2,"页面");

    private Integer type;
    private String desc;

    CategoryType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

}
