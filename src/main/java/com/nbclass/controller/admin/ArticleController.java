package com.nbclass.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nbclass.framework.annotation.AccessToken;
import com.nbclass.framework.util.ResponseUtil;
import com.nbclass.model.BlogArticle;
import com.nbclass.service.ArticleService;
import com.nbclass.vo.ArticleVo;
import com.nbclass.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping("/list")
    @AccessToken
    public ResponseVo list(ArticleVo vo){
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<BlogArticle> blogArticles = articleService.selectList(vo);
        PageInfo<BlogArticle> pageInfo = new PageInfo<>(blogArticles);
        return ResponseUtil.success(pageInfo);
    }

    @PostMapping("/save")
    @AccessToken
    public ResponseVo add(BlogArticle article){
        return articleService.save(article);
    }

    @PostMapping("/top")
    @AccessToken
    public ResponseVo top(@RequestParam("id") Integer id, @RequestParam("isTop") Integer isTop){
        BlogArticle article = new BlogArticle();
        article.setId(id);
        article.setIsTop(isTop);
        articleService.update(article);
        return ResponseUtil.success(isTop==1?"置顶成功":"取消置顶成功");
    }

    @PostMapping("/rec")
    @AccessToken
    public ResponseVo rec(@RequestParam("id") Integer id, @RequestParam("isRec") Integer isRec){
        BlogArticle article = new BlogArticle();
        article.setId(id);
        article.setIsRec(isRec);
        articleService.update(article);
        return ResponseUtil.success(isRec==1?"推荐成功":"取消推荐成功");
    }

    @PostMapping("/delete")
    @AccessToken
    public ResponseVo delete(@RequestParam("ids[]") Integer[]ids){
        articleService.deleteByIds(ids);
        return ResponseUtil.success("删除文章成功");
    }

    @PostMapping("/exist/aliasName")
    @AccessToken
    public boolean isExistAliasName(Integer id, String aliasName){
        BlogArticle article = articleService.selectByAliasName(aliasName);
        return article!=null && (id==null || !id.equals(article.getId()));
    }

}
