package com.nbclass.framework.theme;

import lombok.Data;

import java.util.List;

@Data
public class ZbThemeSetting {

    /**
     * 表单名称
     */
    private String label;

    /**
     * 配置中的form
     */
    private List<ZbThemeForm> form;

}