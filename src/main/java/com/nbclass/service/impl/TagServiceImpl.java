package com.nbclass.service.impl;

import com.nbclass.mapper.TagMapper;
import com.nbclass.model.BlogTag;
import com.nbclass.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * TagServiceImpl
 *
 * @author nbclass
 * @version V1.0
 * @date 2019-10-18
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;


    @Override
    public List<BlogTag> selectAll() {
        return tagMapper.selectList(null);
    }

    @Override
    public List<BlogTag> selectList(String name) {
        return tagMapper.selectList(name);
    }

    @Override
    public void save(BlogTag tag) {
        Date date = new Date();
        tag.setUpdateTime(date);
        if(tag.getId()==null){
            tag.setCreateTime(date);
            tagMapper.insertSelective(tag);
        }else{
            tagMapper.updateByPrimaryKeySelective(tag);
        }
    }

    @Override
    public void deleteBatch(Integer[] ids) {
        tagMapper.deleteBatch(ids);
    }
}
