package com.nbclass.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
/**
 * Role
 *
 * @version V1.0
 * @date 2019/10/10
 * @author nbclass
 */
@Table(name = "sys_role")
@EqualsAndHashCode(callSuper = true)
@Data
public class Role extends BaseEntity{

    private static final long serialVersionUID = 8948779694350766194L;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色描述
     */
    private String description;

}