package com.nbclass.service;

import com.nbclass.model.BlogLink;
import com.nbclass.vo.LinkVo;

import java.util.List;

public interface LinkService {

    List<BlogLink> selectList(LinkVo linkVo);

    List<BlogLink> selectAll();

    BlogLink selectById(Integer id);

    void save(BlogLink blogLink);

    void deleteBatch(Integer[] ids);


}
