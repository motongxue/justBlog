package com.nbclass.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* @author nbclass 2019-10-18
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class FileVo extends PageVo{
    private static final long serialVersionUID = 1L;


    /**
    * 搜索关键字
    */
    private String key;

    /**
     * oss存储类型
     */
    private Integer ossType;

}