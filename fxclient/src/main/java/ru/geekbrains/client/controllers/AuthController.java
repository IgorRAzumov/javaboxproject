package ru.geekbrains.client.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ru.geekbrains.client.Strings;
import ru.geekbrains.client.data.ResultCallback;
import ru.geekbrains.client.services.AuthService;
import ru.geekbrains.client.services.IAuthService;
import ru.geekbrains.common.model.UserData;
import ru.geekbrains.common.utils.AuthUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class AuthController implements Initializable, ResultCallback<UserData> {
    private static final String ERROR_NETWORK = "network error";
    private static final String ERROR_REQUEST = "response error ";
    private static final String ERROR_PASSWORD_ENTRY = "password error";
    private static final String ERROR_LOGIN_ENTRY = "login error";
    private static final String ERROR_EMAIL_ENTRY = "email error";
    private static final String ERROR_CLIENT = "application error";
    private static final String EMPTY_TEXT = "";

    @FXML
    private StackPane authStackPane;

    @FXML
    private GridPane startGrid;


    @FXML
    private GridPane signUpGrid;
    @FXML
    private TextField userNameFieldSignUp;
    @FXML
    private PasswordField passwordFieldSignUp;
    @FXML
    private PasswordField confirmPasswordFieldSignUp;
    @FXML
    private TextField loginFieldSignUp;
    @FXML
    private TextField emailFieldSignUp;
    @FXML
    private Label messageErrorSignUpText;
    @FXML
    private Button getRegistrationButton;
    @FXML
    private Button backSignUpButton;


    @FXML
    private GridPane signInGrid;
    @FXML
    private TextField loginFieldSignIn;
    @FXML
    private PasswordField passwordFieldSignIn;
    @FXML
    private CheckBox rememberCheckBox;
    @FXML
    private Label messageErrorSignInText;
    @FXML
    private Button getAuthorizationButton;
    @FXML
    private Button backSignInButton;

    private AuthUtils authUtils;

    private IAuthService authService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        authUtils = new AuthUtils();
        authService = new AuthService();
    }

    @FXML
    private void signInMenuButtonClick(ActionEvent actionEvent) {
        closeStartPanel();
        openSignInPanel();
    }

    @FXML
    private void signUpMenuButtonClick(ActionEvent actionEvent) {
        closeStartPanel();
        openSignUpPanel();
    }

    @FXML
    private void signInButtonClick(ActionEvent actionEvent) {
        messageErrorSignInText.setText(EMPTY_TEXT);

        String login = loginFieldSignIn.getText();
        String password = passwordFieldSignIn.getText();

        if (authUtils.checkLoginMistake(login)) {
            if (authUtils.checkPasswordMistake(password)) {
                try {
                    setButtonsEnabled(false);
                    authService.signIn(login, password, this);
                } catch (Exception e) {
                    setButtonsEnabled(true);
                    clearSignInFields();
                    showErrorMessage(ERROR_CLIENT);
                    e.printStackTrace();
                }
            } else {
                showErrorMessage(ERROR_PASSWORD_ENTRY);
                passwordFieldSignIn.clear();
            }
        } else {
            showErrorMessage(ERROR_LOGIN_ENTRY);
            loginFieldSignIn.clear();
        }
    }

    @FXML
    private void signUpButtonClick(ActionEvent actionEvent) {
        messageErrorSignUpText.setText(EMPTY_TEXT);

        String login = loginFieldSignUp.getText();
        String password = passwordFieldSignUp.getText();
        String email = emailFieldSignUp.getText();
        String userName = userNameFieldSignUp.getText();

        if (authUtils.checkLoginMistake(login)) {
            if (authUtils.checkPasswordMistake(password) && password.equals(confirmPasswordFieldSignUp.getText())) {
                if (authUtils.checkEmailMistake(email)) {
                    try {
                        setButtonsEnabled(false);
                        authService.signUp(login, password, email, userName, this);

                    } catch (Exception e) {
                        clearSignUpFields();
                        setButtonsEnabled(true);
                        showErrorMessage(ERROR_CLIENT);
                        e.printStackTrace();
                    }
                } else {
                    showErrorMessage(ERROR_EMAIL_ENTRY);
                    emailFieldSignUp.clear();
                }
            } else {
                showErrorMessage(ERROR_PASSWORD_ENTRY);
                passwordFieldSignUp.clear();
                confirmPasswordFieldSignUp.clear();
            }
        } else {
            showErrorMessage(ERROR_LOGIN_ENTRY);
            loginFieldSignUp.clear();
        }
    }

    @FXML
    private void showStartPanel(ActionEvent actionEvent) {
        if (signInGrid.isManaged()) {
            clearSignInFields();
            closeSignInPanel();

        } else {
            clearSignUpFields();
            closeSignUpPanel();
        }
        opensStartPanel();
    }

    @FXML
    private void exitButtonClick(ActionEvent actionEvent) {
        Platform.exit();
    }

    @Override
    public void onSuccessful(UserData result) {
        switchScreen(result);
    }

    @Override
    public void onFailure(String message) {
        showErrorMessage(message);
        resetPanelAfterResponse();
    }

    private void resetPanelAfterResponse() {
        setButtonsEnabled(true);
        if (signInGrid.isManaged()) {
            clearSignInFields();
        } else {
            clearSignUpFields();
        }
    }

    private void clearSignInFields() {
        Platform.runLater(() -> {
            loginFieldSignIn.clear();
            passwordFieldSignIn.clear();
        });
    }

    private void clearSignUpFields() {
        Platform.runLater(() -> {
            loginFieldSignUp.clear();
            passwordFieldSignUp.clear();
            emailFieldSignUp.clear();
            userNameFieldSignUp.clear();
            confirmPasswordFieldSignUp.clear();
        });
    }

    private void setButtonsEnabled(boolean enabled) {
        Platform.runLater(() -> {
            if (signInGrid.isVisible()) {
                backSignInButton.setDisable(enabled);
                getAuthorizationButton.setDisable(enabled);
            } else {
                backSignUpButton.setDisable(enabled);
                getRegistrationButton.setDisable(enabled);
            }
        });
    }

    private void showErrorMessage(String message) {
        Platform.runLater(() -> {
            if (signInGrid.isVisible()) {
                messageErrorSignInText.setText(message);
            } else {
                messageErrorSignUpText.setText(message);
            }
        });
    }

    private void closeStartPanel() {
        Platform.runLater(() -> {
            startGrid.setVisible(false);
            startGrid.setManaged(false);
        });
    }

    private void closeSignInPanel() {
        Platform.runLater(() -> {
            messageErrorSignInText.setText(EMPTY_TEXT);
            signInGrid.setVisible(false);
            signInGrid.setManaged(false);
        });
    }

    private void closeSignUpPanel() {
        Platform.runLater(() -> {
            messageErrorSignUpText.setText(EMPTY_TEXT);
            signUpGrid.setVisible(false);
            signUpGrid.setManaged(false);
        });
    }

    private void opensStartPanel() {
        Platform.runLater(() -> {
            startGrid.setVisible(true);
            startGrid.setManaged(true);
        });
    }

    private void openSignUpPanel() {
        Platform.runLater(() -> {
            signUpGrid.setVisible(true);
            signUpGrid.setManaged(true);
        });
    }

    private void openSignInPanel() {
        Platform.runLater(() -> {
            signInGrid.setVisible(true);
            signInGrid.setManaged(true);
        });
    }

    //переключение между окнами клиента с передачей информации
    private void switchScreen(UserData userData) {
        Platform.runLater(() -> {
            Stage stage = (Stage) authStackPane.getScene().getWindow();
            stage.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root;
            try {
                root = fxmlLoader.load();
                stage = new Stage();
                FileStorageController fileStorageController = fxmlLoader.getController();
                fileStorageController.initData(userData);

                stage.setTitle(Strings.CLIENT_TITLE);
                stage.setScene(new Scene(root, 500, 500));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


}
