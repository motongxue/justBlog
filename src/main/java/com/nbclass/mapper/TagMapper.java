package com.nbclass.mapper;

import com.nbclass.framework.util.MyMapper;
import com.nbclass.model.BlogSlider;
import com.nbclass.model.BlogTag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * LinkMapper
 *
 * @version V1.0
 * @date 2019/10/10
 * @author nbclass
 */
@Repository
public interface TagMapper extends MyMapper<BlogTag> {


    BlogTag selectByName(String name);

    List<BlogTag> selectList(@Param("name") String name);

    int deleteBatch(Integer[] ids);
}
