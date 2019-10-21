package com.nbclass.service;

import com.nbclass.model.BlogTheme;

public interface ThemeService {

    int useTheme(Integer id);

    BlogTheme selectCurrent();

    int deleteBatch(Integer[] ids);

}
