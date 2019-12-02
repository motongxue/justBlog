package com.nbclass.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
* @author nbclass 2019-10-18
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class LinkVo  extends PageVo{
    private static final long serialVersionUID = 1L;


    /**
    * 链接名
    */
    private String name;

    /**
    * 链接地址
    */
    private String url;

    /**
    * 状态
    */
    private Boolean status;

}