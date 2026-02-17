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
- Add multiple choice questions
- Store questions in PostgreSQL

### 👨‍🎓 Student Panel
- Attempt exam
- Next / Previous navigation
- Submit only on last question
- Timer-based exam
- Auto evaluation

### 📊 Result System
- Score calculation
- Percentage calculation
- PASS / FAIL logic
- Store results in database
- View previous results

---

## 🗄 Database Schema

### users
- user_id
- username
- password
- role

### questions
- question_id
- question_text
- option_a
- option_b
- option_c
- option_d
- correct_option
- marks

### results
- result_id
- student_id
- score
- total_marks
- exam_date

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

## 📌 Future Improvements

- Question shuffling
- Review answers after submission
- Spring Boot backend version
- Web-based version with React