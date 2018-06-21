package ru.geekbrains.client.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import ru.geekbrains.client.MyDate;
import ru.geekbrains.client.data.FileInfoTableViewEntity;
import ru.geekbrains.client.data.ResultCallback;
import ru.geekbrains.client.services.FileStorageService;
import ru.geekbrains.common.model.FileInfo;
import ru.geekbrains.common.model.UserData;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FileStorageController implements Initializable {
    private static final String SELECT_DOWNLOAD_FILES = "Select files";
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private Label messageLabel;

    @FXML
    private TableView<FileInfoTableViewEntity> filesInStorageTableView;
    @FXML
    private TableColumn<FileInfoTableViewEntity, Number> fileSizeColumn;
    @FXML
    private TableColumn<FileInfoTableViewEntity, MyDate> fileLastModify;
    @FXML
    private TableColumn<FileInfoTableViewEntity, String> fileNameColumn;

    private UserData userData;
    private FileStorageService fileStorageService;
    private ObservableList<FileInfoTableViewEntity> filesTableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileStorageService = new FileStorageService();
        filesTableList = FXCollections.observableArrayList();
        fileNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        fileSizeColumn.setCellValueFactory(cellData -> cellData.getValue().fileSizeProperty());
        fileLastModify.setCellValueFactory(cellData -> cellData.getValue().localDataProperty());
    }


    public void initData(UserData userData) {
        this.userData = userData;
        Platform.runLater(() -> messageLabel.setText(userData.getName()));
    }


    public void getFileList(ActionEvent actionEvent) {
        fileStorageService.getAllFiles(userData.getToken(), new ResultCallback<List<FileInfo>>() {
            @Override
            public void onSuccessful(List<FileInfo> result) {
                filesTableList = FXCollections.observableArrayList(fileStorageService.mapDateToTableViewFormat(result));
                filesInStorageTableView.setItems(filesTableList);
            }

            @Override
            public void onFailure(String message) {
                Platform.runLater(() -> messageLabel.setText(message));
            }
        });
    }

    @FXML
    private void uploadFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        List<File> files = fileChooser.showOpenMultipleDialog(mainBorderPane.getScene().getWindow());
        if (files != null && files.size() != 0) {
            fileStorageService.uploadFiles(files, userData.getToken(), new ResultCallback<List<FileInfo>>() {
                @Override
                public void onSuccessful(List<FileInfo> result) {
                    filesTableList = FXCollections.observableArrayList(fileStorageService.mapDateToTableViewFormat(result));
                    filesInStorageTableView.setItems(filesTableList);
                }

                @Override
                public void onFailure(String message) {
                    Platform.runLater(() -> messageLabel.setText(message));
                }
            });
        } else {
            //ичего не выбрано
        }
    }

    private void downloadFile() {
        fileStorageService.downloadFile(1, userData.getToken(), new ResultCallback<File>() {
            @Override
            public void onSuccessful(File result) {

            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    @FXML
    private void logOut(ActionEvent actionEvent) {
        downloadFile();
    }

    public void exit(ActionEvent actionEvent) {

    }


}
