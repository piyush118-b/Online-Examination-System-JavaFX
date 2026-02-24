package com.exam.controller;

import com.exam.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

public class StudentController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private javafx.scene.control.ComboBox<com.exam.model.ExamPaper> paperComboBox;

    @FXML
    private Label messageLabel;

    private User currentUser;
    private com.exam.dao.ExamPaperDAO paperDAO = new com.exam.dao.ExamPaperDAO();

    @FXML
    public void initialize() {
        if (paperComboBox != null) {
            paperComboBox.getItems().addAll(paperDAO.getAllExamPapers());
        }
    }

    public void setUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome Student: " + user.getUsername());
    }

    @FXML
    private void handleStartExam(ActionEvent event) throws Exception {

        com.exam.model.ExamPaper selectedPaper = paperComboBox.getValue();
        if (selectedPaper == null) {
            messageLabel.setText("Please select a question paper first!");
            return;
        }

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/exam_screen.fxml"));

        Scene scene = new Scene(loader.load(), 1000, 600);
        scene.getStylesheets().add(
                getClass().getResource("/css/exam.css").toExternalForm());

        ExamController controller = loader.getController();
        controller.setUser(currentUser);
        controller.startExamForPaper(selectedPaper.getPaperId());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void handleViewResults(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/view_results.fxml"));

        Scene scene = new Scene(loader.load(), 600, 400);

        ViewResultsController controller = loader.getController();
        controller.initData(currentUser);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void handleLogout(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/login.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load(), 400, 300));
    }
}