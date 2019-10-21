package com.nbclass.service.impl;

import com.github.pagehelper.PageHelper;
import com.nbclass.framework.util.CoreConst;
import com.nbclass.mapper.ArticleMapper;
import com.nbclass.model.BlogArticle;
import com.nbclass.service.ArticleService;
import com.nbclass.vo.ArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ArticleServiceImpl
 *
 * @author nbclass
 * @version V1.0
 * @date 2019-10-21
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<BlogArticle> selectList(ArticleVo vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<BlogArticle> list = articleMapper.selectList(vo);
        if (!CollectionUtils.isEmpty(list)) {
            List<Integer> ids = new ArrayList<>();
            for (BlogArticle article : list) {
                ids.add(article.getId());
            }
            List<BlogArticle> tagArticles = articleMapper.selectTagsByArticleId(ids);
            Map<Integer, BlogArticle> tagMap = new LinkedHashMap<>(tagArticles.size());
            for (BlogArticle bizArticle : tagArticles) {
                tagMap.put(bizArticle.getId(), bizArticle);
            }

            for (BlogArticle bizArticle : list) {
                BlogArticle tagArticle = tagMap.get(bizArticle.getId());
                if(null!=tagArticle){
                    bizArticle.setTags(tagArticle.getTags());
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
        return this.selectList(vo);
    }

    @Override
    public List<BlogArticle> recentList(int pageSize) {
        ArticleVo vo = new ArticleVo();
        vo.withIsRecent(CoreConst.IS_YES)
                .withStatus(CoreConst.STATUS_VALID)
                .withIsPublic(CoreConst.IS_YES)
                .setPageSize(pageSize);
        return this.selectList(vo);
    }

    @Override
    public List<BlogArticle> randomList(int pageSize) {
        ArticleVo vo = new ArticleVo();
        vo.withIsRandom(CoreConst.IS_YES)
                .withStatus(CoreConst.STATUS_VALID)
                .withIsPublic(CoreConst.IS_YES)
                .setPageSize(pageSize);
        return this.selectList(vo);
    }

    @Override
    public List<BlogArticle> hotList(int pageSize) {
        ArticleVo vo = new ArticleVo();
        vo.withIsHot(CoreConst.IS_YES)
                .withStatus(CoreConst.STATUS_VALID)
                .withIsPublic(CoreConst.IS_YES)
                .setPageSize(pageSize);
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
}
