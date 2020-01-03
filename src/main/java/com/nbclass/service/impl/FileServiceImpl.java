package com.nbclass.service.impl;

import com.nbclass.framework.util.CoreConst;
import com.nbclass.mapper.FileMapper;
import com.nbclass.model.BlogFile;
import com.nbclass.service.FileService;
import com.nbclass.vo.FileVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileMapper fileMapper;

    @Override
    public List<BlogFile> selectList(FileVo vo) {
        return fileMapper.selectList(vo);
    }

    @Override
    public void save(BlogFile file) {
        Date date = new Date();
        file.setUpdateTime(date);
        if(file.getId()==null){
            file.withCreateTime(date)
                .withStatus(CoreConst.STATUS_VALID);
            fileMapper.insertSelective(file);
        }else{
            fileMapper.updateByPrimaryKeySelective(file);
        }
    }

    @Override
    public void deleteBatch(Integer[] ids) {
        fileMapper.deleteBatch(ids);
    }

    @Override
    public List<BlogFile> selectByIds(Integer[] ids) {
        return fileMapper.selectByIds(ids);
    }
}
