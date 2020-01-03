package com.nbclass.mapper;

import com.nbclass.framework.util.MyMapper;
import com.nbclass.model.BlogFile;
import com.nbclass.vo.FileVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FileMapper
 *
 * @version V1.0
 * @date 2020/1/3
 * @author nbclass
 */
@Repository
public interface FileMapper extends MyMapper<BlogFile> {

    /**
     * 根据参数查询文件
     * @param vo 查询参数
     * @return list
     */
    List<BlogFile> selectList(FileVo vo);

    /**
     * 根据ids查询文件
     * @param ids id集合
     * @return list
     */
    List<BlogFile> selectByIds(Integer[] ids);

    /**
     * 根据ids删除文件
     * @param ids id集合
     * @return int
     */
    int deleteBatch(Integer[] ids);
}
