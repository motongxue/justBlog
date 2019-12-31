package com.nbclass.service.impl;

import com.nbclass.enums.ConfigKey;
import com.nbclass.mapper.ConfigMapper;
import com.nbclass.service.PageViewService;
import com.nbclass.service.RedisService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * PageViewServiceImpl
 *
 * @author nbclass
 * @version V1.0
 * @date 2019-12-31
 */
@Service
public class PageViewServiceImpl implements PageViewService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private ConfigMapper configMapper;

    @Override
    public int updateSystemPageViewNum() {
        String sysValue = configMapper.getByKey(ConfigKey.SYSTEM_PAGE_VIEW.getValue());
        String saveValue = StringUtils.isBlank(sysValue)?"1":String.valueOf(Integer.valueOf(sysValue)+1);
        redisService.set(ConfigKey.SYSTEM_PAGE_VIEW.getValue(),saveValue);
        return configMapper.updateByKey(ConfigKey.SYSTEM_PAGE_VIEW.getValue(), saveValue);
    }

    @Override
    public int getSystemPageViewNum() {
        return redisService.get(ConfigKey.SYSTEM_PAGE_VIEW.getValue());
    }

}
