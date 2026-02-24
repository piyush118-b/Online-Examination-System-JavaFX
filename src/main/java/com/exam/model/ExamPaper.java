package com.exam.model;

public class ExamPaper {
    private int paperId;
    private String name;
    private String description;

    public ExamPaper() {
    }

    public ExamPaper(int paperId, String name, String description) {
        this.paperId = paperId;
        this.name = name;
        this.description = description;
    }

    public int getPaperId() {
        return paperId;
    }

    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}
