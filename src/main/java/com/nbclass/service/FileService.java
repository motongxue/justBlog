package com.nbclass.service;

import com.nbclass.model.BlogFile;
import com.nbclass.model.BlogLink;
import com.nbclass.vo.FileVo;

import java.util.List;

/**
 * FileService
 *
 * @author nbclass
 * @version V1.0
 * @date 2020-01-03
 */
public interface FileService {


    List<BlogFile> selectList(FileVo vo);

    void save(BlogFile file);

    void deleteBatch(Integer[] ids);

    List<BlogFile> selectByIds(Integer[] ids);

}
