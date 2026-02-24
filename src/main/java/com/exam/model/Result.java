package com.exam.model;

import java.time.LocalDateTime;

public class Result {

    private int resultId;
    private int studentId;
    private int paperId;
    private int score;
    private int totalMarks;
    private LocalDateTime examDate;

    // For joining and displaying
    private String paperName;

    public Result(int studentId, int paperId, int score, int totalMarks) {
        this.studentId = studentId;
        this.paperId = paperId;
        this.score = score;
        this.totalMarks = totalMarks;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getPaperId() {
        return paperId;
    }

    public int getScore() {
        return score;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public LocalDateTime getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDateTime examDate) {
        this.examDate = examDate;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }
}