package com.exam.dao;

import com.exam.config.DBConnection;
import com.exam.model.ReviewItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    public List<ReviewItem> getReviewData(int studentId) {

        List<ReviewItem> list = new ArrayList<>();

        String query =
                "SELECT q.question_text, s.selected_option, s.correct_option, s.is_correct " +
                        "FROM student_answers s " +
                        "JOIN questions q ON s.question_id = q.question_id " +
                        "WHERE s.student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                ReviewItem item = new ReviewItem(
                        rs.getString("question_text"),
                        rs.getString("selected_option"),
                        rs.getString("correct_option"),
                        rs.getBoolean("is_correct")
                );

                list.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
