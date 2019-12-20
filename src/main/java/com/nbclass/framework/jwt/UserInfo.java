package com.nbclass.framework.jwt;

import lombok.Data;

/**
* @author nbclass 2019-12-20
*/
@Data
public class UserInfo {
    private static final long serialVersionUID = 1L;

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
     * qq
     */
    private String qq;

    /**
     * 头像
     */
    private String avatar;
}