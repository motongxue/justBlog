package com.nbclass.framework.oss;

import com.nbclass.framework.exception.ZbException;
import com.nbclass.framework.util.*;
import com.nbclass.vo.CloudStorageConfigVo;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * 本地存储
 */
public class LocalOssService extends OssService {

    LocalOssService(CloudStorageConfigVo config){
        this.config = config;
    }
    @Override
    public String upload(byte[] data, String path, boolean isPublic) {
        return upload(new ByteArrayInputStream(data), path, isPublic);
    }

    @Override
    public String upload(InputStream is, String path, boolean isPublic) {
        try {
            String realPath = getRealPath(path);
            FileOutputStream os = new FileOutputStream(realPath);
            byte[] bb = new byte[1024];
            int ch;
            while ((ch = is.read(bb)) > -1) {
                os.write(bb, 0, ch);
            }
            os.close();
            is.close();
        } catch (Exception e){
            throw new ZbException("上传本地文件失败", e);
        }
        String localDomain = config.getLocalDomain().endsWith("/")?config.getLocalDomain():config.getLocalDomain()+"/";
        return localDomain + CoreConst.FILE_FOLDER + path;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix, boolean isPublic) {
        return upload(data, getPath(suffix), isPublic);
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix, boolean isPublic) {
        return upload(inputStream, getPath(suffix), isPublic);
    }

    private String getPath(String suffix) {
        //文件按日期分文件夹
        String date = DateUtil.getDateString(new Date());
        String path = date + "/" + UUIDUtil.generateShortUuid() + suffix;
        String dir = PropertiesUtil.getString(CoreConst.workDirKey) + CoreConst.FILE_FOLDER + date;
        if(!FileUtil.exists(dir)){
            try {
                FileUtil.createDir(dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    private String getRealPath(String path){
        return PropertiesUtil.getString(CoreConst.workDirKey)+ CoreConst.FILE_FOLDER +path;
    }
}