package com.exam.dao;

import com.exam.config.DBConnection;
import com.exam.model.ExamPaper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExamPaperDAO {

    public int createExamPaper(ExamPaper paper) {
        String query = "INSERT INTO exam_papers (paper_name, description) VALUES (?, ?) RETURNING paper_id";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, paper.getName());
            stmt.setString(2, paper.getDescription());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("paper_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<ExamPaper> getAllExamPapers() {
        List<ExamPaper> papers = new ArrayList<>();
        String query = "SELECT * FROM exam_papers";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                papers.add(new ExamPaper(
                        rs.getInt("paper_id"),
                        rs.getString("paper_name"),
                        rs.getString("description")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return papers;
    }

    public boolean deleteExamPaper(int paperId) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // 1. Delete student_answers related to questions on this paper
                String delAnswers = "DELETE FROM student_answers WHERE question_id IN (SELECT question_id FROM questions WHERE paper_id = ?)";
                try (PreparedStatement stmt = conn.prepareStatement(delAnswers)) {
                    stmt.setInt(1, paperId);
                    stmt.executeUpdate();
                }

                // 2. Delete question_images related to questions on this paper
                String delImages = "DELETE FROM question_images WHERE question_id IN (SELECT question_id FROM questions WHERE paper_id = ?)";
                try (PreparedStatement stmt = conn.prepareStatement(delImages)) {
                    stmt.setInt(1, paperId);
                    stmt.executeUpdate();
                }

                // 3. Delete questions tied to this paper
                String delQuestions = "DELETE FROM questions WHERE paper_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(delQuestions)) {
                    stmt.setInt(1, paperId);
                    stmt.executeUpdate();
                }

                // 4. Delete results tied to this paper
                String delResults = "DELETE FROM results WHERE paper_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(delResults)) {
                    stmt.setInt(1, paperId);
                    stmt.executeUpdate();
                }

                // 5. Delete the exam paper itself
                String delPaper = "DELETE FROM exam_papers WHERE paper_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(delPaper)) {
                    stmt.setInt(1, paperId);
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
