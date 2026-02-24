package com.exam.dao;

import com.exam.config.DBConnection;
import com.exam.model.ReviewItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    public List<ReviewItem> getReviewData(int resultId) {

        List<ReviewItem> list = new ArrayList<>();

        String query = "SELECT q.question_text, s.selected_option, s.correct_option, s.is_correct, " +
                "q.option_a, q.option_b, q.option_c, q.option_d " +
                "FROM student_answers s " +
                "JOIN questions q ON s.question_id = q.question_id " +
                "WHERE s.result_id = ? " +
                "ORDER BY q.question_id ASC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, resultId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                ReviewItem item = new ReviewItem(
                        rs.getString("question_text"),
                        rs.getString("selected_option"),
                        rs.getString("correct_option"),
                        rs.getBoolean("is_correct"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d"));

                list.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
