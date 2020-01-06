package com.nbclass.framework.util;

import com.nbclass.framework.exception.ZbException;
import com.nbclass.framework.theme.ZbFile;
import com.nbclass.framework.theme.ZbTheme;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * FolderFileScanner
 *
 * @version V1.0
 * @date 2019/10/11
 * @author nbclass
 */
@Slf4j
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
        newFile.mkdirs();
        if (System.getProperty("os.name").startsWith("win")) {
            Files.setAttribute(newFile.toPath(), "dos:hidden", isHidden);
        }
        return file;
    }

    public static void checkDirectoryTraversal(Path parentPath, Path pathToCheck) {
        if (pathToCheck.startsWith(parentPath.normalize())) {
            return;
        }

        throw new ZbException("无权限访问 " + pathToCheck);
    }


    public static Path createTempDirectory() throws IOException {
        return Files.createTempDirectory("nbclass");
    }

    public static boolean isEmpty(Path path) throws IOException {
        if (!Files.isDirectory(path) || Files.notExists(path)) {
            return true;
        }

        try (Stream<Path> pathStream = Files.list(path)) {
            return pathStream.count() == 0;
        }
    }

    public static void unzip(ZipInputStream zis, Path targetPath) throws IOException {
        if (Files.notExists(targetPath)) {
            Files.createDirectories(targetPath);
        }
        if (!isEmpty(targetPath)) {
            throw new DirectoryNotEmptyException("Target directory: " + targetPath + " was not empty");
        }

        ZipEntry zipEntry = zis.getNextEntry();

        while (zipEntry != null) {
            Path entryPath = targetPath.resolve(zipEntry.getName());
            FileUtil.checkDirectoryTraversal(targetPath, entryPath);
            if (zipEntry.isDirectory()) {
                Files.createDirectories(entryPath);
            } else {
                Files.copy(zis, entryPath);
            }
            zipEntry = zis.getNextEntry();
        }
    }

    public static void delete(Path deletingPath) {
        try {
            if (Files.isDirectory(deletingPath)) {
                Files.walk(deletingPath).sorted(Comparator.reverseOrder()).map(Path::toFile)
                        .forEach(File::delete);
            } else {
                Files.delete(deletingPath);
            }
        } catch (IOException e) {
            log.warn("Failed to delete:{},{}",deletingPath,e);
        }
    }

    public static String getBaseName(String filename) {
        int separatorLastIndex = StringUtils.lastIndexOf(filename, File.separatorChar);
        if (separatorLastIndex == filename.length() - 1) {
            return "";
        }
        if (separatorLastIndex >= 0 && separatorLastIndex < filename.length() - 1) {
            filename = filename.substring(separatorLastIndex + 1);
        }
        int dotLastIndex = StringUtils.lastIndexOf(filename, '.');

        if (dotLastIndex < 0) {
            return filename;
        }
        return filename.substring(0, dotLastIndex);
    }

    public static void closeStream(InputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            log.warn("Failed to close input stream", e);
        }
    }

    public static void closeStream(ZipInputStream zipInputStream) {
        try {
            if (zipInputStream != null) {
                zipInputStream.closeEntry();
                zipInputStream.close();
            }
        } catch (IOException e) {
            log.warn("Failed to close input zipInputStream", e);
        }
    }

    public static Path skipZipParentFolder(Path unzippedPath) throws IOException {
        try (Stream<Path> pathStream = Files.list(unzippedPath)) {
            List<Path> childrenPath = pathStream.collect(Collectors.toList());
            if (childrenPath.size() == 1 && Files.isDirectory(childrenPath.get(0))) {
                return childrenPath.get(0);
            }
            return unzippedPath;
        }
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
                ZbFile themeFile = pathToZbFile(path);
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
                    String s = readFile(path.resolve(CoreConst.THEME_SETTING_NAME));
                    if(StringUtils.isNotEmpty(s)){
                        Yaml yaml = new Yaml();
                        ZbTheme zbTheme = yaml.loadAs(s, ZbTheme.class);
                        resMap.put(zbTheme.getId(),zbTheme);
                    }
                }
            });
            return resMap;
        } catch (IOException e) {
            throw new ZbException("Failed to scan theme folder");
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
                    list.add(pathToZbFile(path));
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

    public static String readFile(Path path){
        try {
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    private static ZbFile pathToZbFile(Path path){
        ZbFile file = new ZbFile();
        file.setName(path.getFileName().toString());
        file.setPath(path.toString());
        file.setIsFile(Files.isRegularFile(path));
        file.setIsEdit(isEditable(path));
        file.setDisabled(file.getIsFile()&&!file.getIsEdit());
        return file;
    }

    public static void main(String[] args) {
        try {
             String filePath = ResourceUtils.getFile("classpath:templates/theme/nbclass/").getPath();
            List<ZbFile> files = FileUtil.listFiles(Paths.get(filePath), true);
            List<ZbFile> files1  = FileUtil.listFiles(Paths.get(filePath), false);
            List<ZbFile> files2 = FileUtil.listFileTree(Paths.get(filePath));
            System.out.println(GsonUtil.toJson(files2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}