package com.exam.dao;

import com.exam.config.DBConnection;
import com.exam.model.Result;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ResultDAO {

    public boolean saveResult(Result result) {

        String query = "INSERT INTO results (student_id, score, total_marks) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, result.getStudentId());
            stmt.setInt(2, result.getScore());
            stmt.setInt(3, result.getTotalMarks());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}