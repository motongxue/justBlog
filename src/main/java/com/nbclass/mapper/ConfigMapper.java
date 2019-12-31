package com.nbclass.mapper;

import com.nbclass.framework.util.MyMapper;
import com.nbclass.model.BlogArticle;
import com.nbclass.model.BlogConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ConfigMapper
 *
 * @version V1.0
 * @date 2019/10/10
 * @author nbclass
 */
@Repository
public interface ConfigMapper extends MyMapper<BlogConfig> {

    /**
     * 根据key跟新
     * @param key
     * @param value
     * @return int
     */
    int updateByKey(@Param("key") String key, @Param("value") String value);

    /**
     * 根据key获取value
     * @param key
     * @return int
     */
    String getByKey(@Param("key") String key);

}
