package com.nbclass.service.impl;

import com.github.pagehelper.PageHelper;
import com.nbclass.enums.CacheKeyPrefix;
import com.nbclass.enums.ConfigKey;
import com.nbclass.framework.util.CoreConst;
import com.nbclass.framework.util.ResponseUtil;
import com.nbclass.framework.util.UUIDUtil;
import com.nbclass.mapper.ArticleMapper;
import com.nbclass.mapper.ArticleTagMapper;
import com.nbclass.mapper.TagMapper;
import com.nbclass.model.BlogArticle;
import com.nbclass.model.BlogArticleTag;
import com.nbclass.model.BlogTag;
import com.nbclass.service.ArticleService;
import com.nbclass.service.ConfigService;
import com.nbclass.service.RedisService;
import com.nbclass.vo.ArticleVo;
import com.nbclass.vo.ResponseVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * ArticleServiceImpl
 *
 * @author nbclass
 * @version V1.0
 * @date 2019-10-21
 */
@Service
public class ArticleServiceImpl implements ArticleService
{
    @Autowired
    private RedisService redisService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public List<BlogArticle> selectList(ArticleVo vo) {
        List<BlogArticle> list = articleMapper.selectList(vo);
        if (!CollectionUtils.isEmpty(list)) {
            List<Integer> ids = new ArrayList<>();
            for (BlogArticle article : list) {
                ids.add(article.getId());
            }
            List<BlogArticle> tagArticles = articleMapper.selectTagsByArticleId(ids);
            Map<Integer, BlogArticle> tagMap = new LinkedHashMap<>(tagArticles.size());
            for (BlogArticle article : tagArticles) {
                tagMap.put(article.getId(), article);
            }

            for (BlogArticle article : list) {
                BlogArticle tagArticle = tagMap.get(article.getId());
                if(null!=tagArticle){
                    article.setTagList(tagArticle.getTagList());
                }
            }
        }
        return list;
    }

    @Override
    public List<BlogArticle> recommendedList(int pageSize) {
        ArticleVo vo = new ArticleVo();
        vo.withIsRec(CoreConst.IS_YES)
            .withStatus(CoreConst.STATUS_VALID)
            .withIsPublic(CoreConst.IS_YES)
            .setPageSize(pageSize);
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return this.selectList(vo);
    }

    @Override
    public List<BlogArticle> recentList(int pageSize) {
        ArticleVo vo = new ArticleVo();
        vo.withIsRecent(CoreConst.IS_YES)
                .withStatus(CoreConst.STATUS_VALID)
                .withIsPublic(CoreConst.IS_YES)
                .setPageSize(pageSize);
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return this.selectList(vo);
    }

    @Override
    public List<BlogArticle> randomList(int pageSize) {
        ArticleVo vo = new ArticleVo();
        vo.withIsRandom(CoreConst.IS_YES)
                .withStatus(CoreConst.STATUS_VALID)
                .withIsPublic(CoreConst.IS_YES)
                .setPageSize(pageSize);
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return this.selectList(vo);
    }

    @Override
    public List<BlogArticle> hotList(int pageSize) {
        ArticleVo vo = new ArticleVo();
        vo.withIsHot(CoreConst.IS_YES)
                .withStatus(CoreConst.STATUS_VALID)
                .withIsPublic(CoreConst.IS_YES)
                .setPageSize(pageSize);
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return this.selectList(vo);
    }

    @Override
    public BlogArticle selectByAliasName(String aliasName) {
        return articleMapper.selectByAliasName(aliasName);
    }

    @Override
    public Map<String, Object> siteInfoStatistics() {
        return articleMapper.siteInfoStatistics();
    }

    @Override
    public ResponseVo articleLook(Integer articleId, String ip) {
        String cacheKey = CacheKeyPrefix.ARTICLE_LOOK.getPrefix()+articleId+"_"+ip;
        Object obj = redisService.get(cacheKey);
        if(obj==null){
            Map<String,Object> map = new HashMap<>();
            map.put("articleId", articleId);
            map.put("lookNum", true);
            articleMapper.updateNum(map);
            //1小时过期
            redisService.set(cacheKey, true, 1, TimeUnit.HOURS);
        }
        return ResponseUtil.success();
    }

    @Override
    public ResponseVo articleLove(Integer articleId, String ip) {
        String cacheKey = CacheKeyPrefix.ARTICLE_LOVE.getPrefix()+articleId+"_"+ip;
        Object obj = redisService.get(cacheKey);
        if(obj==null){
            Map<String,Object> map = new HashMap<>();
            map.put("articleId", articleId);
            map.put("supportNum", true);
            articleMapper.updateNum(map);
            //1小时过期
            redisService.set(cacheKey, true, 1, TimeUnit.HOURS);
        }else{
            return ResponseUtil.error("您已经点过赞了~");
        }
        return ResponseUtil.success();
    }

    @Override
    public ResponseVo save(BlogArticle article) {
        Date date = new Date();
        article.setUpdateTime(date);
        article.setAliasName(article.getAliasName().trim());
        if(StringUtils.isEmpty(article.getAliasName())){
            article.setAliasName(UUIDUtil.generateShortUuid());
        }
        if(article.getId()==null){
            article.setEditorType(Integer.valueOf(configService.selectAll().get(ConfigKey.EDITOR_TYPE.getValue())));
            article.setCreateTime(date);
            articleMapper.insertSelective(article);
        }else{
            articleMapper.updateByPrimaryKeySelective(article);
            articleTagMapper.deleteByArticleId(article.getId());
        }
        //处理tag
        List<BlogArticleTag> articleTags = new ArrayList<>();
        for(String tagName : article.getTags().split(",")){
            BlogTag tag = tagMapper.selectByName(tagName);
            if(tag==null){
                tag = new BlogTag();
                tag.setName(tagName);
                tag.setStatus(CoreConst.STATUS_VALID);
                tag.setCreateTime(new Date());
                tagMapper.insertSelective(tag);
            }
            BlogArticleTag articleTag = new BlogArticleTag();
            articleTag.setArticleId(article.getId());
            articleTag.setTagId(tag.getId());
            articleTag.setStatus(CoreConst.STATUS_VALID);
            articleTags.add(articleTag);
        }
        if(!CollectionUtils.isEmpty(articleTags)){
            articleTagMapper.insertBatch(articleTags);
        }
        return ResponseUtil.success("保存文章成功");
    }

    @Override
    public void deleteByIds(Integer[] ids) {
        articleMapper.deleteBatch(ids);
    }


}
