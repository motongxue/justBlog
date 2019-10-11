package com.nbclass.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * User
 *
 * @version V1.0
 * @date 2019/10/10
 * @author nbclass
 */
@Table(name = "sys_user")
@EqualsAndHashCode(callSuper = true)
@Data
public class User extends BaseEntity{

    private static final long serialVersionUID = -460531621106843096L;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 加密盐值
     */
    private String salt;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 年龄：1男2女
     */
    private Integer sex;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 微信二维码
     */
    private String wxCode;

    /**
     * 微信收款码
     */
    private String wxPayCode;

    /**
     * 支付宝收款码
     */
    private String aliPayCode;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;


    /**
     * 角色
     */
    @Transient
    private List<Role> roles;


}