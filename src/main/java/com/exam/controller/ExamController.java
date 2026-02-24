package com.exam.controller;

import com.exam.dao.QuestionDAO;
import com.exam.dao.ResultDAO;
import com.exam.dao.StudentAnswerDAO;
import com.exam.model.Question;
import com.exam.model.Result;
import com.exam.model.User;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.ActionEvent;

import java.util.*;

public class ExamController {

    @FXML
    private void handleReviewAnswers(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/review_screen.fxml"));

        Scene scene = new Scene(loader.load(), 700, 600);

        ReviewController controller = loader.getController();
        controller.setResultId(currentResultId);

        Stage stage = new Stage();
        stage.setTitle("Review Your Exam");
        stage.setScene(scene);
        stage.show();
    }

    // ================= ENUM =================
    public enum QuestionState {
        NOT_VISITED,
        NOT_ANSWERED,
        ANSWERED,
        MARKED_FOR_REVIEW,
        ANSWERED_AND_MARKED_FOR_REVIEW
    }

    // ================= UI =================
    @FXML
    private Label questionLabel;
    @FXML
    private Label questionNumberLabel;
    @FXML
    private Label timerLabel;
    @FXML
    private Label resultLabel;

    @FXML
    private GridPane questionGrid;
    @FXML
    private VBox imageContainer;

    @FXML
    private RadioButton optionA, optionB, optionC, optionD;
    @FXML
    private Button prevButton, nextButton, submitButton, saveAndNextButton;

    @FXML
    private Label answeredCount;
    @FXML
    private Label notAnsweredCount;
    @FXML
    private Label notVisitedCount;
    @FXML
    private Label markedCount;
    @FXML
    private Label answeredMarkedCount;

    @FXML
    private HBox examContainer;
    @FXML
    private VBox resultContainer;

    private ToggleGroup optionsGroup;

    // ================= DATA =================
    private List<Question> aptitudeQuestions = new ArrayList<>();
    private List<Question> coreQuestions = new ArrayList<>();
    private List<Question> activeQuestions = new ArrayList<>();
    private List<Question> allQuestions = new ArrayList<>();

    private Map<Question, String> selectedAnswers = new HashMap<>();
    private Map<Question, QuestionState> questionStates = new HashMap<>();

    private int currentIndex = 0;
    private boolean examSubmitted = false;

    private QuestionDAO questionDAO = new QuestionDAO();
    private User currentUser;
    private int studentId;

    // ================= TIMER =================
    private int timeRemaining = 10800; // 3 hours (3 * 60 * 60)
    private Timeline timeline;

    private int paperId;
    private int currentResultId;

    // ================= INITIALIZE =================
    @FXML
    public void initialize() {
        optionsGroup = new ToggleGroup();
        optionA.setToggleGroup(optionsGroup);
        optionB.setToggleGroup(optionsGroup);
        optionC.setToggleGroup(optionsGroup);
        optionD.setToggleGroup(optionsGroup);
    }

    public void startExamForPaper(int paperId) {
        this.paperId = paperId;
        allQuestions = questionDAO.getQuestionsByPaperId(paperId);

        for (Question q : allQuestions) {
            String section = q.getSection();
            if (section == null)
                section = "CORE";

            section = section.trim().toUpperCase();

            if ("APTITUDE".equals(section)) {
                aptitudeQuestions.add(q);
            } else {
                coreQuestions.add(q);
            }
        }

        activeQuestions = aptitudeQuestions.isEmpty() ? coreQuestions : aptitudeQuestions;

        initializeStates();
        generateNavigator();
        if (!activeQuestions.isEmpty()) {
            loadQuestion(currentIndex);
            startTimer();
        } else {
            questionLabel.setText("No questions available for this paper.");
        }
    }

    private void initializeStates() {
        questionStates.clear();
        selectedAnswers.clear();
        currentIndex = 0;

        for (Question q : allQuestions) {
            questionStates.put(q, QuestionState.NOT_VISITED);
        }
    }

    // ================= SECTION SWITCH =================
    @FXML
    private void loadAptitudeSection() {
        activeQuestions = aptitudeQuestions;
        currentIndex = 0;
        generateNavigator();
        if (!activeQuestions.isEmpty()) {
            loadQuestion(currentIndex);
        }
    }

    @FXML
    private void loadCoreSection() {
        activeQuestions = coreQuestions;
        currentIndex = 0;
        generateNavigator();
        if (!activeQuestions.isEmpty()) {
            loadQuestion(currentIndex);
        }
    }

