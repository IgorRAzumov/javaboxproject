package ru.geekbrains.client.data;

import javafx.beans.property.*;
import ru.geekbrains.client.MyDate;

import java.sql.Date;


public class FileInfoTableViewEntity {
    private LongProperty fileSize;
    private StringProperty name;
    private SimpleObjectProperty<MyDate> localData;

    public FileInfoTableViewEntity() {
        this(0, "", new MyDate(System.currentTimeMillis()));
    }

    public FileInfoTableViewEntity(long fileSize, String name, MyDate localData) {
        this.fileSize = new SimpleLongProperty(fileSize);
        this.name = new SimpleStringProperty(name);
        this.localData = new SimpleObjectProperty<>(localData);
    }


    public long getFileSize() {
        return fileSize.get();
    }

    public void setFileSize(long fileSize) {
        this.fileSize.set(fileSize);
    }

    public LongProperty fileSizeProperty() {
        return fileSize;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public Date getLocalData() {
        return localData.get();
    }

    public void setLocalData(MyDate localData) {
        this.localData.set(localData);
    }

    public SimpleObjectProperty<MyDate> localDataProperty() {
        return localData;
    }
}
