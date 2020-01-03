package com.nbclass.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nbclass.framework.annotation.AccessToken;
import com.nbclass.framework.oss.OssFactory;
import com.nbclass.framework.util.ResponseUtil;
import com.nbclass.model.BlogFile;
import com.nbclass.model.BlogLink;
import com.nbclass.service.FileService;
import com.nbclass.service.LinkService;
import com.nbclass.vo.FileVo;
import com.nbclass.vo.LinkVo;
import com.nbclass.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/admin/file")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/list")
    @AccessToken
    public ResponseVo loadLinks(FileVo vo){
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<BlogFile> files = fileService.selectList(vo);
        PageInfo<BlogFile> pageInfo = new PageInfo<>(files);
        return ResponseUtil.success(pageInfo);
    }

    @PostMapping("/delete")
    @AccessToken
    public ResponseVo delete(@RequestParam("ids[]") Integer[]ids){
        List<BlogFile> files = fileService.selectByIds(ids);
        fileService.deleteBatch(ids);
        for(BlogFile file : files){
            Objects.requireNonNull(OssFactory.init(file.getOssType())).delete(file.getFilePath());
        }
        return ResponseUtil.success("删除文件成功");
    }

}
