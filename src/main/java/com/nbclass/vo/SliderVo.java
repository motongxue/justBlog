package com.nbclass.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* @author nbclass 2019-12-18
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class SliderVo extends PageVo{
    private static final long serialVersionUID = 1L;


    /**
    * 标题
    */
    private String title;


    /**
     * 1,网站公告，2-其他轮播
     */
    private Integer type;

}