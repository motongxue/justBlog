package com.nbclass.controller.admin;

import com.alibaba.druid.support.json.JSONUtils;
import com.google.gson.Gson;
import com.nbclass.enums.ConfigKey;
import com.nbclass.framework.annotation.AccessToken;
import com.nbclass.framework.exception.OssException;
import com.nbclass.framework.oss.OssFactory;
import com.nbclass.framework.util.CoreConst;
import com.nbclass.framework.util.ResponseUtil;
import com.nbclass.service.ConfigService;
import com.nbclass.vo.CloudStorageConfigVo;
import com.nbclass.vo.ResponseVo;
import com.nbclass.vo.UploadResponseVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    private ConfigService configService;

    @PostMapping(value = "/upload")
    @AccessToken
    public UploadResponseVo upload(@RequestParam MultipartFile file) throws Exception{
        if (file == null || file.isEmpty()) {
            throw new OssException(UploadResponseVo.Error.FILENOTFOUND);
        }
        String originalFilename = file.getOriginalFilename();
        try {
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            String path = Objects.requireNonNull(OssFactory.init()).uploadSuffix(file.getBytes(), suffix,true);
            return  new UploadResponseVo(path,originalFilename, suffix, path, CoreConst.SUCCESS_CODE);
        }catch (OssException e) {
            return  new UploadResponseVo(originalFilename, CoreConst.FAIL_CODE, e.getMessage());
        }catch (Exception e) {
            logger.error(String.format("UploadController.upload%s", e));
            throw e;
        }
    }

    @PostMapping("/uploadForEditor")
    @AccessToken
    public String upload(@RequestParam("img") List<MultipartFile> list){
        if (list == null || list.isEmpty()) {
            throw new OssException(UploadResponseVo.Error.FILENOTFOUND);
        }
        List<String> urlList= new ArrayList<>();
        for(MultipartFile file : list){
            String originalFilename = file.getOriginalFilename();
            try {
                String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
                String path = Objects.requireNonNull(OssFactory.init()).uploadSuffix(file.getBytes(), suffix,true);
                if(StringUtils.isNotBlank(path)){
                    urlList.add(path);
                }else{
                    return  "{\"errno\":-1,\"data\":[]}";
                }
            } catch (Exception e) {
                logger.error(String.format("NewsController.upload%s", e));
                return  "{\"errno\":-1,\"data\":[]}";
            }
        }
        return  "{\"errno\":0,\"data\":"+ JSONUtils.toJSONString(urlList)+"}";

    }

}
