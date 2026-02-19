package com.exam.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import com.exam.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdminController {
    @FXML private VBox selectedImagesBox;

    private List<File> selectedImageFiles = new ArrayList<>();

    @FXML
    private Label welcomeLabel;

    public void setUser(User user) {
        welcomeLabel.setText("Welcome Admin: " + user.getUsername());
    }

    @FXML
    private void handleLogout(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/login.fxml")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load(), 400, 300));
    }
    @FXML
    private void handleAddImage() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        List<File> files = fileChooser.showOpenMultipleDialog(null);

        if (files != null) {
            selectedImageFiles.addAll(files);

            for (File file : files) {
                Label label = new Label(file.getName());
                selectedImagesBox.getChildren().add(label);
            }
        }
    }

    @FXML
    private void handleManageQuestions(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/manage_questions.fxml")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load(), 500, 500));
    }
}