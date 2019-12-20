package com.nbclass.service;

import com.nbclass.framework.theme.ZbTheme;

import java.util.List;

public interface ThemeService {

    void useTheme(ZbTheme theme);

    ZbTheme selectCurrent();

    void initThymeleafVars();

    List<ZbTheme> selectAll();

    void updateSettings(String settingsJson);


}
