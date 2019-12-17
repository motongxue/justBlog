package com.nbclass.service;

import com.nbclass.model.BlogArticle;
import com.nbclass.vo.ArticleVo;
import com.nbclass.vo.ResponseVo;

import java.util.List;
import java.util.Map;

/**
 * ArticleService
 *
 * @author nbclass
 * @version V1.0
 * @date 2019-10-21
 */
public interface ArticleService {

    /**
     * 分页查询
     * @param vo 查询参数vo
     * @return list
     */
    List<BlogArticle> selectList(ArticleVo vo);

    /**
     * 站长推荐
     *
     * @param pageSize 页数
     * @return list
     */
    List<BlogArticle> recommendedList(int pageSize);

    /**
     * 最近文章
     *
     * @param pageSize 页数
     * @return list
     */

    List<BlogArticle> recentList(int pageSize);

    /**
     * 随机文章
     *
     * @param pageSize 页数
     * @return list
     */
    List<BlogArticle> randomList(int pageSize);
    /**
     * 热门文章
     * @param pageSize 页数
     * @return list
     */
    List<BlogArticle> hotList(int pageSize);

    /**
     * 根据别名获取文章
     * @param aliasName 别名
     * @return article
     */
    BlogArticle selectByAliasName(String aliasName);


    /**
     * 根据id获取文章
     * @param id id
     * @return article
     */
    BlogArticle selectById(Integer id);

    /**
     * 网站信息统计
     * @return map
     */
    Map<String, Object> siteInfoStatistics();

    /**
     * 文章查看
     * @return ResponseVo
     */
    ResponseVo articleLook(Integer articleId, String ip);

    /**
     * 文章点赞
     * @return ResponseVo
     */
    ResponseVo articleLove(Integer articleId, String ip);

    /**
     * 保存文章
     */
    ResponseVo save(BlogArticle article);

    /**
     * 删除文章
     */
    void deleteByIds(Integer[] ids);

    /**
     * 根据分类id查询文章id集合
     * @param categoryId 分类id
     * @return list
     */
    List<Integer> selectArticleIdsByCategoryId(Integer categoryId);

    /**
     * 根据标签id集合查询文章id集合
     * @param tagIds 标签ids
     * @return list
     */
    List<Integer> selectArticleIdsByTagIds(Integer [] tagIds);

}
