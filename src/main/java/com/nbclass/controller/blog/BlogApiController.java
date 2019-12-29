package com.nbclass.controller.blog;

import com.nbclass.framework.util.*;
import com.nbclass.model.BlogComment;
import com.nbclass.service.ArticleService;
import com.nbclass.service.CommentService;
import com.nbclass.vo.CommentVo;
import com.nbclass.vo.ResponseVo;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 博客接口控制器
 *
 * @author nbclass
 * @version V1.0
 * @date 2019-10-22
 */
@RestController
@RequestMapping("api")
public class BlogApiController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleService articleService;

    @PostMapping("/comments")
    public ResponseVo selectComments(CommentVo vo){
        return commentService.selectBySid(vo);
    }

    @PostMapping("article/look")
    public ResponseVo articleLook(HttpServletRequest request, @RequestParam Integer articleId){
        synchronized (IpUtil.getIpAddr(request)) {
            return articleService.articleLook(articleId, IpUtil.getIpAddr(request));
        }
    }

    @PostMapping("article/love")
    public ResponseVo articleLove(HttpServletRequest request, @RequestParam Integer articleId){
        synchronized (IpUtil.getIpAddr(request)) {
            return articleService.articleLove(articleId, IpUtil.getIpAddr(request));
        }
    }

    @PostMapping("comment/love")
    public ResponseVo commentLove(HttpServletRequest request, @RequestParam Integer commentId){
        synchronized (IpUtil.getIpAddr(request)) {
            return commentService.commentLove(commentId, IpUtil.getIpAddr(request));
        }
    }

    @PostMapping("comment/save")
    public ResponseVo commentSave(HttpServletRequest request, BlogComment comment){
        synchronized (IpUtil.getIpAddr(request)){
            if (StringUtils.isEmpty(comment.getNickname())) {
                return ResponseUtil.error("请输入昵称");
            }
            String content = comment.getContent();
            if (!XssKillerUtil.isValid(content) || ValidatorUtil.hasEmoji(content)) {
                return ResponseUtil.error("内容不合法");
            }
            comment.setContent(ValidatorUtil.crlf(comment.getContent()));
            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
            comment.setBrowser(userAgent.getBrowser().getName());
            comment.setOs(userAgent.getOperatingSystem().getName());
            comment.setIp(IpUtil.getIpAddr(request));
            return commentService.save(comment);
        }
    }

    @PostMapping("qq/{id}")
    public ResponseVo qqInfo(@PathVariable("id") Long id){
        try{
            return ResponseUtil.success(QQUtil.getQqInfo(id));
        }catch (Exception e){
            return ResponseUtil.error("获取QQ号失败");
        }
    }



}
