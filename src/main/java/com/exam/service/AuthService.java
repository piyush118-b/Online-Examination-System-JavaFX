package com.exam.service;

import com.exam.dao.UserDAO;
import com.exam.model.User;

public class AuthService {

    private UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    // 🔹 Login Logic
    public User login(String username, String password) {

        User user = userDAO.validateLogin(username, password);

        if (user != null) {
            return user;
        }

        return null;
    }

    // 🔹 Register Logic
    public boolean register(String username, String password, String role) {

        // Optional: Check if user already exists
        User existingUser = userDAO.getUserByUsername(username);

        if (existingUser != null) {
            return false; // Username already taken
        }

        User newUser = new User(username, password, role);
        return userDAO.addUser(newUser);
    }
}