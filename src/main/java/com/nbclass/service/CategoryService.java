package com.nbclass.service;

import com.nbclass.model.BlogCategory;
import com.nbclass.model.BlogLink;
import com.nbclass.vo.CategoryVo;

import java.util.List;

/**
 * CategoryService
 *
 * @author nbclass
 * @version V1.0
 * @date 2019-10-18
 */
public interface CategoryService {

    List<BlogCategory> selectAll(Integer type);

    BlogCategory selectByAlias(String alias);

    void save(BlogCategory category);

    void deleteById(Integer id);
}
