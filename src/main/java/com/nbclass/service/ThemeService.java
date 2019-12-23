package com.nbclass.service;

import com.nbclass.framework.theme.ZbTheme;

import java.util.List;

public interface ThemeService {

    void useTheme(String themeId);

    ZbTheme selectCurrent();

    List<ZbTheme> selectAll();

    ZbTheme selectByThemeId(String themeId);

    void updateSettings(String themeId, String settingJson);

    String getFileContent(String absolutePath);

    void saveFileContent(String absolutePath, String content);


}
