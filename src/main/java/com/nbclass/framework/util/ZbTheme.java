package com.nbclass.framework.util;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

@Data
public class ZbTheme implements Comparator<ZbTheme> {

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 图片
     */
    private String img;

    /**
     * 是否可设置：1-是，0-否
     */
    private Integer setFlag;

    /**
     * 设置内容的json
     */
    private String settings;
    /**
     * 状态 1-启用，0-未启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @Override
    public int compare(ZbTheme left, ZbTheme right) {
        return left.getName().compareTo(right.getName());
    }

}