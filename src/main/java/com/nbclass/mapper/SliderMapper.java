package com.nbclass.mapper;

import com.nbclass.framework.util.MyMapper;
import com.nbclass.model.BlogLink;
import com.nbclass.model.BlogSlider;
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
public interface SliderMapper extends MyMapper<BlogSlider> {
    /**
     * 根据type查询轮播/公告列表
     * @param type
     * @param title
     * @return list
     */
    List<BlogSlider> selectList(@Param("type") Integer type, @Param("title") String title);


    /**
     * 根据ids删除轮播
     * @param ids id集合
     * @return int
     */
    int deleteBatch(Integer[] ids);
}
