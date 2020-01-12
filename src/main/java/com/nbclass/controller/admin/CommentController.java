package com.nbclass.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nbclass.framework.annotation.AccessToken;
import com.nbclass.framework.util.ResponseUtil;
import com.nbclass.model.BlogComment;
import com.nbclass.service.CommentService;
import com.nbclass.vo.CommentVo;
import com.nbclass.vo.ResponseVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/list")
    @AccessToken
    public ResponseVo loadComments(CommentVo vo){
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<BlogComment> comments = commentService.selectList(vo.getStatus());
        PageInfo<BlogComment> pageInfo = new PageInfo<>(comments);
        return ResponseUtil.success(pageInfo);
    }

    @PostMapping("/audit")
    @AccessToken
    public ResponseVo audit(Integer id, String replyContent,Integer emailFlag){
        commentService.audit(new Integer[]{id});
        commentService.adminReply(id,replyContent,emailFlag);
        return ResponseUtil.success("审核成功");
    }

    @PostMapping("/audit/batch")
    @AccessToken
    public ResponseVo audit(@RequestParam("ids[]") Integer[]ids){
        commentService.audit(ids);
        return ResponseUtil.success("批量审核成功");
    }

    @PostMapping("/reply")
    @AccessToken
    public ResponseVo reply(Integer id, String replyContent,Integer emailFlag){
        commentService.adminReply(id,replyContent,emailFlag);
        return ResponseUtil.success("回复成功");
    }

    @PostMapping("/delete")
    @AccessToken
    public ResponseVo delete(@RequestParam("ids[]") Integer[]ids){
        commentService.deleteBatch(ids);
        return ResponseUtil.success("删除评论成功");
    }

}
