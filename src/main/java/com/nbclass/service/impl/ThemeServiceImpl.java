package com.nbclass.service.impl;

import com.google.gson.reflect.TypeToken;
import com.nbclass.enums.CacheKeyPrefix;
import com.nbclass.framework.config.properties.ZbProperties;
import com.nbclass.framework.exception.ZbException;
import com.nbclass.framework.theme.ZbTheme;
import com.nbclass.framework.theme.ZbThemeForm;
import com.nbclass.framework.theme.ZbThemeSetting;
import com.nbclass.framework.util.CoreConst;
import com.nbclass.framework.util.FileUtil;
import com.nbclass.framework.util.GsonUtil;
import com.nbclass.framework.util.ResponseUtil;
import com.nbclass.model.BlogFile;
import com.nbclass.service.RedisService;
import com.nbclass.service.ThemeService;
import com.nbclass.service.ThymeleafService;
import com.nbclass.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
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
        Map<String, ZbTheme> map = selectThemesMap();
        List<ZbTheme> list = new LinkedList<>();
        map.forEach((k,v)-> list.add(v));
        return list;
    }

    @Override
    public Map<String, ZbTheme> selectThemesMap() {
        String json = redisService.get(CacheKeyPrefix.THEMES.getPrefix());
        return GsonUtil.fromJson(json,new TypeToken<Map<String, ZbTheme>>(){}.getType());
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
    public void delete(String themeId) {
        if(themeId.equals(selectCurrent().getId())){
            throw new ZbException("主题正在使用，不可删除");
        }
        FileUtil.delete(getUserThemePath(themeId));
        FileUtil.delete(getSysThemePath(themeId));
        redisService.del(CacheKeyPrefix.THEME.getPrefix()+themeId);
        Map<String, ZbTheme> themeMap = selectThemesMap();
        themeMap.remove(themeId);
        redisService.set(CacheKeyPrefix.THEMES.getPrefix(),GsonUtil.toJson(themeMap));
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
            Path sysSource = this.getSysThemePath(themeId);
            Path userSource = this.getUserThemePath(themeId);
            FileUtil.copyFolder(userSource, sysSource);
        } catch (Exception e) {
            throw new ZbException("保存文件失败", e);
        }
    }

    @Override
    public ResponseVo upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ZbException("文章不能为空");
        }
        if(!StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), ".zip")){
            throw new ZbException("不支持的文件类型");
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
                String themeDir = zbProperties.getWorkThemeDir(themeId);
                Path targetThemePath = Paths.get(themeDir);
                FileUtil.copyFolder(filterTempPath, targetThemePath);
                //用户目录copy到系统目录，待优化
                Path sysSource = this.getSysThemePath(null);
                Path userSource = this.getUserThemePath(null);
                FileUtil.copyFolder(userSource, sysSource);
                //存入缓存
                handleThemeSetting(zbTheme);
                Map<String, ZbTheme> themeMap = selectThemesMap();
                themeMap.put(themeId,zbTheme);
                redisService.set(CacheKeyPrefix.THEMES.getPrefix(),GsonUtil.toJson(themeMap));
                BlogFile blogFile = new BlogFile();
                blogFile.withOriginalName(file.getOriginalFilename())
                        .withFileName(themeId)
                        .withFilePath(themeId);
                return ResponseUtil.success(blogFile);
            }else{
                throw new ZbException("未找到该主题配置文件！");
            }
        } catch (Exception e) {
            log.error("上传主题失败:{}",e);
            return ResponseUtil.error(file.getOriginalFilename());
        } finally {
            FileUtil.closeStream(zis);
            FileUtil.delete(tempPath);
        }

    }

    @Override
    public Path getSysThemePath(String themeId) {
        try {
            String themeClassPath = String.format("%s%s%s",ResourceUtils.CLASSPATH_URL_PREFIX,CoreConst.THEME_FOLDER,StringUtils.isNotEmpty(themeId)?(themeId+"/"):"");
            URI themeUri = ResourceUtils.getURL(themeClassPath).toURI();
            boolean isJarEnv = "jar".equalsIgnoreCase(themeUri.getScheme());
            FileSystem fileSystem = isJarEnv? FileSystems.newFileSystem(themeUri, Collections.emptyMap()):null;
            return isJarEnv? fileSystem.getPath(String.format("/BOOT-INF/classes/%s%s",CoreConst.THEME_FOLDER,StringUtils.isNotEmpty(themeId)?(themeId+"/"):"")) : Paths.get(themeUri);
        } catch (Exception e) {
            log.error("获取系统主题路径失败：{}",e);
        }
        return null;
    }

    @Override
    public Path getUserThemePath(String themeId) {
        return  Paths.get(StringUtils.isEmpty(themeId)? zbProperties.getWorkThemeDir():zbProperties.getWorkThemeDir(themeId));
    }

    @Override
    public void handleThemeSetting(ZbTheme theme) {
        List<ZbThemeSetting> settingList = theme.getSettings();
        Map<String,String> map= new HashMap<>();
        settingList.forEach(setting-> setting.getForm().forEach(formItem->{
            formItem.setValue(formItem.getDefaultValue());
            map.put(formItem.getName(),formItem.getValue());
        }));
        theme.setSetting(map);
        redisService.set(CacheKeyPrefix.THEME.getPrefix() + theme.getId(), theme);
    }

}
