package com.nbclass.framework.util;

import com.nbclass.framework.theme.ZbFile;
import com.nbclass.framework.theme.ZbTheme;
import com.nbclass.framework.exception.ZbException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
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


    /**
     * 文件或目录是否存在
     */
    public static boolean exists(String path) {
        return new File(path).exists();
    }

    /**
     * 文件是否存在
     */
    public static boolean existsFile(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    /**
     * 删除文件或文件夹
     */
    public static void deleteIfExists(File file) throws IOException {
        if (file.exists()) {
            if (file.isFile()) {
                if (!file.delete()) {
                    throw new IOException("Delete file failure,path:" + file.getAbsolutePath());
                }
            } else {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (File temp : files) {
                        deleteIfExists(temp);
                    }
                }
                if (!file.delete()) {
                    throw new IOException("Delete file failure,path:" + file.getAbsolutePath());
                }
            }
        }
    }

    /**
     * 删除文件或文件夹
     */
    public static void deleteIfExists(String path) throws IOException {
        deleteIfExists(new File(path));
    }

    /**
     * 创建文件夹，如果目标存在则删除
     */
    public static File createDir(String path) throws IOException {
        return createDir(path, false);
    }

    /**
     * 创建文件，如果目标存在则删除
     */
    public static File createFile(String path, boolean isHidden) throws IOException {
        File file = createFileSmart(path);
        if (System.getProperty("os.name").startsWith("win")) {
            Files.setAttribute(file.toPath(), "dos:hidden", isHidden);
        }
        return file;
    }

    public static File createFileSmart(String path) throws IOException {
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            } else {
                createDirSmart(file.getParent());
                file.createNewFile();
            }
            return file;
        } catch (IOException e) {
            throw new IOException("createFileSmart=" + path, e);
        }
    }

    public static File createDirSmart(String path) throws IOException {
        try {
            File file = new File(path);
            if (!file.exists()) {
                Stack<File> stack = new Stack<>();
                File temp = new File(path);
                while (temp != null) {
                    stack.push(temp);
                    temp = temp.getParentFile();
                }
                while (stack.size() > 0) {
                    File dir = stack.pop();
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                }
            }
            return file;
        } catch (Exception e) {
            throw new IOException("createDirSmart=" + path, e);
        }
    }

    /**
     * 创建文件夹，如果目标存在则删除
     */
    public static File createDir(String path, boolean isHidden) throws IOException {
        File file = new File(path);
        deleteIfExists(file);
        File newFile = new File(path);
        newFile.mkdir();
        if (System.getProperty("os.name").startsWith("win")) {
            Files.setAttribute(newFile.toPath(), "dos:hidden", isHidden);
        }
        return file;
    }

    public static void copyFolder(Path source, Path target) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {

            private Path current;

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                current = target.resolve(source.relativize(dir).toString());
                Files.createDirectories(current);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, target.resolve(source.relativize(file).toString()), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }


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
                    themeFile.setChildren(listFileTree(path));
                }
                themeFiles.add(themeFile);
            });
            themeFiles.sort(new ZbFile());
            return themeFiles;
        } catch (IOException e) {
            throw new ZbException("Failed to list tree files");
        }
    }

    public static Map<String, ZbTheme> scanThemeFolder(Path topPath) {
        if (!Files.isDirectory(topPath)) {
            return null;
        }
        try (Stream<Path> pathStream = Files.list(topPath)) {
            Map<String,ZbTheme> resMap = new LinkedHashMap<>();
            pathStream.forEach(path -> {
                if (Files.isDirectory(path)) {
                    String s = readFile(Paths.get(path.toString()+"/setting.json"));
                    if(StringUtils.isNotEmpty(s)){
                        ZbTheme zbTheme=GsonUtil.fromJson(s,ZbTheme.class);
                        String id=path.getFileName().toString();
                        zbTheme.setId(id);
                        resMap.put(id,zbTheme);
                    }

                }
            });
            return resMap;
        } catch (IOException e) {
            throw new ZbException("Failed to scan system theme");
        }
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

    private static String readFile(Path path){
        try {
            return new String(Files.readAllBytes(path));
        } catch (IOException e) {
            return null;
        }
    }
    public static void main(String[] args) {
        try {
             String filePath = ResourceUtils.getFile("classpath:templates/theme/zblog/").getPath();
            List<ZbFile> files = FileUtil.listFiles(Paths.get(filePath), true);
            List<ZbFile> files1  = FileUtil.listFiles(Paths.get(filePath), false);
            List<ZbFile> files2 = FileUtil.listFileTree(Paths.get(filePath));
            System.out.println(GsonUtil.toJson(files2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}