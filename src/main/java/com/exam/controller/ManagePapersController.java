package com.exam.controller;

import com.exam.dao.ExamPaperDAO;
import com.exam.model.ExamPaper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ManagePapersController {

    @FXML
    private ListView<ExamPaper> paperListView;
    @FXML
    private TextField paperNameField;
    @FXML
    private TextField paperDescriptionField;
    @FXML
    private Label messageLabel;

    private ExamPaperDAO paperDAO = new ExamPaperDAO();
    private ObservableList<ExamPaper> paperList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadPapers();
    }

    private void loadPapers() {
        paperList.clear();
        paperList.addAll(paperDAO.getAllExamPapers());
        paperListView.setItems(paperList);
    }

    @FXML
    private void handleCreatePaper() {
        String name = paperNameField.getText();
        String desc = paperDescriptionField.getText();

        if (name == null || name.trim().isEmpty()) {
            messageLabel.setText("Paper name cannot be empty!");
            return;
        }

        ExamPaper newPaper = new ExamPaper(0, name, desc);
        int id = paperDAO.createExamPaper(newPaper);

        if (id != -1) {
            messageLabel.setText("Paper Created successfully!");
            paperNameField.clear();
            paperDescriptionField.clear();
            loadPapers();
        } else {
            messageLabel.setText("Failed to create paper.");
        }
    }

    @FXML
    private void handleManageQuestions(ActionEvent event) throws Exception {
        ExamPaper selected = paperListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setText("Please select a paper first!");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/manage_questions.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600); // Widened scene

        ManageQuestionController controller = loader.getController();
        controller.setExamPaper(selected);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void handleDeletePaper() {
        ExamPaper selected = paperListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setText("Please select a paper to delete!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Paper");
        alert.setHeaderText("Are you sure you want to delete this paper?");
        alert.setContentText("This will also delete ALL questions and results associated with it!");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (paperDAO.deleteExamPaper(selected.getPaperId())) {
                messageLabel.setText("Paper deleted successfully!");
                loadPapers();
            } else {
                messageLabel.setText("Failed to delete paper.");
            }
        }
    }

    @FXML
    private void handleBack(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin_dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load(), 400, 300));
    }
}
