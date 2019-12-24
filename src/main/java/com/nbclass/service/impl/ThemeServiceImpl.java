package com.nbclass.service.impl;

import com.google.gson.reflect.TypeToken;
import com.nbclass.enums.CacheKeyPrefix;
import com.nbclass.framework.config.properties.ZbProperties;
import com.nbclass.framework.exception.OssException;
import com.nbclass.framework.exception.ZbException;
import com.nbclass.framework.theme.ZbTheme;
import com.nbclass.framework.theme.ZbThemeForm;
import com.nbclass.framework.util.CoreConst;
import com.nbclass.framework.util.FileUtil;
import com.nbclass.framework.util.GsonUtil;
import com.nbclass.service.RedisService;
import com.nbclass.service.ThemeService;
import com.nbclass.service.ThymeleafService;
import com.nbclass.vo.UploadResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.ZipInputStream;

@Service
@Slf4j
public class ThemeServiceImpl implements ThemeService {
    @Autowired
    private ThymeleafService thymeleafService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ZbProperties zbProperties;

    @Override
    public void useTheme(String themeId) {
        CoreConst.currentTheme=themeId;
        ZbTheme theme = this.selectByThemeId(themeId);
        redisService.set(CacheKeyPrefix.CURRENT_THEME.getPrefix(), theme);
        thymeleafService.init();
    }

    @Override
    public ZbTheme selectCurrent() {
        return redisService.get(CacheKeyPrefix.CURRENT_THEME.getPrefix());
    }


    @Override
    public List<ZbTheme> selectAll() {
        String json = redisService.get(CacheKeyPrefix.THEMES.getPrefix());
        Map<String, ZbTheme> map = GsonUtil.fromJson(json,new TypeToken<Map<String, ZbTheme>>(){}.getType());
        List<ZbTheme> list = new ArrayList<>();
        map.forEach((k,v)-> list.add(v));
        return list;
    }

    @Override
    public ZbTheme selectByThemeId(String themeId) {
        return redisService.get(CacheKeyPrefix.THEME.getPrefix()+themeId);
    }

    @Override
    public void updateSettings(String themeId, String settingJson) {
        boolean isCurrent = false;
        if(this.selectCurrent().getId().equals(themeId)){
            isCurrent=true;
        }
        ZbTheme theme = this.selectByThemeId(themeId);
        if(theme!=null){
            List<ZbThemeForm> list = GsonUtil.fromJson(settingJson,new TypeToken<List<ZbThemeForm>>(){}.getType());
            list.forEach(item->{
                theme.getSettings().forEach(setting -> setting.getForm().forEach(formItem->{
                    if(item.getName().equals(formItem.getName())){
                        formItem.setValue(item.getValue());
                    }
                }));
                Map<String, String> settingMap = theme.getSetting();
                for(String key : settingMap.keySet()){
                    if(key.equals(item.getName())){
                        settingMap.put(key,item.getValue());
                    }
                }
            });
            redisService.set(CacheKeyPrefix.THEME.getPrefix()+themeId,theme);
            if(isCurrent){
                redisService.set(CacheKeyPrefix.CURRENT_THEME.getPrefix(),theme);
                thymeleafService.initCurrentTheme(theme);
            }
        }
    }

    @Override
    public String getFileContent(String absolutePath) {
        Path path = Paths.get(absolutePath);
        try {
             return  new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ZbException("读取内容失败 " + absolutePath, e);
        }
    }

