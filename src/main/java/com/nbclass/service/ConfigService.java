package com.nbclass.service;

import com.nbclass.vo.ConfigEmailVo;
import com.nbclass.vo.ConfigStorageVo;

import java.util.Map;


/**
 * ConfigService
 *
 * @author nbclass
 * @version V1.0
 * @date 2019-10-21
 */
public interface ConfigService {

    Map<String,String> selectAll();

    int updateByKey(String key, String value);

    ConfigStorageVo selectStorageConfig();

    void saveStorageConfig(ConfigStorageVo vo);

    ConfigEmailVo selectEmailConfig();

    void saveEmailConfig(ConfigEmailVo vo);

}
