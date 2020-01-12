package com.nbclass.mapper;

import com.nbclass.framework.util.MyMapper;
import com.nbclass.model.BlogCategory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CategoryMapper
 *
 * @version V1.0
 * @date 2019/10/10
 * @author nbclass
 */
@Repository
public interface CategoryMapper extends MyMapper<BlogCategory> {
    /**
     * 根据参数查询栏目列表
     * @param type 状态
     * @return list
     */
    List<BlogCategory> selectByType(@Param("type") Integer type);


    /**
     * 删除
     * @param id id
     */
    int deleteById(Integer id);

    /**
     * 根据ids查询栏目列表
     * @param ids ids
     * @return list
     */
    List<BlogCategory> selectByIds(List<Integer> ids);

    /**
     * 根据id查询栏目
     * @param id id
     * @return BlogCategory
     */
    BlogCategory selectById(Integer id);
}
