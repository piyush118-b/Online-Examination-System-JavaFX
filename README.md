# 🧠 Online Examination System (JavaFX + PostgreSQL)

A full-featured desktop-based examination system built using:

- Java (Core)
- JavaFX
- PostgreSQL
- JDBC
- Maven
- MVC Architecture

---

## 🚀 Features

### 🔐 Authentication
- Role-based login (Admin / Student)

### 👨‍🏫 Admin Panel
- Comprehensive Management Dashboard
- Create and manage distinct "Exam Papers"
- Add, update, and delete multiple choice questions per paper
- Categorize questions explicitly into Aptitude and Core sections
- Full question deletion cascading through student answers

### 👨‍🎓 Student Panel
- Select exam paper from active Dropdown
- Advanced 3-hour timer-based exam 
- Fully functional Question Grid Navigation jumping to any question
- Status tracking per question (Answered, Not Answered, Not Visited, Marked for Review)
- Isolated sections filtering (Aptitude vs Core)
- Clear interactions handling non-attempted answers gracefully

### 📊 Result System
- Auto evaluation triggered safely on submission
- Detailed Past Results View Dashboard tracking scores across attempts
- Granular Post-Exam Review Screen rendering each attempt showing the student's selected answer versus the correct answer (with distinct coloring for correct/incorrect/skipped).

---

## 🗄 Database Schema

### users
- `user_id`
- `username`
- `password`
- `role`

### exam_papers
- `paper_id`
- `paper_name`
- `description`

### questions
- `question_id`
- `paper_id` (FK)
- `question_text`
- `option_a`, `option_b`, `option_c`, `option_d`
- `correct_option`
- `marks`
- `section`

### question_images
- `image_id`
- `question_id` (FK)
- `image_path`

### results
- `result_id`
- `student_id` (FK)
- `paper_id` (FK)
- `score`
- `total_marks`
- `exam_date`

### student_answers
- `answer_id`
- `student_id` (FK)
- `result_id` (FK)
- `question_id` (FK)
- `selected_option`
- `correct_option`
- `is_correct`

---

## 🛠 Tech Stack

- Java 17
- JavaFX
- PostgreSQL
- Maven
- JDBC

---

## 🎯 Architecture

Layered Architecture:

Controller → Service → DAO → Database

---

## 📌 Setup

There are helper `.sql` files at the root of the project to initialize databases and inject mock exams:
1. `db_changes.sql`: Executes the full schema build configuring cascading foreign keys.
2. `insert_test_exam.sql`: Creates a full 25-question Mock Exam Paper containing 10 aptitude and 15 programming core questions.

---

## 📌 Future Improvements

- Fully decouple Question Banking to draw randomly generated paper instances
- Web-based version with React/Next.js
- Additional metrics charting performance over time