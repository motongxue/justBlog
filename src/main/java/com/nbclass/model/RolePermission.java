package com.nbclass.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * RolePermission
 *
 * @version V1.0
 * @date 2019/10/10
 * @author nbclass
 */
@Table(name = "sys_role_permission")
@EqualsAndHashCode(callSuper = true)
@Data
public class RolePermission extends BaseEntity{

    private static final long serialVersionUID = 8604426614836005740L;
    /**
     * 角色id
     */
    private String roleId;

    /**
     * 权限id
     */
    private String permissionId;

}