package com.nbclass.service.impl;

import com.nbclass.enums.CacheKeyPrefix;
import com.nbclass.framework.util.ZbTheme;
import com.nbclass.service.RedisService;
import com.nbclass.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThemeServiceImpl implements ThemeService {

    @Autowired
    private RedisService redisService;

    @Override
    public void useTheme(ZbTheme theme) {
        redisService.set(CacheKeyPrefix.CURRENT_THEME.getPrefix(), theme);;
    }

    @Override
    public ZbTheme selectCurrent() {
        return redisService.get(CacheKeyPrefix.CURRENT_THEME.getPrefix());
    }

}