    // ================= TIMER =================
    private void startTimer() {

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    timeRemaining--;

                    int hours = timeRemaining / 3600;
                    int minutes = (timeRemaining % 3600) / 60;
                    int seconds = timeRemaining % 60;

                    timerLabel.setText(String.format("Time Left: %02d:%02d:%02d", hours, minutes, seconds));

                    if (timeRemaining <= 0) {
                        timeline.stop();
                        handleSubmit();
                    }
                }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    // ================= LOAD QUESTION =================
    private void loadQuestion(int index) {
        Question q = activeQuestions.get(index);

        if (questionStates.get(q) == QuestionState.NOT_VISITED) {
            questionStates.put(q, QuestionState.NOT_ANSWERED);
        }

        questionNumberLabel.setText(
                "Section: " + q.getSection() +
                        " | Question " + (index + 1) +
                        " / " + activeQuestions.size());

        questionLabel.setText(q.getQuestionText());

        optionA.setText("A. " + q.getOptionA());
        optionB.setText("B. " + q.getOptionB());
        optionC.setText("C. " + q.getOptionC());
        optionD.setText("D. " + q.getOptionD());

        optionsGroup.selectToggle(null);

        if (selectedAnswers.containsKey(q)) {
            selectOption(selectedAnswers.get(q));
        }

        imageContainer.getChildren().clear();
        if (q.getImagePaths() != null) {
            for (String path : q.getImagePaths()) {
                Image img = new Image(
                        getClass().getResource("/" + path).toExternalForm());
                ImageView iv = new ImageView(img);
                iv.setFitWidth(300);
                iv.setPreserveRatio(true);
                imageContainer.getChildren().add(iv);
            }
        }

        prevButton.setDisable(index == 0);
        nextButton.setDisable(index == activeQuestions.size() - 1);

        updateNavigator();
        updateLegendCounts();
    }

    private void selectOption(String answer) {
        switch (answer) {
            case "A":
                optionsGroup.selectToggle(optionA);
                break;
            case "B":
                optionsGroup.selectToggle(optionB);
                break;
            case "C":
                optionsGroup.selectToggle(optionC);
                break;
            case "D":
                optionsGroup.selectToggle(optionD);
                break;
        }
    }

    // ================= NAVIGATOR =================
    private void generateNavigator() {

        questionGrid.getChildren().clear();
        int columns = 4;

        for (int i = 0; i < activeQuestions.size(); i++) {

            int index = i;
            Button btn = new Button(String.valueOf(i + 1));
            btn.setPrefSize(40, 40);

            btn.setOnAction(e -> {
                currentIndex = index;
                loadQuestion(currentIndex);
            });

            questionGrid.add(btn, i % columns, i / columns);
        }
    }

    private void updateNavigator() {

        for (Node node : questionGrid.getChildren()) {

            if (node instanceof Button) {

                Button btn = (Button) node;
                int index = Integer.parseInt(btn.getText()) - 1;
                Question q = activeQuestions.get(index);
                QuestionState state = questionStates.get(q);

                btn.setStyle("");

                switch (state) {
                    case NOT_VISITED:
                        btn.setStyle("-fx-background-color: lightgray;");
                        break;
                    case NOT_ANSWERED:
                        btn.setStyle("-fx-background-color: orange;");
                        break;
                    case ANSWERED:
                        btn.setStyle("-fx-background-color: lightgreen;");
                        break;
                    case MARKED_FOR_REVIEW:
                    case ANSWERED_AND_MARKED_FOR_REVIEW:
                        btn.setStyle("-fx-background-color: purple; -fx-text-fill: white;");
                        break;
                }

                if (index == currentIndex) {
                    btn.setStyle(btn.getStyle() + "-fx-border-color: black; -fx-border-width: 2;");
                }
            }
        }
    }

    // ================= SAVE ANSWER =================
    private void saveAnswer(boolean markForReview) {
        if (activeQuestions.isEmpty())
            return;
        Question q = activeQuestions.get(currentIndex);
        Toggle selected = optionsGroup.getSelectedToggle();

        if (selected != null) {
            RadioButton rb = (RadioButton) selected;
            String ans = rb.getText().substring(0, 1);
            selectedAnswers.put(q, ans);

            if (markForReview) {
                questionStates.put(q, QuestionState.ANSWERED_AND_MARKED_FOR_REVIEW);
            } else {
                questionStates.put(q, QuestionState.ANSWERED);
            }
        } else {
            selectedAnswers.remove(q);
            if (markForReview) {
                questionStates.put(q, QuestionState.MARKED_FOR_REVIEW);
            } else {
                questionStates.put(q, QuestionState.NOT_ANSWERED);
            }
        }

        updateNavigator();
        updateLegendCounts();
    }

