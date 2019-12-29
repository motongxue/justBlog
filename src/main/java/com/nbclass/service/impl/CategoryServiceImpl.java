package com.nbclass.service.impl;

import com.nbclass.enums.CategoryType;
import com.nbclass.framework.util.UUIDUtil;
import com.nbclass.mapper.CategoryMapper;
import com.nbclass.model.BlogCategory;
import com.nbclass.service.CategoryService;
import org.apache.commons.lang.StringUtils;
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
    public List<BlogCategory> selectAll(Integer type, boolean disabled) {
        List<BlogCategory> categories = categoryMapper.selectByType(type);
        return listToTree(categories, disabled);
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
        if(!category.getType().equals(CategoryType.CATEGORY.getType()) && StringUtils.isEmpty(category.getAliasName())){
            category.setAliasName(UUIDUtil.generateShortUuid());
        }
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


    private static List<BlogCategory> listToTree(List<BlogCategory> list, boolean disabled) {
        //用递归找子。
        List<BlogCategory> treeList = new ArrayList<>();
        for (BlogCategory tree : list) {
            tree.setOpen(true);
            if(disabled && (tree.getType()==0||tree.getType()==2)){
                tree.setDisabled(true);
            }
            if (tree.getPid()==null||tree.getPid() == 0) {
                treeList.add(findChildren(tree, list));
            }
        }
        return treeList;
    }

    private static BlogCategory findChildren(BlogCategory tree, List<BlogCategory> list) {
        for (BlogCategory node : list) {
            if (node.getPid()!=null && node.getPid().equals(tree.getId())) {
                if (tree.getChildren() == null) {
                    tree.setChildren(new ArrayList<>());
                }
                tree.getChildren().add(findChildren(node, list));
            }
        }
        return tree;
    }

}
