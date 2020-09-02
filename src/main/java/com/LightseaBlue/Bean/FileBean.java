package com.LightseaBlue.Bean;

import java.io.Serializable;

/**
 * Description: FileBean
 * Date: 2020/2/7
 * Time 12:59
 * Author: LightseaBlue
 * Version: 1.0 <br>
 */
public class FileBean implements Serializable {
    private static final long serialVersionUID = -4307158979815011952L;

    private String FileTime;//文件创建时间
    private String FileName;//文件名
    private String FileLen="";//文件大小
    private String FilePath;//文件路径

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public String getFileTime() {
        return FileTime;
    }

    public void setFileTime(String fileTime) {
        FileTime = fileTime;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFileLen() {
        return FileLen;
    }

    public void setFileLen(String fileLen) {
        FileLen = fileLen;
    }
}
