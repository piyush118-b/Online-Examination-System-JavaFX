package com.exam.controller;

import com.exam.model.User;
import com.exam.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private AuthService authService = new AuthService();

    @FXML
    private void handleLogin() throws Exception {

        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = authService.login(username, password);

        if (user != null) {

            FXMLLoader loader;

            if (user.getRole().equalsIgnoreCase("ADMIN")) {

                loader = new FXMLLoader(
                        getClass().getResource("/fxml/admin_dashboard.fxml")
                );

                Scene scene = new Scene(loader.load(), 400, 300);

                AdminController controller = loader.getController();
                controller.setUser(user);

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(scene);

            } else {

                loader = new FXMLLoader(
                        getClass().getResource("/fxml/student_dashboard.fxml")
                );

                Scene scene = new Scene(loader.load(), 400, 300);

                StudentController controller = loader.getController();
                controller.setUser(user);

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(scene);
            }

        } else {
            messageLabel.setText("Invalid Credentials!");
        }
    }
}