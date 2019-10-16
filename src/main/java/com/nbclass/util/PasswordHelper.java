package com.nbclass.util;

import com.nbclass.model.User;

import java.util.Objects;

/**
 * 密码工具类
 *
 * @author nbclass
 * @version V1.0
 * @date 2019-10-11
 */
public class PasswordHelper {

 /**
  * 生成密码
  *
  * @param user
  * @return user
  */
    public static User encryptPassword(User user) {
        user.setPassword(MD5.md5Hex(user.getPassword() + user.getSalt()));
        return user;
    }

    /**
     * 校验密码
     *
     * @param password
     * @param user
     * @return boolean
     */
    public static boolean verify(String password, User user){
        return Objects.equals(MD5.md5Hex(password + user.getSalt()), user.getPassword());
    }
}
