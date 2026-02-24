package com.exam.dao;

import com.exam.config.DBConnection;
import com.exam.model.Result;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ResultDAO {

    public int saveResult(Result result) {
        String query = "INSERT INTO results (student_id, paper_id, score, total_marks, exam_date) VALUES (?, ?, ?, ?, NOW()) RETURNING result_id";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, result.getStudentId());
            stmt.setInt(2, result.getPaperId());
            stmt.setInt(3, result.getScore());
            stmt.setInt(4, result.getTotalMarks());
            java.sql.ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("result_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public java.util.List<Result> getResultsByStudent(int studentId) {
        java.util.List<Result> results = new java.util.ArrayList<>();
        String query = "SELECT r.*, e.paper_name FROM results r " +
                "LEFT JOIN exam_papers e ON r.paper_id = e.paper_id " +
                "WHERE r.student_id = ? ORDER BY r.exam_date DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, studentId);
            java.sql.ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Result res = new Result(
                        rs.getInt("student_id"),
                        rs.getInt("paper_id"),
                        rs.getInt("score"),
                        rs.getInt("total_marks"));
                res.setResultId(rs.getInt("result_id"));
                res.setPaperName(rs.getString("paper_name"));

                java.sql.Timestamp ts = rs.getTimestamp("exam_date");
                if (ts != null) {
                    res.setExamDate(ts.toLocalDateTime());
                }

                results.add(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
}