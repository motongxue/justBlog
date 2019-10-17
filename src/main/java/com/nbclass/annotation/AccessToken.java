package com.nbclass.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * AccessToken
 *
 * @version V1.0
 * @date 2019/10/10
 * @author nbclass
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessToken {

}