    // ================= LEGEND =================
    private void updateLegendCounts() {

        long answered = questionStates.values().stream()
                .filter(s -> s == QuestionState.ANSWERED)
                .count();

        long notAnswered = questionStates.values().stream()
                .filter(s -> s == QuestionState.NOT_ANSWERED)
                .count();

        long notVisited = questionStates.values().stream()
                .filter(s -> s == QuestionState.NOT_VISITED)
                .count();

        long markedForReview = questionStates.values().stream()
                .filter(s -> s == QuestionState.MARKED_FOR_REVIEW)
                .count();

        long answeredAndMarked = questionStates.values().stream()
                .filter(s -> s == QuestionState.ANSWERED_AND_MARKED_FOR_REVIEW)
                .count();

        answeredCount.setText(String.valueOf(answered));
        notAnsweredCount.setText(String.valueOf(notAnswered));
        notVisitedCount.setText(String.valueOf(notVisited));
        markedCount.setText(String.valueOf(markedForReview));
        answeredMarkedCount.setText(String.valueOf(answeredAndMarked));
    }

    // ================= SAVE & NAVIGATION =================
    @FXML
    private void handleSaveAndNext() {
        if (optionsGroup.getSelectedToggle() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Please select an answer to save! Use 'Clear Response' if you wish to wipe.");
            alert.showAndWait();
            return;
        }

        Question q = activeQuestions.get(currentIndex);
        boolean wasReview = (questionStates.get(q) == QuestionState.MARKED_FOR_REVIEW ||
                questionStates.get(q) == QuestionState.ANSWERED_AND_MARKED_FOR_REVIEW);
        saveAnswer(wasReview);

        if (currentIndex < activeQuestions.size() - 1) {
            currentIndex++;
            loadQuestion(currentIndex);
        }
    }

    @FXML
    private void handleNext() {
        Question q = activeQuestions.get(currentIndex);
        if (optionsGroup.getSelectedToggle() == null && questionStates.get(q) == QuestionState.NOT_VISITED) {
            questionStates.put(q, QuestionState.NOT_ANSWERED);
            updateNavigator();
            updateLegendCounts();
        }

        if (currentIndex < activeQuestions.size() - 1) {
            currentIndex++;
            loadQuestion(currentIndex);
        }
    }

    @FXML
    private void handlePrevious() {
        if (currentIndex > 0) {
            currentIndex--;
            loadQuestion(currentIndex);
        }
    }

    // ================= SUBMIT =================
    @FXML
    private void handleSubmit() {

        if (examSubmitted)
            return;

        int score = 0;
        int totalMarks = 0;

        for (Question q : allQuestions) {
            totalMarks += q.getMarks();

            String selected = selectedAnswers.get(q);
            String correct = q.getCorrectOption();

            if (selected != null && selected.equalsIgnoreCase(correct)) {
                score += q.getMarks();
            }
        }

        double percentage = totalMarks == 0 ? 0 : ((double) score / totalMarks) * 100;

        resultLabel.setText(
                "Score: " + score + " / " + totalMarks +
                        "\nPercentage: " + String.format("%.2f", percentage) + "%");

        int resultId = new ResultDAO().saveResult(new Result(studentId, paperId, score, totalMarks));
        this.currentResultId = resultId;

        if (resultId != -1) {
            for (Question q : allQuestions) {
                String selected = selectedAnswers.get(q);
                String correct = q.getCorrectOption();
                boolean isCorrect = false;

                if (selected != null && selected.equalsIgnoreCase(correct)) {
                    isCorrect = true;
                }

                new StudentAnswerDAO().saveStudentAnswer(
                        studentId,
                        resultId,
                        q.getQuestionId(),
                        selected,
                        correct,
                        isCorrect);
            }
        }

        examContainer.setVisible(false);
        resultContainer.setVisible(true);

        examSubmitted = true;
    }

    public void setUser(User user) {
        this.currentUser = user;
        this.studentId = user.getUserId();
    }

    @FXML
    private void handleMarkForReview() {
        saveAnswer(true);
        if (currentIndex < activeQuestions.size() - 1) {
            currentIndex++;
            loadQuestion(currentIndex);
        } else {
            updateNavigator();
            updateLegendCounts();
        }
    }

    @FXML
    private void handleClearResponse() {
        optionsGroup.selectToggle(null);
        saveAnswer(false);
    }

    @FXML
    private void handleReturnToDashboard(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/student_dashboard.fxml"));

        Scene scene = new Scene(loader.load(), 400, 300);

        StudentController controller = loader.getController();
        if (currentUser != null) {
            controller.setUser(currentUser);
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

}
