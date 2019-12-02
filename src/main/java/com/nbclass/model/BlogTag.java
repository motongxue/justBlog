package com.nbclass.model;

import java.io.Serializable;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

/**
*  zb_tag
* @author nbclass 2019-10-18
*/
@Data
public class BlogTag implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
    * 标签名称
    */
    private String name;

    /**
    * 状态：1有效; 0删除
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


}