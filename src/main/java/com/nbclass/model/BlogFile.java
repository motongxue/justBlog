package com.nbclass.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 *  文件
 * @author nbclass 2020-01-03
 */
@Data
public class BlogFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
    * 原始文件名称
    */
    private String originalName;

    /**
    * 文件名称
    */
    private String fileName;

    /**
    * 文件类型
    */
    private String fileType;

    /**
    * 友链图片地址
    */
    private String fileSize;

    /**
    * 文件相对路径
    */
    private String filePath;

    /**
    * 文件绝对路径
    */
    private String fileFullPath;

    /**
    * file_hash
    */
    private String fileHash;

    /**
    * oss存储类型
    */
    private Integer ossType;

    /**
    * 状态
    */
    private Integer status;

    /**
    * 添加时间
    */
    private Date createTime;

    /**
    * 更新时间
    */
    private Date updateTime;


    public BlogFile withOriginalName(String originalName) {
        this.originalName = originalName;
        return this;
    }
    public BlogFile withFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
    public BlogFile withFileType(String fileType) {
        this.fileType = fileType;
        return this;
    }
    public BlogFile withFileSize(String fileSize) {
        this.fileSize = fileSize;
        return this;
    }
    public BlogFile withFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }
    public BlogFile withFileFullPath(String fileFullPath) {
        this.fileFullPath = fileFullPath;
        return this;
    }
    public BlogFile withFileHash(String fileHash) {
        this.fileHash = fileHash;
        return this;
    }
    public BlogFile withOssType(Integer ossType) {
        this.ossType = ossType;
        return this;
    }
    public BlogFile withStatus(Integer status) {
        this.status = status;
        return this;
    }
    public BlogFile withCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
    public BlogFile withUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

}
