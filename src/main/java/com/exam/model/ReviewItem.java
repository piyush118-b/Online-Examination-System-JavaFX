package com.exam.model;

public class ReviewItem {

    private String questionText;
    private String selectedOption;
    private String correctOption;
    private boolean isCorrect;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    public ReviewItem(String questionText,
            String selectedOption,
            String correctOption,
            boolean isCorrect,
            String optionA,
            String optionB,
            String optionC,
            String optionD) {

        this.questionText = questionText;
        this.selectedOption = selectedOption;
        this.correctOption = correctOption;
        this.isCorrect = isCorrect;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }
}
