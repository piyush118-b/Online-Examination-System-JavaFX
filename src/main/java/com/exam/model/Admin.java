package com.exam.model;

public class Admin extends User {

    public Admin(int userId, String username, String password) {
        super(userId, username, password, "ADMIN");
    }

    public Admin(String username, String password) {
        super(username, password, "ADMIN");
    }

    public void manageQuestions() {
        System.out.println("Admin managing questions...");
    }
}