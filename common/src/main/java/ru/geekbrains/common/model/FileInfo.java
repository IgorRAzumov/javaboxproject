package ru.geekbrains.common.model;

import java.io.Serializable;
import java.util.Date;

public class FileInfo implements Serializable {
    private Date lastModify;
    private String name;
    private Long size;

    public FileInfo() {
    }

    public FileInfo(Date lastModify, String name, Long size) {
        this.lastModify = lastModify;
        this.name = name;
        this.size = size;
    }

    public Date getLastModify() {
        return lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
