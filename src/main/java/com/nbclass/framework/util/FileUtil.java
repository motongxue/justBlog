package com.nbclass.framework.util;

import com.nbclass.framework.exception.ZbException;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Stream;


/**
 * FolderFileScanner
 *
 * @version V1.0
 * @date 2019/10/11
 * @author nbclass
 */
public class FileUtil {

    private static final String[] CAN_EDIT_SUFFIX = {".html", ".css", ".js", ".yaml", ".yml", ".properties"};



    public static List<ZbFile> listFiles(Path topPath, boolean recursion){
        List<ZbFile> files = listFiles(new ArrayList<>(), topPath, recursion);
        if (files != null) {
            files.sort(new ZbFile());
        }
        return  files;
    }

    public static List<ZbFile> listFileTree(Path topPath) {
        if (!Files.isDirectory(topPath)) {
            return null;
        }
        try (Stream<Path> pathStream = Files.list(topPath)) {
            List<ZbFile> themeFiles = new LinkedList<>();
            pathStream.forEach(path -> {
                ZbFile themeFile = new ZbFile();
                themeFile.setName(path.getFileName().toString());
                themeFile.setPath(path.toString());
                themeFile.setIsFile(Files.isRegularFile(path));
                themeFile.setIsEdit(isEditable(path));
                if (Files.isDirectory(path)) {
                    themeFile.setNode(listFileTree(path));
                }
                themeFiles.add(themeFile);
            });
            themeFiles.sort(new ZbFile());
            return themeFiles;
        } catch (IOException e) {
            throw new ZbException("Failed to list tree files");
        }
    }

    public static LinkedList<String> scanSystemTheme(Path topPath) {
        if (!Files.isDirectory(topPath)) {
            return null;
        }
        try (Stream<Path> pathStream = Files.list(topPath)) {
            LinkedList<String> themeList = new LinkedList<>();
            pathStream.forEach(path -> {
                if (Files.isDirectory(path)) {
                    themeList.add(path.getFileName().toString());
                }
            });
            return themeList;
        } catch (IOException e) {
            throw new ZbException("Failed to scan system theme");
        }
    }

    public static boolean isExist(String path){
        File file =new File(path);
        return file.exists();
    }

    private static List<ZbFile> listFiles(List<ZbFile> list, Path topPath, boolean recursion){
        if (!Files.isDirectory(topPath)) {
            return null;
        }
        try (Stream<Path> pathStream = Files.list(topPath)) {
            pathStream.forEach(path -> {
                if (Files.isDirectory(path)) {
                    if(recursion){
                        listFiles(list,path,true);
                    }
                }else{
                    ZbFile themeFile = new ZbFile();
                    themeFile.setName(path.getFileName().toString());
                    themeFile.setPath(path.toString());
                    themeFile.setIsFile(Files.isRegularFile(path));
                    themeFile.setIsEdit(isEditable(path));
                    list.add(themeFile);
                }

            });
            return list;
        } catch (IOException e) {
            throw new ZbException("Failed to list files");
        }
    }


    private static boolean isEditable(Path path) {
        boolean isEditable = Files.isReadable(path) && Files.isWritable(path);
        if (!isEditable) {
            return false;
        }
        for (String suffix : CAN_EDIT_SUFFIX) {
            if (path.toString().endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        try {
            String filePath = new ClassPathResource("/templates/theme/zblog/").getPath();
           /* String filePath = ResourceUtils.getFile("classpath:templates/theme/zblog/").getPath();*/
            List<ZbFile> files = FileUtil.listFiles(Paths.get(filePath), true);
            List<ZbFile> files1  = FileUtil.listFiles(Paths.get(filePath), false);
            List<ZbFile> files2 = FileUtil.listFileTree(Paths.get(filePath));
            System.out.println(GsonUtil.toJson(files2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}