package com.nbclass.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * User
 *
 * @version V1.0
 * @date 2019/10/10
 * @author nbclass
 */
@Table(name = "zb_user")
@Data
public class User {

    private static final long serialVersionUID = -460531621106843096L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
     * 是否是管理员：1-是，0-否
     */
    private Integer isAdmin;

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

    public User withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public User withNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public User withSalt(String salt) {
        this.salt = salt;
        return this;
    }

    public User withIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
        return this;
    }

    public User withStatus(Integer status) {
        this.status = status;
        return this;
    }

    public User withCreateId(Integer createId) {
        this.createId = createId;
        return this;
    }

    public User withUpdateId(Integer updateId) {
        this.updateId = updateId;
        return this;
    }

    public User withCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public User withUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }


}