package com.nbclass.mapper;

import com.nbclass.framework.util.MyMapper;
import com.nbclass.model.BlogArticle;
import com.nbclass.model.BlogComment;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CommentMapper
 *
 * @version V1.0
 * @date 2019/10/10
 * @author nbclass
 */
@Repository
public interface CommentMapper extends MyMapper<BlogComment> {
    /**
     * 根据参数查询评论列表
     * @param comment
     * @return list
     */
    List<BlogComment> selectList(BlogComment comment);


}
