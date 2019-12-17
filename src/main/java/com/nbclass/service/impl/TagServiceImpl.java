package com.nbclass.service.impl;

import com.nbclass.framework.util.ResponseUtil;
import com.nbclass.mapper.TagMapper;
import com.nbclass.model.BlogTag;
import com.nbclass.service.TagService;
import com.nbclass.vo.ResponseVo;
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
    public ResponseVo save(BlogTag tag) {
        Date date = new Date();
        tag.setUpdateTime(date);
        BlogTag blogTag = tagMapper.selectByName(tag.getName(),tag.getId());
        if(blogTag!=null){
            return ResponseUtil.error(String.format("标签名【%s】已存在！",tag.getName()));
        }
        if(tag.getId()==null){
            tag.setCreateTime(date);
            tagMapper.insertSelective(tag);
        }else{
            tagMapper.updateByPrimaryKeySelective(tag);
        }
        return ResponseUtil.success("保存标签成功");
    }

    @Override
    public void deleteBatch(Integer[] ids) {
        tagMapper.deleteBatch(ids);
    }
}
