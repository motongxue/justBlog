package com.nbclass.framework.theme;

import lombok.Data;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class ZbTheme implements Comparator<ZbTheme> {

    /**
     * 主题主键
     */
    private String id;

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
     * 系统默认设置的配置信息
     */
    private List<ZbThemeSetting> settings;

    /**
     * 用户设置的配置map
     */
    private Map<String,String> setting;

    /**
     * html模板
     */
    private List<String> templates;

    /**
     * 版本号
     */
    private String version;

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
        return left.getId().compareTo(right.getId());
    }

}