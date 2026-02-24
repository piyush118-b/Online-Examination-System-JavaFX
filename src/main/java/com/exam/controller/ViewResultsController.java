package com.exam.controller;

import com.exam.dao.ResultDAO;
import com.exam.model.Result;
import com.exam.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ViewResultsController {

    @FXML
    private Label studentInfoLabel;
    @FXML
    private TableView<Result> resultsTable;
    @FXML
    private TableColumn<Result, String> paperNameCol;
    @FXML
    private TableColumn<Result, Integer> scoreCol;
    @FXML
    private TableColumn<Result, Integer> totalMarksCol;
    @FXML
    private TableColumn<Result, String> valDateCol;

    private User currentUser;
    private ResultDAO resultDAO = new ResultDAO();

    @FXML
    public void initialize() {
        paperNameCol.setCellValueFactory(new PropertyValueFactory<>("paperName"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        totalMarksCol.setCellValueFactory(new PropertyValueFactory<>("totalMarks"));

        // Custom formatting for ExamDate
        valDateCol.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getExamDate();
            String formattedDate = "";
            if (date != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                formattedDate = date.format(formatter);
            }
            return new javafx.beans.property.SimpleStringProperty(formattedDate);
        });
    }

    public void initData(User user) {
        this.currentUser = user;
        studentInfoLabel.setText("Results for: " + user.getUsername());

        ObservableList<Result> resultsList = FXCollections.observableArrayList();
        resultsList.addAll(resultDAO.getResultsByStudent(user.getUserId()));
        resultsTable.setItems(resultsList);
    }

    @FXML
    private void handleBack(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/student_dashboard.fxml"));
        Scene scene = new Scene(loader.load(), 400, 400); // dashboard height

        StudentController controller = loader.getController();
        controller.setUser(currentUser);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void handleReviewAttempt(ActionEvent event) throws Exception {
        Result selected = resultsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            // maybe an alert or label
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/review_screen.fxml"));
        Scene scene = new Scene(loader.load(), 700, 600);

        ReviewController controller = loader.getController();
        controller.setResultId(selected.getResultId());

        Stage stage = new Stage();
        stage.setTitle("Review Attempt");
        stage.setScene(scene);
        stage.show();
    }
}
