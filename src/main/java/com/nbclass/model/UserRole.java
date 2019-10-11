package com.nbclass.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * UserRole
 *
 * @version V1.0
 * @date 2019/10/10
 * @author nbclass
 */
@Table(name = "sys_user_role")
@EqualsAndHashCode(callSuper = true)
@Data
public class UserRole extends BaseEntity{

    private static final long serialVersionUID = 3195980821961960887L;
    /**
     * 用户id
     */
    private String userId;

    /**
     * 角色id
     */
    private String roleId;

}