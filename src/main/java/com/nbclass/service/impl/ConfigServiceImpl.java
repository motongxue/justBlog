package com.nbclass.service.impl;

import com.nbclass.enums.CacheKeyPrefix;
import com.nbclass.enums.ConfigKey;
import com.nbclass.framework.annotation.RedisCache;
import com.nbclass.framework.util.GsonUtil;
import com.nbclass.mapper.ConfigMapper;
import com.nbclass.model.BlogConfig;
import com.nbclass.service.ConfigService;
import com.nbclass.service.RedisService;
import com.nbclass.vo.ConfigEmailVo;
import com.nbclass.vo.ConfigStorageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ConfigServiceImpl
 *
 * @author nbclass
 * @version V1.0
 * @date 2019-10-18
 */
@Service
public class ConfigServiceImpl implements ConfigService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private ConfigMapper configMapper;

    @Override
    public Map<String, String> selectAll() {
        Map<String, String> map = redisService.get(CacheKeyPrefix.SYS_CONFIG.getPrefix());
        if(map==null){
            List<BlogConfig> sysConfigs = configMapper.selectAll();
            map= new HashMap<>(sysConfigs.size());
            for (BlogConfig config : sysConfigs){
                map.put(config.getSysKey(),config.getSysValue());
            }
            redisService.set(CacheKeyPrefix.SYS_CONFIG.getPrefix(), map);
        }
        return map;
    }

    @Override
    public int updateByKey(String key,String value) {
        clearConfigCache();
        return configMapper.updateByKey(key, value);
    }

    @Override
    @RedisCache(key = "CONFIG_STORAGE")
    public ConfigStorageVo selectStorageConfig() {
        return GsonUtil.fromJson(configMapper.getByKey(ConfigKey.CONFIG_STORAGE.getValue()),ConfigStorageVo.class);
    }

    @Override
    @RedisCache(key = "CONFIG_STORAGE", flush = true)
    public void saveStorageConfig(ConfigStorageVo vo) {
        clearConfigCache();
        configMapper.updateByKey(ConfigKey.CONFIG_STORAGE.getValue(),GsonUtil.toJson(vo));
    }

    @Override
    @RedisCache(key = "CONFIG_EMAIL")
    public ConfigEmailVo selectEmailConfig() {
        return GsonUtil.fromJson(configMapper.getByKey(ConfigKey.CONFIG_EMAIL.getValue()),ConfigEmailVo.class);
    }

    @Override
    @RedisCache(key = "CONFIG_EMAIL", flush = true)
    public void saveEmailConfig(ConfigEmailVo vo) {
        clearConfigCache();
        configMapper.updateByKey(ConfigKey.CONFIG_EMAIL.getValue(), GsonUtil.toJson(vo));
    }


    private void clearConfigCache(){
        redisService.del(CacheKeyPrefix.SYS_CONFIG.getPrefix());
    }
}
