package com.nbclass.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* @author nbclass 2019-12-17
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class TagVo extends PageVo{
    private static final long serialVersionUID = 1L;

    /**
    * 名称
    */
    private String name;

}