    @Override
    public void saveFileContent(String absolutePath, String content) {
        Path path = Paths.get(absolutePath);
        try {
            Files.write(path, content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new ZbException("保存文件失败 " + absolutePath, e);
        }
    }

    @Override
    public void copyUserThemeToSystemTheme(String themeId) {
        try {
            String workThemeDir = zbProperties.getWorkDir() + "theme/" + themeId+"/";
            String themeClassPath = ResourceUtils.CLASSPATH_URL_PREFIX + CoreConst.THEME_FOLDER + themeId +"/";
            URI themeUri = ResourceUtils.getURL(themeClassPath).toURI();
            boolean isJarEnv = "jar".equalsIgnoreCase(themeUri.getScheme());
            FileSystem fileSystem = isJarEnv? FileSystems.newFileSystem(themeUri, Collections.emptyMap()):null;
            Path sysSource = isJarEnv? fileSystem.getPath("/BOOT-INF/classes/" + CoreConst.THEME_FOLDER + themeId+"/") : Paths.get(themeUri);
            Path userSource = Paths.get(workThemeDir);
            FileUtil.copyFolder(userSource, sysSource);
        } catch (Exception e) {
            throw new ZbException("保存文件失败", e);
        }
    }

    @Override
    public UploadResponseVo upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ZbException(UploadResponseVo.Error.FILENOTFOUND);
        }
        if(!StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), ".zip")){
            throw new ZbException(UploadResponseVo.Error.ILLEGALEXTENSION);
        }
        ZipInputStream zis = null;
        Path tempPath = null;
        try {
            tempPath = FileUtil.createTempDirectory();
            String basename = FileUtil.getBaseName(file.getOriginalFilename());
            Path themeTempPath = tempPath.resolve(basename);
            zis = new ZipInputStream(file.getInputStream());
            FileUtil.unzip(zis, themeTempPath);
            Path filterTempPath = FileUtil.skipZipParentFolder(themeTempPath);
            Path settingPath = filterTempPath.resolve(CoreConst.THEME_SETTING_NAME);
            String settingStr = FileUtil.readFile(settingPath);
            if(StringUtils.isNotEmpty(settingStr)){
                Yaml yaml = new Yaml();
                ZbTheme zbTheme = yaml.loadAs(settingStr, ZbTheme.class);
                String themeId = zbTheme.getId();
                if(null!=selectByThemeId(themeId)){
                    throw new ZbException("当前安装的主题已存在！");
                }
                String themeDir = zbProperties.getWorkDir() + "theme/"+themeId+"/";
                Path targetThemePath = Paths.get(themeDir);
                FileUtil.copyFolder(filterTempPath, targetThemePath);
                //用户目录copy到系统目录，待优化
                String workThemeDir = zbProperties.getWorkDir() + "theme/";
                String themeClassPath = ResourceUtils.CLASSPATH_URL_PREFIX + CoreConst.THEME_FOLDER;
                URI themeUri = ResourceUtils.getURL(themeClassPath).toURI();
                boolean isJarEnv = "jar".equalsIgnoreCase(themeUri.getScheme());
                FileSystem fileSystem = isJarEnv? FileSystems.newFileSystem(themeUri, Collections.emptyMap()):null;
                Path sysSource = isJarEnv? fileSystem.getPath("/BOOT-INF/classes/" + CoreConst.THEME_FOLDER) : Paths.get(themeUri);
                Path userSource = Paths.get(workThemeDir);
                FileUtil.copyFolder(userSource, sysSource);
                //存入缓存
                redisService.set(CacheKeyPrefix.THEME.getPrefix()+themeId,zbTheme);
                List<ZbTheme> list = this.selectAll();
                list.add(zbTheme);
                Map<String, ZbTheme> userThemeMap = new LinkedHashMap<>();
                for(ZbTheme theme : list){
                    userThemeMap.put(theme.getId(),theme);
                }
                redisService.set(CacheKeyPrefix.THEMES.getPrefix(),GsonUtil.toJson(userThemeMap));
                return  new UploadResponseVo(themeId,file.getOriginalFilename(), "zip", themeId, CoreConst.SUCCESS_CODE);
            }else{
                throw new ZbException("未找到该主题配置文件！");
            }
        } catch (Exception e) {
            log.error("上传主题失败:{}",e);
            return  new UploadResponseVo(file.getOriginalFilename(), CoreConst.FAIL_CODE, e.getMessage());
        } finally {
            FileUtil.closeStream(zis);
            FileUtil.delete(tempPath);
        }

    }
}
