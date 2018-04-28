package com.aktu.root.teachersassistant.teacher.post_file;

/**
 * Created by root on 2/28/18.
 */

public class FileDataModel {
    private String fileName, fileUrl ,fileDescription;

    public FileDataModel(String fileName1,String fileDescription1, String fileUrl1) {
        fileName = fileName1;
        fileUrl = fileUrl1;
        fileDescription = fileDescription1;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getFileDescription() {
        return fileDescription;
    }
}
