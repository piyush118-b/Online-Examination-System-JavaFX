package com.exam.controller;

import com.exam.dao.QuestionDAO;
import com.exam.model.Question;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.File;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class ManageQuestionController {

    // ================= UI FIELDS =================

    @FXML private TextArea questionText;
    @FXML private TextField optionA;
    @FXML private TextField optionB;
    @FXML private TextField optionC;
    @FXML private TextField optionD;
    @FXML private TextField correctOption;
    @FXML private TextField marks;
    @FXML private Label messageLabel;
    @FXML private ComboBox<String> sectionBox;

    @FXML private VBox selectedImagesBox;

    // ================= DATA =================

    private List<File> selectedImageFiles = new ArrayList<>();
    private QuestionDAO questionDAO = new QuestionDAO();

    // ================= ADD IMAGE =================

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

    // ================= SAVE QUESTION =================

    @FXML
    private void handleSaveQuestion() {

        try {

            // -------- VALIDATION --------

            if (questionText.getText().isEmpty() ||
                    optionA.getText().isEmpty() ||
                    optionB.getText().isEmpty() ||
                    optionC.getText().isEmpty() ||
                    optionD.getText().isEmpty() ||
                    correctOption.getText().isEmpty() ||
                    marks.getText().isEmpty()) {

                messageLabel.setText("Please fill all fields!");
                return;
            }

            String correct = correctOption.getText().toUpperCase();

            if (!correct.matches("[ABCD]")) {
                messageLabel.setText("Correct option must be A, B, C, or D.");
                return;
            }

            int questionMarks = Integer.parseInt(marks.getText());

            // -------- CREATE QUESTION OBJECT --------

            Question question = new Question();
            question.setQuestionText(questionText.getText());
            question.setOptionA(optionA.getText());
            question.setOptionB(optionB.getText());
            question.setOptionC(optionC.getText());
            question.setOptionD(optionD.getText());
            question.setCorrectOption(correct);
            question.setMarks(questionMarks);
            if (sectionBox.getValue() == null) {
                messageLabel.setText("Please select section!");
                return;
            }

            question.setSection(sectionBox.getValue());
            // -------- INSERT QUESTION & GET ID --------

            int questionId = questionDAO.addQuestion(question);

            if (questionId == -1) {
                messageLabel.setText("Failed to add question.");
                return;
            }

            // -------- SAVE IMAGES --------

            for (File file : selectedImageFiles) {

                String fileName = file.getName();

                // Copy to resources/images
                Path destination = Paths.get(
                        "src/main/resources/images/" + fileName
                );

                Files.copy(
                        file.toPath(),
                        destination,
                        StandardCopyOption.REPLACE_EXISTING
                );

                // Save image path in DB
                questionDAO.addImageForQuestion(
                        questionId,
                        "images/" + fileName
                );
            }

            messageLabel.setText("Question added successfully!");
            clearFields();

        } catch (NumberFormatException e) {
            messageLabel.setText("Marks must be a valid number.");
        } catch (Exception e) {
            messageLabel.setText("Error occurred while saving.");
            e.printStackTrace();
        }
    }

    // ================= CLEAR FIELDS =================

    private void clearFields() {

        questionText.clear();
        optionA.clear();
        optionB.clear();
        optionC.clear();
        optionD.clear();
        correctOption.clear();
        marks.clear();

        selectedImageFiles.clear();
        selectedImagesBox.getChildren().clear();
    }

    // ================= BACK TO DASHBOARD =================

    @FXML
    private void handleBack(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/admin_dashboard.fxml")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load(), 400, 300));
    }

    @FXML
    public void initialize() {
        System.out.println("ManageQuestionController initialized");

        sectionBox.getItems().addAll("APTITUDE", "CORE");
        sectionBox.setValue("CORE");
    }


}
