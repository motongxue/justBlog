package com.nbclass.service;

import com.github.pagehelper.PageHelper;
import com.nbclass.enums.SliderType;
import com.nbclass.model.BlogCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 权限模块，thymeleaf调用
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

    public Object get(String moduleName) {
        switch (moduleName) {
            case "categoryList":        //分类
                return categoryService.selectList(new BlogCategory());
            case "tagList":             //标签
                return tagService.selectAll();
            case "notifyList":          //轮播文章
                return sliderService.selectByType(SliderType.NOTIFY.getType());
            case "sliderList":          //轮播文章
                return sliderService.selectByType(SliderType.SLIDER.getType());
           /*

            case "recentList":          //最近文章
                return bizArticleService.recentList(CoreConst.PAGE_SIZE);
            case "recommendedList":    //推荐文章
                return bizArticleService.recommendedList(CoreConst.PAGE_SIZE);
            case "hotList":             //热门文章
                return bizArticleService.hotList(CoreConst.PAGE_SIZE);
            case "linkList":            //友链
                return bizLinkService.selectByStatus(CoreConst.STATUS_VALID);
            case "siteInfo":            //网站信息统计
                return siteInfoService.getSiteInfo();
            case "sysConfig":           //网站基本信息配置
                return sysConfigService.selectAll();
            case "notifies":             //热门文章
                return notifyService.selectAll();
            case "latestComments":      //最新评论
                PageHelper.startPage(1,10);
                CommentConditionVo commentConditionVo = new CommentConditionVo();
                commentConditionVo.setStatus(1);
                return commentService.selectComments(commentConditionVo);*/

            default:
                return null;
        }
    }
}
