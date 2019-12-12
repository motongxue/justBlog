package com.nbclass.service.impl;

import com.nbclass.framework.util.CoreConst;
import com.nbclass.mapper.CategoryMapper;
import com.nbclass.model.BlogCategory;
import com.nbclass.service.CategoryService;
import com.nbclass.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CategoryServiceImpl
 *
 * @author nbclass
 * @version V1.0
 * @date 2019-10-18
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<BlogCategory> selectAll() {
        List<BlogCategory> categories = categoryMapper.selectByStatus(CoreConst.STATUS_VALID);
        return toTree(categories);
    }

    @Override
    public BlogCategory selectByAlias(String alias) {
        BlogCategory category = new BlogCategory();
        category.setAliasName(alias);
        return categoryMapper.selectOne(category);
    }

    @Override
    public void save(BlogCategory category) {
        Date date = new Date();
        category.setUpdateTime(date);
        if(category.getId()==null){
            category.setCreateTime(date);
            categoryMapper.insertSelective(category);
        }else{
            categoryMapper.updateByPrimaryKeySelective(category);
        }
    }

    @Override
    public void deleteById(Integer id) {
        categoryMapper.deleteById(id);
    }


    private static List<BlogCategory> toTree(List<BlogCategory> list) {
        List<BlogCategory> treeList = new ArrayList<>();
        for (BlogCategory tree : list) {
            tree.setOpen(true);
            if(tree.getPid()==null||tree.getPid() == 0){
                treeList.add(tree);
            }
        }
        for (BlogCategory tree : list) {
            toTreeChildren(treeList,tree);
        }
        return treeList;
    }

    private static void toTreeChildren(List<BlogCategory> treeList, BlogCategory tree) {
        for (BlogCategory node : treeList) {
            if(tree.getPid()!=null && tree.getPid().equals(node.getId())){
                if(node.getChildren() == null){
                    node.setChildren(new ArrayList<>());
                }
                node.getChildren().add(tree);
            }
            if(node.getChildren() != null){
                toTreeChildren(node.getChildren(),tree);
            }
        }
    }
}
