package com.exam.controller;

import com.exam.dao.QuestionDAO;
import com.exam.model.Question;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

public class ManageQuestionController {

    @FXML
    private TextArea questionText;

    @FXML
    private TextField optionA, optionB, optionC, optionD;

    @FXML
    private TextField correctOption, marks;

    @FXML
    private Label messageLabel;

    private QuestionDAO questionDAO = new QuestionDAO();

    @FXML
    private void handleSaveQuestion() {

        try {
            Question question = new Question();

            question.setQuestionText(questionText.getText());
            question.setOptionA(optionA.getText());
            question.setOptionB(optionB.getText());
            question.setOptionC(optionC.getText());
            question.setOptionD(optionD.getText());
            question.setCorrectOption(correctOption.getText().toUpperCase());
            question.setMarks(Integer.parseInt(marks.getText()));

            boolean success = questionDAO.addQuestion(question);

            if (success) {
                messageLabel.setText("Question Saved Successfully!");
                clearFields();
            } else {
                messageLabel.setText("Failed to Save Question!");
            }

        } catch (Exception e) {
            messageLabel.setText("Invalid Input!");
        }
    }

    private void clearFields() {
        questionText.clear();
        optionA.clear();
        optionB.clear();
        optionC.clear();
        optionD.clear();
        correctOption.clear();
        marks.clear();
    }

    @FXML
    private void handleBack(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/admin_dashboard.fxml")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load(), 400, 300));
    }
}