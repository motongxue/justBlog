package com.nbclass.framework.oss;

import com.nbclass.framework.exception.OssException;
import com.nbclass.framework.util.*;
import com.nbclass.model.BlogFile;
import com.nbclass.vo.CloudStorageConfigVo;
import com.qiniu.util.IOUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.file.Paths;
import java.util.Date;

/**
 * 本地存储
 */
public class LocalOssService extends OssService {

    LocalOssService(CloudStorageConfigVo config){
        this.config = config;
    }
    @Override
    public BlogFile upload(byte[] data, String path, boolean isPublic) {
        String date = DateUtil.getDateString(new Date());
        String realPath = getRealPath(path, date);
        try (FileOutputStream os = new FileOutputStream(realPath)){
            FileCopyUtils.copy(data, os);
            String localDomain = config.getLocalDomain().endsWith("/")?config.getLocalDomain():config.getLocalDomain()+"/";
            String filePath = CoreConst.FILE_ + "/" + date + "/" + path;
            BlogFile blogFile = new BlogFile();
            blogFile.withFilePath(filePath)
                    .withFileFullPath(localDomain + filePath)
                    .withFileName(path)
                    .withFileType(getFileType(path))
                    .withOssType(OssTypeEnum.LOCAL.getValue());
            return blogFile;
        } catch (Exception e){
            throw new OssException("上传本地文件失败", e);
        }
    }

    @Override
    public BlogFile upload(InputStream is, String path, boolean isPublic){
        try {
            return upload(IOUtils.toByteArray(is), path, isPublic);
        } catch (IOException e) {
            throw new OssException("上传本地文件失败", e);
        }
    }

    @Override
    public BlogFile uploadSuffix(byte[] data, String suffix, boolean isPublic) {
        return upload(data, getPath(suffix), isPublic);
    }

    @Override
    public BlogFile uploadSuffix(InputStream inputStream, String suffix, boolean isPublic) {
        return upload(inputStream, getPath(suffix), isPublic);
    }

    @Override
    public void delete(String path) {
        FileUtil.delete(Paths.get(CoreConst.USER_HOME + File.separator + PropertiesUtil.getString(CoreConst.WORK_DIR_KEY) + File.separator + path));
    }

    private String getPath(String suffix) {
        return UUIDUtil.generateShortUuid() + suffix;
    }

    private String getRealPath(String path, String pre){
        String dir = CoreConst.USER_HOME + File.separator + PropertiesUtil.getString(CoreConst.WORK_DIR_KEY) + File.separator + CoreConst.FILE_ + File.separator + pre;
        if(!FileUtil.exists(dir)){
            try {
                FileUtil.createDir(dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dir + File.separator + path;
    }
}
