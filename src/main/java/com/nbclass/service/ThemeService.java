package com.nbclass.service;

import com.nbclass.framework.theme.ZbTheme;
import com.nbclass.vo.ResponseVo;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface ThemeService {

    void useTheme(String themeId);

    ZbTheme selectCurrent();

    List<ZbTheme> selectAll();

    Map<String, ZbTheme> selectThemesMap();

    ZbTheme selectByThemeId(String themeId);

    void updateSettings(String themeId, String settingJson);

    void delete(String themeId);

    String getFileContent(String absolutePath);

    void saveFileContent(String absolutePath, String content);

    ResponseVo upload(MultipartFile file);

    Path getSysTemplatePath();

    Path getSysThemePath(String themeId);

    Path getUserThemePath();

    Path getUserThemePath(String themeId);

    void handleThemeSetting(ZbTheme theme);
}
