package com.nbclass.service;

import com.nbclass.framework.theme.ZbTheme;

public interface ThymeleafService {

    void init();

    void initConfig();

    void initCurrentTheme(ZbTheme theme);

}
