package com.exam.dao;

import com.exam.config.DBConnection;

import java.sql.*;

public class ExamDAO {

    public int createAttempt(int studentId) {

        String query = "INSERT INTO exam_attempts (student_id) " +
                "VALUES (?) RETURNING attempt_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, studentId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("attempt_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1; // failed
    }
}
