package com.nbclass.mapper;

import com.nbclass.framework.util.MyMapper;
import com.nbclass.model.BlogArticle;
import com.nbclass.model.BlogComment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
     * 根据状态查询评论列表
     * @param status 状态
     * @return list
     */
    List<BlogComment> selectList(@Param("status") Integer status);

    /**
     * 根据sid查询评论列表
     * @param sid 评论主体id
     * @return list
     */
    List<BlogComment> selectBySid(Integer sid);

    /**
     * 根据midList查询评论
     * @param midList 主评论id
     * @return list
     */
    List<BlogComment> selectByMids(List<Integer> midList);

    /**
     * 更新评论相关数量
     * @return int
     */
    int updateNum(Map<String,Object> map);

    /**
     * 根据sid查询最大楼层
     * @param sid 评论主体id
     * @return int
     */
    Integer selectMaxFloorBySid(Integer sid);

    /**
     * 根据文章id集合删除评论
     * @param articleIds 文章id集合
     * @return int
     */
    int deleteBatchByArticleIds(Integer[] articleIds);


    /**
     * 批量删除
     *
     * @param ids ids
     */
    int deleteBatch(Integer[] ids);

}
