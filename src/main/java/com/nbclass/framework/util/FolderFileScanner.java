package com.nbclass.framework.util;

import com.nbclass.framework.exception.ZbException;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class FolderFileScanner {

    public static Map<String, Integer> scanFiles(String folderPath, boolean recursion){
        return listFiles(new HashMap<>(), folderPath, recursion);
    }

    private static Map<String,Integer> listFiles(Map<String,Integer> map, String folderPath, boolean recursion){
        File directory	= new File(folderPath);
        if (!directory.isDirectory()){
            throw new ZbException( '"' + folderPath + '"' + " input path is not a Directory , please input the right path of the Directory. ^_^...^_^" );
        }
        File[] files = directory.listFiles();
        for (File file : Objects.requireNonNull(files)){
            if (file.isDirectory()){
                if(recursion){
                    listFiles(map, file.getAbsolutePath(),true);
                }else{
                    map.put(file.getAbsolutePath(),1);
                }
            }else{
                map.put(file.getAbsolutePath(),2);
            }
        }
        return map;
    }


    public static void main(String[] args) {
        try {
            String filePath = ResourceUtils.getFile("classpath:templates/theme/zblog/").getPath();
            Map<String, Integer> map = FolderFileScanner.scanFiles(filePath, true);
            Map<String, Integer> map1 = FolderFileScanner.scanFiles(filePath, false);
            System.out.println(map);
            System.out.println(map1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}