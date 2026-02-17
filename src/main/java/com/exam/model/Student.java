package com.exam.model;

public class Student extends User {

    public Student(int userId, String username, String password) {
        super(userId, username, password, "STUDENT");
    }

    public Student(String username, String password) {
        super(username, password, "STUDENT");
    }

    public void takeExam() {
        System.out.println("Student starting exam...");
    }
}