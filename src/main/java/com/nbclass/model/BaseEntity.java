package com.nbclass.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * BaseEntity
 *
 * @author nbclass
 * @version V1.0
 * @date 2019-10-11
 */
@Data
class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 状态：1有效; 0无效
     */
    private Integer status;

    /**
     * 创建者id
     */
    private Integer createId;

    /**
     * 更新者id
     */
    private Integer updateId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


}
