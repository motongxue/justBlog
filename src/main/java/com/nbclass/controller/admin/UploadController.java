package com.nbclass.controller.admin;

import com.alibaba.druid.support.json.JSONUtils;
import com.nbclass.framework.annotation.AccessToken;
import com.nbclass.framework.exception.ZbException;
import com.nbclass.framework.oss.OssFactory;
import com.nbclass.framework.util.ResponseUtil;
import com.nbclass.model.BlogFile;
import com.nbclass.service.FileService;
import com.nbclass.vo.ResponseVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 上传接口控制器
 *
 * @version V1.0
 * @date 2019/12/13
 * @author nbclass
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    @Autowired
    private FileService fileService;

    @PostMapping(value = "/upload")
    @AccessToken
    public ResponseVo upload(@RequestParam MultipartFile file) throws Exception{
        if (file == null || file.isEmpty()) {
            throw new ZbException("文件不能为空");
        }
        try {
            BlogFile blogFile = Objects.requireNonNull(OssFactory.init()).uploadFile(file, true);
            fileService.save(blogFile);
            return ResponseUtil.success(blogFile);
        }catch (Exception e) {
            logger.error(String.format("UploadController.upload%s", e));
            return ResponseUtil.error("上传失败",file.getOriginalFilename());
        }
    }

    @PostMapping("/uploadForEditor")
    @AccessToken
    public String uploadForEditor(@RequestParam("img") List<MultipartFile> list){
        if (list == null || list.isEmpty()) {
            throw new ZbException("文件不能为空");
        }
        List<String> urlList= new ArrayList<>();
        for(MultipartFile file : list){
            try {
                BlogFile blogFile = Objects.requireNonNull(OssFactory.init()).uploadFile(file, true);
                String path = blogFile.getFileFullPath();
                fileService.save(blogFile);
                if(StringUtils.isNotBlank(path)){
                    urlList.add(path);
                }else{
                    return  "{\"errno\":-1,\"data\":[]}";
                }
            } catch (Exception e) {
                logger.error(String.format("UploadController.uploadForEditor%s", e));
                return  "{\"errno\":-1,\"data\":[]}";
            }
        }
        return  "{\"errno\":0,\"data\":"+ JSONUtils.toJSONString(urlList)+"}";

    }

}
