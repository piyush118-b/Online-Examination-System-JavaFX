package com.exam.controller;

import com.exam.dao.ReviewDAO;
import com.exam.model.ReviewItem;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class ReviewController {

    @FXML
    private VBox reviewContainer;

    private int studentId;

    public void setStudentId(int studentId) {
        this.studentId = studentId;
        loadReview();
    }

    private void loadReview() {

        ReviewDAO reviewDAO = new ReviewDAO();
        List<ReviewItem> answers = reviewDAO.getReviewData(studentId);

        reviewContainer.getChildren().clear();

        int questionNumber = 1;

        for (ReviewItem item : answers) {

            VBox box = new VBox(5);
            box.setStyle("-fx-padding: 10; -fx-border-color: lightgray;");

            Label qLabel = new Label(
                    "Q" + questionNumber + ": " + item.getQuestionText()
            );

            String selectedText = item.getSelectedOption() == null
                    ? "Not Attempted"
                    : item.getSelectedOption();

            Label selected = new Label("Your Answer: " + selectedText);
            Label correct = new Label("Correct Answer: " + item.getCorrectOption());

            if (item.isCorrect()) {
                selected.setStyle("-fx-text-fill: green;");
            } else {
                selected.setStyle("-fx-text-fill: red;");
            }

            box.getChildren().addAll(qLabel, selected, correct);
            reviewContainer.getChildren().add(box);

            questionNumber++;
        }
    }
}
