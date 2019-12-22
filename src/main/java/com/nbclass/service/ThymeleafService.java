package com.nbclass.service;

import com.nbclass.framework.theme.ZbTheme;

public interface ThymeleafService {

    void init();

    void initStaticPath();

    void initCurrentTheme(ZbTheme theme);

}
