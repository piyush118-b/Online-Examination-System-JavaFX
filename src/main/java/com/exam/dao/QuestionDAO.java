package com.exam.dao;

import com.exam.config.DBConnection;
import com.exam.model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {
    private List<String> getImagesForQuestion(int questionId) {

        List<String> images = new ArrayList<>();
        String query = "SELECT image_path FROM question_images WHERE question_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, questionId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                images.add(rs.getString("image_path"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return images;
    }

    // Add Question
    public int addQuestion(Question question) {

        String query = "INSERT INTO questions " +
                "(question_text, option_a, option_b, option_c, option_d, correct_option, marks, section, paper_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING question_id";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, question.getQuestionText());
            stmt.setString(2, question.getOptionA());
            stmt.setString(3, question.getOptionB());
            stmt.setString(4, question.getOptionC());
            stmt.setString(5, question.getOptionD());
            stmt.setString(6, question.getCorrectOption());
            stmt.setInt(7, question.getMarks());
            stmt.setString(8, question.getSection());
            stmt.setInt(9, question.getPaperId());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("question_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void addImageForQuestion(int questionId, String imagePath) {

        String query = "INSERT INTO question_images (question_id, image_path) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, questionId);
            stmt.setString(2, imagePath);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Fetch All Questions
    public List<Question> getAllQuestions() {

        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM questions";

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Question q = new Question(
                        rs.getInt("question_id"),
                        rs.getString("question_text"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d"),
                        rs.getString("correct_option"),
                        rs.getInt("marks"),
                        rs.getString("section"));
                q.setImagePaths(getImagesForQuestion(q.getQuestionId()));
                questions.add(q);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }

    public List<Question> getQuestionsByPaperId(int paperId) {

        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM questions WHERE paper_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, paperId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Question q = new Question(
                        rs.getInt("question_id"),
                        rs.getString("question_text"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d"),
                        rs.getString("correct_option"),
                        rs.getInt("marks"),
                        rs.getString("section"));
                q.setPaperId(rs.getInt("paper_id"));
                q.setImagePaths(getImagesForQuestion(q.getQuestionId()));
                questions.add(q);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }

    public boolean updateQuestion(Question question) {
        String query = "UPDATE questions SET question_text = ?, option_a = ?, option_b = ?, " +
                "option_c = ?, option_d = ?, correct_option = ?, marks = ?, section = ? " +
                "WHERE question_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, question.getQuestionText());
            stmt.setString(2, question.getOptionA());
            stmt.setString(3, question.getOptionB());
            stmt.setString(4, question.getOptionC());
            stmt.setString(5, question.getOptionD());
            stmt.setString(6, question.getCorrectOption());
            stmt.setInt(7, question.getMarks());
            stmt.setString(8, question.getSection());
            stmt.setInt(9, question.getQuestionId());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteQuestion(int questionId) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Delete answers for this question
                try (PreparedStatement stmt = conn
                        .prepareStatement("DELETE FROM student_answers WHERE question_id = ?")) {
                    stmt.setInt(1, questionId);
                    stmt.executeUpdate();
                }

                // Delete images for this question
                try (PreparedStatement stmt = conn
                        .prepareStatement("DELETE FROM question_images WHERE question_id = ?")) {
                    stmt.setInt(1, questionId);
                    stmt.executeUpdate();
                }

                // Delete the question
                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM questions WHERE question_id = ?")) {
                    stmt.setInt(1, questionId);
                    int affected = stmt.executeUpdate();
                    conn.commit();
                    return affected > 0;
                }
            } catch (SQLException ex) {
                conn.rollback();
                ex.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}