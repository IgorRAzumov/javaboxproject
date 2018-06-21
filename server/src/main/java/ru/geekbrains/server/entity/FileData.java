package ru.geekbrains.server.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "files")
public class FileData {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String path;

    @Column(name = "last_modify")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifyAt;

    @Column(name = "user_id", nullable = false)
    private Long userID;

    @Column(name = "file_size", nullable = false)
    private Long size;

    public FileData() {
    }

    public FileData(String fileName, String path,  Date lastModifyAt, Long userID, Long size) {
        this.fileName = fileName;
        this.path = path;
        this.lastModifyAt = lastModifyAt;
        this.userID = userID;
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Date getLastModify() {
        return lastModifyAt;
    }

    public void setLastModify(Date lastModifyAt) {
        this.lastModifyAt = lastModifyAt;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
