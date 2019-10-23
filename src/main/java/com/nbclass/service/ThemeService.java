package com.nbclass.service;

import com.nbclass.framework.util.ZbTheme;

public interface ThemeService {

    void useTheme(ZbTheme theme);

    ZbTheme selectCurrent();

}
