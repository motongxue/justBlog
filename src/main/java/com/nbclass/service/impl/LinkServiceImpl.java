package com.nbclass.service.impl;

import com.nbclass.mapper.LinkMapper;
import com.nbclass.model.BlogLink;
import com.nbclass.service.LinkService;
import com.nbclass.vo.LinkVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LinkServiceImpl implements LinkService {

    @Autowired
    private LinkMapper linkMapper;


    @Override
    public List<BlogLink> selectList(LinkVo linkVo) {
        return linkMapper.selectList(linkVo);
    }

    @Override
    public List<BlogLink> selectAll() {
        LinkVo link = new LinkVo();
        link.setStatus(true);
        return linkMapper.selectList(link);
    }

    @Override
    public void save(BlogLink blogLink) {
        Date date = new Date();
        blogLink.setUpdateTime(date);
        if(blogLink.getId()==null){
            blogLink.setCreateTime(date);
            linkMapper.insertSelective(blogLink);
        }else{
            linkMapper.updateByPrimaryKeySelective(blogLink);
        }
    }

    @Override
    public void deleteBatch(Integer[] ids) {
        linkMapper.deleteBatch(ids);
    }
}
