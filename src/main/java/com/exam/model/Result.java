package com.exam.model;

import java.time.LocalDateTime;

public class Result {

    private int resultId;
    private int studentId;
    private int score;
    private int totalMarks;
    private LocalDateTime examDate;

    public Result(int studentId, int score, int totalMarks) {
        this.studentId = studentId;
        this.score = score;
        this.totalMarks = totalMarks;
    }

    public int getStudentId() { return studentId; }
    public int getScore() { return score; }
    public int getTotalMarks() { return totalMarks; }
}