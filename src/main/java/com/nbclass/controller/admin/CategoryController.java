package com.nbclass.controller.admin;

import com.nbclass.framework.annotation.AccessToken;
import com.nbclass.framework.util.ResponseUtil;
import com.nbclass.model.BlogCategory;
import com.nbclass.service.CategoryService;
import com.nbclass.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/list")
    @AccessToken
    public ResponseVo list(Integer type, boolean disabled){
        List<BlogCategory> categories = categoryService.selectAll(type, disabled);
        return ResponseUtil.success(categories);
    }

    @PostMapping("/save")
    @AccessToken
    public ResponseVo add(BlogCategory category){
        categoryService.save(category);
        return ResponseUtil.success("保存分类成功");
    }

    @PostMapping("/delete")
    @AccessToken
    public ResponseVo delete(Integer id){
        categoryService.deleteById(id);
        return ResponseUtil.success("删除分类成功");
    }
}
