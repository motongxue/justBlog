package com.nbclass.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nbclass.framework.annotation.AccessToken;
import com.nbclass.framework.util.ResponseUtil;
import com.nbclass.model.BlogLink;
import com.nbclass.model.BlogSlider;
import com.nbclass.service.LinkService;
import com.nbclass.service.SliderService;
import com.nbclass.vo.LinkVo;
import com.nbclass.vo.ResponseVo;
import com.nbclass.vo.SliderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/slider")
public class SliderController {
    @Autowired
    private SliderService sliderService;

    @PostMapping("/list")
    @AccessToken
    public ResponseVo loadLinks(SliderVo vo){
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<BlogSlider> sliders = sliderService.selectList(vo.getType(),vo.getTitle());
        PageInfo<BlogSlider> pageInfo = new PageInfo<>(sliders);
        return ResponseUtil.success(pageInfo);
    }

    @PostMapping("/save")
    @AccessToken
    public ResponseVo add(BlogSlider slider){
        sliderService.save(slider);
        return ResponseUtil.success("保存成功");
    }

    @PostMapping("/delete")
    @AccessToken
    public ResponseVo delete(@RequestParam("ids[]") Integer[]ids){
        sliderService.deleteBatch(ids);
        return ResponseUtil.success("删除成功");
    }

}
