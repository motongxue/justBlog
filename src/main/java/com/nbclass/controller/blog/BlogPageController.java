package com.nbclass.controller.blog;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nbclass.enums.CategoryType;
import com.nbclass.enums.SliderType;
import com.nbclass.framework.exception.ResourceNotFoundException;
import com.nbclass.framework.util.CoreConst;
import com.nbclass.framework.util.IpUtil;
import com.nbclass.model.BlogArticle;
import com.nbclass.model.BlogCategory;
import com.nbclass.service.ArticleService;
import com.nbclass.service.CategoryService;
import com.nbclass.service.SliderService;
import com.nbclass.vo.ArticleVo;
import com.nbclass.vo.PageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 前端页面控制器
 *
 * @version V1.0
 * @date 2019/10/10
 * @author nbclass
 */
@Controller
public class BlogPageController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private SliderService sliderService;

    /**
     * 首页入口
     */
    @GetMapping("/")
    public String index(Model model) {
        ArticleVo vo = new ArticleVo();
        loadArticle(model, vo, "page");
        model.addAttribute("notifyList",sliderService.selectByType(SliderType.NOTIFY.getType()));
        model.addAttribute("sliderList",sliderService.selectByType(SliderType.SLIDER.getType()));
        return String.format("theme/%s/%s", CoreConst.currentTheme, "index");
    }

    @GetMapping("/page/{pageNum}")
    public String index(@PathVariable("pageNum") Integer pageNum, Model model) {
        ArticleVo vo = new ArticleVo();
        vo.setPageNum(pageNum);
        loadArticle(model, vo, "page");
        return String.format("theme/%s/%s", CoreConst.currentTheme, "index");
    }

    @GetMapping("/{category}")
    public String category(@PathVariable("category") String category, Model model){
        BlogCategory blogCategory = categoryService.selectByAlias(category);
        if(blogCategory==null){
            throw new ResourceNotFoundException();
        }
        if(blogCategory.getType().equals(CategoryType.CATEGORY_PAGE.getType())){
            ArticleVo vo = new ArticleVo();
            vo.setCategory(category);
            loadArticle(model, vo, category + "/page");
        }
        model.addAttribute("sid",-blogCategory.getId());
        return String.format("theme/%s/%s", CoreConst.currentTheme, blogCategory.getTemplate());
    }

    @GetMapping("/{category}/page/{pageNum}")
    public String category(@PathVariable("category") String category, @PathVariable("pageNum") Integer pageNum, Model model){
        BlogCategory blogCategory = categoryService.selectByAlias(category);
        if(blogCategory==null || !blogCategory.getType().equals(CategoryType.CATEGORY_PAGE.getType())){
            throw new ResourceNotFoundException();
        }
        ArticleVo vo = new ArticleVo();
        vo.setPageNum(pageNum);
        vo.setCategory(category);
        loadArticle(model, vo, category + "/page");
        return String.format("theme/%s/%s", CoreConst.currentTheme,  blogCategory.getTemplate());
    }

    @GetMapping("/tag/{tagId}")
    public String tag(Model model,@PathVariable("tagId") Integer tagId){
        ArticleVo vo = new ArticleVo();
        vo.setTagId(tagId);
        loadArticle(model, vo, "tag/" + tagId + "/page");
        return String.format("theme/%s/%s", CoreConst.currentTheme, "index");
    }

    @GetMapping("/tag/{tagId}/page/{pageNum}")
    public String tag(@PathVariable("tagId") Integer tagId, @PathVariable("pageNum") Integer pageNum, Model model) {
        ArticleVo vo = new ArticleVo();
        vo.setTagId(tagId);
        vo.setPageNum(pageNum);
        loadArticle(model, vo, "tag/" + tagId +"/page");
        return String.format("theme/%s/%s", CoreConst.currentTheme, "index");
    }


    /**
     * 文章详情
     *
     * @param model
     * @param alias
     * @return
     */
    @GetMapping("/a/{alias}")
    public String article(HttpServletRequest request, Model model, @PathVariable("alias") String alias) {
        BlogArticle article = articleService.selectByAliasName(alias);
        if (article == null || CoreConst.STATUS_INVALID.equals(article.getStatus()) ) {
            throw new ResourceNotFoundException();
        }
        articleService.articleLook(article.getId(), IpUtil.getIpAddr(request));
        model.addAttribute("article", article);
        return String.format("theme/%s/%s", CoreConst.currentTheme,  article.getTemplate());
    }

    private void loadArticle(Model model, ArticleVo vo, String pageUrl) {
        vo.setStatus(CoreConst.STATUS_VALID);
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<BlogArticle> articleList =  articleService.selectList(vo);
        PageInfo<BlogArticle> pageInfo = new PageInfo<>(articleList);
        PageVo pageVo = new PageVo();
        BeanUtils.copyProperties(pageInfo,pageVo);
        model.addAttribute("page", pageVo);
        model.addAttribute("pageUrl", pageUrl);
        model.addAttribute("articleList",articleList);
    }


}
