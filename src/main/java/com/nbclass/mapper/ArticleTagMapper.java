package com.nbclass.mapper;

import com.nbclass.framework.util.MyMapper;
import com.nbclass.model.BlogArticleTag;
import com.nbclass.model.BlogTag;
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
public interface ArticleTagMapper extends MyMapper<BlogArticleTag> {

    int deleteByArticleId(Integer articleId);

    int insertBatch(List<BlogArticleTag> articleTags);

}
