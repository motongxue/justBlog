package com.nbclass.mapper;

import com.nbclass.framework.util.MyMapper;
import com.nbclass.model.BlogArticle;
import com.nbclass.model.BlogCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * UserMapper
 *
 * @version V1.0
 * @date 2019/10/10
 * @author nbclass
 */
@Repository
public interface ArticleMapper extends MyMapper<BlogArticle> {
    /**
     * 根据参数查询文章列表
     * @param article
     * @return list
     */
    List<BlogArticle> selectList(BlogArticle article);


}
