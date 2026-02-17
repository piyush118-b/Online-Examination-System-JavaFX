package com.exam.controller;

import com.exam.dao.QuestionDAO;
import com.exam.dao.ResultDAO;
import com.exam.model.Question;
import com.exam.model.Result;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.ActionEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamController {

    // UI Components
    @FXML private Label questionLabel;
    @FXML private Label resultLabel;
    @FXML private Label timerLabel;

    @FXML private RadioButton optionA, optionB, optionC, optionD;
    @FXML private Button prevButton, nextButton, submitButton, returnButton;

    private ToggleGroup optionsGroup;

    // Exam State
    private List<Question> questions;
    private int currentIndex = 0;
    private boolean examSubmitted = false;

    private Map<Integer, String> selectedAnswers = new HashMap<>();

    private QuestionDAO questionDAO = new QuestionDAO();
    private int studentId;

    // Timer
    private int timeRemaining = 60;
    private Timeline timeline;

    // ================= INITIALIZE =================
    @FXML
    public void initialize() {

        optionsGroup = new ToggleGroup();
        optionA.setToggleGroup(optionsGroup);
        optionB.setToggleGroup(optionsGroup);
        optionC.setToggleGroup(optionsGroup);
        optionD.setToggleGroup(optionsGroup);

        submitButton.setVisible(false);
        returnButton.setVisible(false);

        questions = questionDAO.getAllQuestions();

        if (!questions.isEmpty()) {
            loadQuestion(currentIndex);
        } else {
            questionLabel.setText("No questions found in database.");
        }

        startTimer();
    }

    // ================= TIMER =================
    private void startTimer() {

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {

                    timeRemaining--;
                    timerLabel.setText("Time Left: " + timeRemaining + "s");

                    if (timeRemaining <= 0) {
                        timeline.stop();
                        autoSubmit();
                    }
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void autoSubmit() {
        resultLabel.setText("Time's up! Auto-submitting...\n");
        handleSubmit();
    }

    // ================= LOAD QUESTION =================
    private void loadQuestion(int index) {

        Question q = questions.get(index);

        questionLabel.setText(q.getQuestionText());

        optionA.setText("A. " + q.getOptionA());
        optionB.setText("B. " + q.getOptionB());
        optionC.setText("C. " + q.getOptionC());
        optionD.setText("D. " + q.getOptionD());

        optionsGroup.selectToggle(null);

        if (selectedAnswers.containsKey(index)) {
            selectOption(selectedAnswers.get(index));
        }

        prevButton.setDisable(index == 0);
        nextButton.setDisable(index == questions.size() - 1);
        submitButton.setVisible(index == questions.size() - 1);
    }

    private void selectOption(String answer) {
        switch (answer) {
            case "A": optionsGroup.selectToggle(optionA); break;
            case "B": optionsGroup.selectToggle(optionB); break;
            case "C": optionsGroup.selectToggle(optionC); break;
            case "D": optionsGroup.selectToggle(optionD); break;
        }
    }

    // ================= NAVIGATION =================
    @FXML
    private void handleNext() {
        saveAnswer();
        if (currentIndex < questions.size() - 1) {
            currentIndex++;
            loadQuestion(currentIndex);
        }
    }

    @FXML
    private void handlePrevious() {
        saveAnswer();
        if (currentIndex > 0) {
            currentIndex--;
            loadQuestion(currentIndex);
        }
    }

    private void saveAnswer() {
        Toggle selected = optionsGroup.getSelectedToggle();
        if (selected != null) {
            RadioButton rb = (RadioButton) selected;
            String answer = rb.getText().substring(0, 1);
            selectedAnswers.put(currentIndex, answer);
        }
    }

    // ================= SUBMIT =================
    @FXML
    private void handleSubmit() {

        if (examSubmitted) return;

        if (timeline != null) {
            timeline.stop();
        }

        saveAnswer();

        int score = 0;
        int totalMarks = 0;
        int correctCount = 0;
        int wrongCount = 0;

        for (int i = 0; i < questions.size(); i++) {

            totalMarks += questions.get(i).getMarks();

            String selected = selectedAnswers.get(i);

            if (selected != null &&
                    selected.equalsIgnoreCase(questions.get(i).getCorrectOption())) {

                score += questions.get(i).getMarks();
                correctCount++;

            } else {
                wrongCount++;
            }
        }

        int totalQuestions = questions.size();
        double percentage = ((double) score / totalMarks) * 100;
        String status = percentage >= 40 ? "PASS" : "FAIL";

        resultLabel.setText(
                "===== EXAM RESULT =====\n\n" +
                        "Total Questions: " + totalQuestions + "\n" +
                        "Correct Answers: " + correctCount + "\n" +
                        "Wrong Answers: " + wrongCount + "\n\n" +
                        "Score: " + score + " / " + totalMarks + "\n" +
                        "Percentage: " + String.format("%.2f", percentage) + "%\n" +
                        "Result: " + status
        );

        Result result = new Result(studentId, score, totalMarks);
        new ResultDAO().saveResult(result);

        examSubmitted = true;

        prevButton.setDisable(true);
        nextButton.setDisable(true);
        submitButton.setDisable(true);

        optionA.setDisable(true);
        optionB.setDisable(true);
        optionC.setDisable(true);
        optionD.setDisable(true);

        returnButton.setVisible(true);
    }

    // ================= RETURN =================
    @FXML
    private void handleReturnToDashboard(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/student_dashboard.fxml")
        );

        Scene scene = new Scene(loader.load(), 400, 300);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    // ================= SET STUDENT =================
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}