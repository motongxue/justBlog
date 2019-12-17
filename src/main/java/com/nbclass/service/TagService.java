package com.nbclass.service;

import com.nbclass.model.BlogTag;
import com.nbclass.vo.ResponseVo;

import java.util.List;

/**
 * TagService
 *
 * @author nbclass
 * @version V1.0
 * @date 2019-10-18
 */
public interface TagService {

    List<BlogTag> selectAll();

    List<BlogTag> selectList(String name);

    ResponseVo save(BlogTag tag);

    void deleteBatch(Integer[] ids);

}
