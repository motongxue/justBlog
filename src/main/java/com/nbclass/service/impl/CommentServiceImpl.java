package com.nbclass.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nbclass.enums.CacheKeyPrefix;
import com.nbclass.framework.jwt.JwtUtil;
import com.nbclass.framework.jwt.UserInfo;
import com.nbclass.framework.util.*;
import com.nbclass.mapper.CommentMapper;
import com.nbclass.model.BlogComment;
import com.nbclass.service.CommentService;
import com.nbclass.service.RedisService;
import com.nbclass.vo.CommentVo;
import com.nbclass.vo.ResponseVo;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * CommentServiceImpl
 *
 * @author nbclass
 * @version V1.0
 * @date 2019-10-22
 */
@Service
public class CommentServiceImpl implements CommentService {
    private static Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedisService redisService;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<BlogComment> selectList(Integer status) {
        return commentMapper.selectList(status);
    }

    @Override
    public ResponseVo selectBySid(CommentVo vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<BlogComment> comments = commentMapper.selectBySid(vo.getSid());
        //java处理nodes
        List<Integer> mids = new ArrayList<>();
        for(BlogComment comment : comments){
            mids.add(comment.getId());
        }
        if(!CollectionUtils.isEmpty(mids)){
            List<BlogComment> nodeComments = commentMapper.selectByMids(mids);
            if(nodeComments.size()>0){
                for(BlogComment comment : comments){
                    List<BlogComment> nodes = new ArrayList<>();
                    for(BlogComment nodeComment : nodeComments){
                        if(nodeComment.getMid().equals(comment.getId())){
                            nodes.add(nodeComment);
                        }
                    }
                    comment.setNodes(nodes);
                }
            }
        }
        PageInfo<BlogComment> pageInfo = new PageInfo<>(comments);
        return ResponseUtil.success("success", pageInfo);
    }

    @Override
    public ResponseVo commentLove(Integer commentId, String ip) {
        String cacheKey = CacheKeyPrefix.COMMENT_LOVE.getPrefix()+commentId+"_"+ip;
        Object obj = redisService.get(cacheKey);
        if(obj==null){
            Map<String,Object> map = new HashMap<>();
            map.put("commentId", commentId);
            map.put("supportNum", true);
            commentMapper.updateNum(map);
            //1小时过期
            redisService.set(cacheKey, true, 1, TimeUnit.HOURS);
        }else{
            return ResponseUtil.error("您已经点过赞了~");
        }
        return ResponseUtil.success();
    }

    @Override
    public ResponseVo save(BlogComment comment){
        try{
            if(null==comment.getMid()){
                String cacheKey = CacheKeyPrefix.COMMENT_FLOOR.getPrefix()+comment.getSid();
                Integer floor = redisService.get(cacheKey);
                if(floor==null){
                    floor = commentMapper.selectMaxFloorBySid(comment.getSid());
                    if (floor==null){
                        floor=0;
                    }
                }
                comment.setFloor(floor+1);
                redisService.set(cacheKey, comment.getFloor());
            }
            if(StringUtils.isNotBlank(comment.getQq())&&StringUtils.isEmpty(comment.getAvatar())){
                comment.setAvatar("https://q1.qlogo.cn/g?b=qq&nk="+comment.getQq()+"&s=100");
            }
            comment.setCreateTime(new Date());
            commentMapper.insertSelective(comment);
            return ResponseUtil.success("评论提交成功,系统正在审核",comment);
        }catch (Exception e){
            logger.error("system error:{}",e);
            return ResponseUtil.error();
        }

    }

    @Override
    public void audit(Integer[] ids) {
        commentMapper.auditBatch(ids);
    }

    @Override
    public void adminReply(Integer id, String replyContent) {
        if(StringUtils.isNotBlank(replyContent)){
            BlogComment comment = new BlogComment();
            comment.setId(id);
            comment = commentMapper.selectByPrimaryKey(comment);
            if(comment!=null){
                BlogComment reply = new BlogComment();
                reply.setSid(comment.getSid());
                reply.setMid(comment.getMid()==null?comment.getId():comment.getMid());
                reply.setParentId(comment.getId());
                reply.setParentNickname(comment.getNickname());
                HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
                reply.setIp(IpUtil.getIpAddr(request));
                UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
                reply.setBrowser(userAgent.getBrowser().getName());
                reply.setOs(userAgent.getOperatingSystem().getName());
                UserInfo userInfo = jwtUtil.getUserInfo();
                if(userInfo!=null){
                    CopyUtil.copy(userInfo,reply);
                }
                reply.setContent(replyContent);
                reply.setStatus(CoreConst.STATUS_VALID);
                reply.setCreateTime(new Date());
                reply.setId(null);
                commentMapper.insertSelective(reply);
            }
        }
    }

    @Override
    public void deleteBatch(Integer[] ids) {
        commentMapper.deleteBatch(ids);
    }
}
