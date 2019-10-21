package com.nbclass.mapper;

import com.nbclass.framework.util.MyMapper;
import com.nbclass.model.BlogTag;
import com.nbclass.model.BlogTheme;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * LinkMapper
 *
 * @version V1.0
 * @date 2019/10/10
 * @author nbclass
 */
@Repository
public interface ThemeMapper extends MyMapper<BlogTheme> {


    int updateStatusById(@Param("id") Integer id, @Param("status") Integer status);

    int deleteBatch(Integer[] ids);

}
