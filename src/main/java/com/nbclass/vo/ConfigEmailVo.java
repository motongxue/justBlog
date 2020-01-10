package com.nbclass.vo;

import lombok.Data;

/**
 * EmailConfigVo
 *
 * @author nbclass
 * @version V1.0
 * @date 2020-01-10
 */
@Data
public class ConfigEmailVo {
    //是否设置
    private Integer setFlag;
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String from;
}
