package com.nbclass.model;

import java.io.Serializable;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Category
 *
 * @version V1.0
 * @date 2019/10/10
 * @author nbclass
 */
@Data
public class BlogCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
    * 分类名称
    */
    private String name;

    /**
    * 分类别名
    */
    private String aliasName;

    /**
    * 分类描述
    */
    private String description;

    /**
     * type 0-目录, 1-栏目
     */
    private Integer type;

    /**
    * pid
    */
    private Integer pid;

    /**
    * sort
    */
    private Integer sort;

    /**
    * 图标
    */
    private String icon;

    /**
    * 模板名称
    */
    private String template;

    /**
    * 用户状态：1有效; 0删除
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

    /**
     * 父节点
     */
    @Transient
    private BlogCategory parent;

    /**
     * 子节点
     */
    @Transient
    private List<BlogCategory> children;


    @Transient
    private Boolean checked;

    @Transient
    private Boolean open;


    @Transient
    private Boolean disabled;
}