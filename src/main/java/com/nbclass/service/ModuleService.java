package com.nbclass.service;

import com.github.pagehelper.PageHelper;
import com.nbclass.enums.SliderType;
import com.nbclass.framework.exception.ResourceNotFoundException;
import com.nbclass.framework.util.CoreConst;
import com.nbclass.model.BlogArticle;
import com.nbclass.model.BlogCategory;
import com.nbclass.model.BlogComment;
import com.nbclass.model.BlogTag;
import com.nbclass.vo.ArticleVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限模块，thymeleaf调用,部分方法可传pageSize
 *
 * @author nbclass
 * @version V1.0
 * @date 2019-10-12
 */
@Component("module")
public class ModuleService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private SliderService sliderService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private LinkService linkService;
    @Autowired
    private CommentService commentService;

    public Object get(String moduleName) {
        switch (moduleName) {
            default:
                String functionName="";
                String param="";
                if(moduleName.contains("(")){
                    functionName = moduleName.substring(0,moduleName.indexOf("("));
                    param = moduleName.substring(moduleName.indexOf("(")+1,moduleName.indexOf(")"));
                }else{
                    functionName=moduleName;
                }
                switch (functionName){
                    case "categoryList":        //分类
                        return categoryService.selectAll();
                    case "tagList":             //标签
                        return tagService.selectAll();
                    case "linkList":            //友链
                        return linkService.selectAll();
                    case "notifyList":          //系统公告
                        return sliderService.selectAll().stream().filter(item->item.getType().equals(SliderType.NOTIFY.getType())).collect(Collectors.toList());
                    case "sliderList":          //轮播列表
                        return sliderService.selectAll().stream().filter(item->item.getType().equals(SliderType.SLIDER.getType())).collect(Collectors.toList());
                    case "recentList":          //最近文章
                        return articleService.recentList(CoreConst.PAGE_SIZE);
                    case "recommendedList":    //推荐文章
                        return articleService.recommendedList(CoreConst.PAGE_SIZE);
                    case "hotList":             //热门文章
                        return articleService.hotList(CoreConst.PAGE_SIZE);
                    case "randomList":         //随机文章
                        return articleService.randomList(CoreConst.PAGE_SIZE);
                    case "siteInfo":            //网站信息统计
                        return articleService.siteInfoStatistics();
                    case "config":           //网站设置
                        return configService.selectAll();
                    case "latestComments":
                        PageHelper.startPage(CoreConst.PAGE_NUM, StringUtils.isNotBlank(param)?new Integer(param):CoreConst.PAGE_SIZE);
                        return commentService.selectList(CoreConst.STATUS_VALID);
                    case "categoryArticles":
                        String params[] = param.split(",");
                        BlogCategory category = categoryService.selectByAlias(params[0]);
                        if(category!=null){
                            PageHelper.startPage(CoreConst.PAGE_NUM, params.length>1?new Integer(params[1]):CoreConst.PAGE_SIZE);
                            ArticleVo vo = new ArticleVo();
                            vo.setCategory(params[0]);
                            vo.setStatus(CoreConst.STATUS_VALID);
                            return articleService.selectList(vo);
                        }else{
                            return null;
                        }
                    default:
                        return null;
                }
        }
    }




}
