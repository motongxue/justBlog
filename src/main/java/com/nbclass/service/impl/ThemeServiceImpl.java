package com.nbclass.service.impl;

import com.nbclass.framework.util.CoreConst;
import com.nbclass.mapper.ThemeMapper;
import com.nbclass.model.BlogTheme;
import com.nbclass.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThemeServiceImpl implements ThemeService {

    @Autowired
    private ThemeMapper themeMapper;

    @Override
    public int useTheme(Integer id) {
        return themeMapper.updateStatusById(id, CoreConst.STATUS_VALID);
    }

    @Override
    public BlogTheme selectCurrent() {
        BlogTheme bizTheme = new BlogTheme();
        bizTheme.setStatus(CoreConst.STATUS_VALID);
        return themeMapper.selectOne(bizTheme);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return themeMapper.deleteBatch(ids);
    }
}
