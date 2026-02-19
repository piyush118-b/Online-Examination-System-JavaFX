package com.exam.dao;

import com.exam.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class StudentAnswerDAO {

    public void saveStudentAnswer(int studentId,
                                  int questionId,
                                  String selectedOption,
                                  String correctOption,
                                  boolean isCorrect) {

        String query = "INSERT INTO student_answers " +
                "(student_id, question_id, selected_option, correct_option, is_correct) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, questionId);
            stmt.setString(3, selectedOption);
            stmt.setString(4, correctOption);
            stmt.setBoolean(5, isCorrect);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
