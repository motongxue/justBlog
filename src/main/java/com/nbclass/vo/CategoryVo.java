package com.nbclass.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* @author nbclass 2019-10-18
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryVo extends PageVo{
    private static final long serialVersionUID = 1L;


    /**
    * 分类名称
    */
    private String name;


    /**
     * 0-目录，1-文章栏目，2-页面
     */
    private Integer type;

}