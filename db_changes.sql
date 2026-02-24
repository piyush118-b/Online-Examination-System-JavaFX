-- Database Schema Enhancements for Question Papers

-- Create new table for Exam Papers
CREATE TABLE IF NOT EXISTS exam_papers (
    paper_id SERIAL PRIMARY KEY,
    paper_name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT
);

-- Associate the existing questions table with the newly created exam papers
ALTER TABLE questions ADD COLUMN paper_id INT REFERENCES exam_papers(paper_id);

-- Add paper_id to results table
ALTER TABLE results ADD COLUMN IF NOT EXISTS paper_id INT REFERENCES exam_papers(paper_id);
ALTER TABLE results ADD COLUMN IF NOT EXISTS exam_date TIMESTAMP DEFAULT NOW();

-- Add result_id to student_answers to distinguish attempts
ALTER TABLE student_answers ADD COLUMN IF NOT EXISTS result_id INT REFERENCES results(result_id) ON DELETE CASCADE;

-- Optional: If you had previous questions, you might create a default paper, and map them:
-- INSERT INTO exam_papers (paper_name, description) VALUES ('Default Paper', 'Migration paper');
-- UPDATE questions SET paper_id = 1;
-- UPDATE results SET paper_id = 1;

-- =========================================================
-- QUERIES TO DELETE PAST ATTEMPTS
-- =========================================================

-- Option 1: Delete ALL exam attempts and answers across the entire system.
-- (This will wipe all scores, but keep the users and exam papers intact)
-- TRUNCATE TABLE student_answers CASCADE;
-- TRUNCATE TABLE results CASCADE;

-- Option 2: Delete attempts for a specific student (replace [STUDENT_ID] with the actual ID)
-- DELETE FROM results WHERE student_id = [STUDENT_ID];

-- Option 3: Delete a specific exam attempt (replace [RESULT_ID] with the actual Result ID)
-- DELETE FROM results WHERE result_id = [RESULT_ID];

