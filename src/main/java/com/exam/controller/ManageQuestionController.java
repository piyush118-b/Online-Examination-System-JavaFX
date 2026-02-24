package com.exam.controller;

import com.exam.dao.QuestionDAO;
import com.exam.model.ExamPaper;
import com.exam.model.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class ManageQuestionController {

    @FXML
    private Label contextLabel;
    @FXML
    private ListView<Question> questionListView;
    @FXML
    private Label formTitleLabel;

    @FXML
    private TextArea questionText;
    @FXML
    private TextField optionA;
    @FXML
    private TextField optionB;
    @FXML
    private TextField optionC;
    @FXML
    private TextField optionD;
    @FXML
    private TextField correctOption;
    @FXML
    private TextField marks;
    @FXML
    private Label messageLabel;
    @FXML
    private ComboBox<String> sectionBox;
    @FXML
    private VBox selectedImagesBox;

    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;

    private List<File> selectedImageFiles = new ArrayList<>();
    private QuestionDAO questionDAO = new QuestionDAO();

    private ExamPaper currentPaper;
    private Question selectedQuestion;
    private ObservableList<Question> questionList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        sectionBox.getItems().addAll("APTITUDE", "CORE");
        sectionBox.setValue("CORE");

        questionListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                populateForm(newVal);
            }
        });
    }

    public void setExamPaper(ExamPaper paper) {
        this.currentPaper = paper;
        contextLabel.setText("Questions for: " + paper.getName());
        loadQuestions();
    }

    private void loadQuestions() {
        if (currentPaper == null)
            return;
        questionList.clear();
        questionList.addAll(questionDAO.getQuestionsByPaperId(currentPaper.getPaperId()));
        questionListView.setItems(questionList);
    }

    private void populateForm(Question question) {
        this.selectedQuestion = question;
        formTitleLabel.setText("Edit Question");

        questionText.setText(question.getQuestionText());
        optionA.setText(question.getOptionA());
        optionB.setText(question.getOptionB());
        optionC.setText(question.getOptionC());
        optionD.setText(question.getOptionD());
        correctOption.setText(question.getCorrectOption());
        marks.setText(String.valueOf(question.getMarks()));
        sectionBox.setValue(question.getSection() == null ? "CORE" : question.getSection());

        selectedImagesBox.getChildren().clear();
        selectedImageFiles.clear();
        // Since original logic only supports adding images per session, loading
        // existing images visually can be complex if stored loosely, so we'll just
        // clear it for now.

        updateButton.setDisable(false);
        deleteButton.setDisable(false);
    }

    @FXML
    private void handleClearSelection() {
        this.selectedQuestion = null;
        formTitleLabel.setText("Add New Question");
        questionListView.getSelectionModel().clearSelection();

        questionText.clear();
        optionA.clear();
        optionB.clear();
        optionC.clear();
        optionD.clear();
        correctOption.clear();
        marks.clear();

        selectedImagesBox.getChildren().clear();
        selectedImageFiles.clear();

        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        messageLabel.setText("");
    }

    @FXML
    private void handleAddImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

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
    private void handleSaveQuestion() {
        if (currentPaper == null) {
            messageLabel.setText("No paper selected!");
            return;
        }

        if (!validateFields())
            return;

        Question question = new Question();
        question.setQuestionText(questionText.getText());
        question.setOptionA(optionA.getText());
        question.setOptionB(optionB.getText());
        question.setOptionC(optionC.getText());
        question.setOptionD(optionD.getText());
        question.setCorrectOption(correctOption.getText().toUpperCase());
        question.setMarks(Integer.parseInt(marks.getText()));
        question.setSection(sectionBox.getValue());
        question.setPaperId(currentPaper.getPaperId());

        int questionId = questionDAO.addQuestion(question);
        if (questionId == -1) {
            messageLabel.setText("Failed to add question.");
            return;
        }

        saveImages(questionId);

        messageLabel.setText("Question added successfully!");
        handleClearSelection();
        loadQuestions();
    }

    @FXML
    private void handleUpdateQuestion() {
        if (selectedQuestion == null)
            return;
        if (!validateFields())
            return;

        selectedQuestion.setQuestionText(questionText.getText());
        selectedQuestion.setOptionA(optionA.getText());
        selectedQuestion.setOptionB(optionB.getText());
        selectedQuestion.setOptionC(optionC.getText());
        selectedQuestion.setOptionD(optionD.getText());
        selectedQuestion.setCorrectOption(correctOption.getText().toUpperCase());
        selectedQuestion.setMarks(Integer.parseInt(marks.getText()));
        selectedQuestion.setSection(sectionBox.getValue());

        if (questionDAO.updateQuestion(selectedQuestion)) {
            // we could save additional images here if we want
            saveImages(selectedQuestion.getQuestionId());
            messageLabel.setText("Question updated successfully!");
            loadQuestions();
        } else {
            messageLabel.setText("Failed to update question.");
        }
    }

    @FXML
    private void handleDeleteQuestion() {
        if (selectedQuestion == null)
            return;

        if (questionDAO.deleteQuestion(selectedQuestion.getQuestionId())) {
            messageLabel.setText("Question deleted successfully!");
            handleClearSelection();
            loadQuestions();
        } else {
            messageLabel.setText("Failed to delete question.");
        }
    }

    private boolean validateFields() {
        if (questionText.getText().isEmpty() || optionA.getText().isEmpty() ||
                optionB.getText().isEmpty() || optionC.getText().isEmpty() ||
                optionD.getText().isEmpty() || correctOption.getText().isEmpty() ||
                marks.getText().isEmpty() || sectionBox.getValue() == null) {

            messageLabel.setText("Please fill all fields!");
            return false;
        }

        if (!correctOption.getText().toUpperCase().matches("[ABCD]")) {
            messageLabel.setText("Correct option must be A, B, C, or D.");
            return false;
        }

        try {
            Integer.parseInt(marks.getText());
        } catch (NumberFormatException e) {
            messageLabel.setText("Marks must be a valid number.");
            return false;
        }
        return true;
    }

    private void saveImages(int questionId) {
        try {
            for (File file : selectedImageFiles) {
                String fileName = file.getName();
                Path destination = Paths.get("src/main/resources/images/" + fileName);

                // create dirs if they don't exist
                if (!Files.exists(destination.getParent())) {
                    Files.createDirectories(destination.getParent());
                }

                Files.copy(file.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
                questionDAO.addImageForQuestion(questionId, "images/" + fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/manage_papers.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load(), 500, 500));
    }
}
