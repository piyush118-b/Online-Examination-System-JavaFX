-- This script generates a full 25-question Mock Exam Paper.
-- It inserts 10 Aptitude questions and 15 Core Programming questions under a single Exam Paper.

DO $$ 
DECLARE
    new_paper_id INT;
BEGIN
    -- 1. Create the new Exam Paper and store its ID in the variable
    INSERT INTO exam_papers (paper_name, description) 
    VALUES ('Mock Programming Exam', 'Test exam containing 10 aptitude and 15 coding questions')
    RETURNING paper_id INTO new_paper_id;

    -- 2. Insert 10 Aptitude Questions
    INSERT INTO questions (paper_id, section, marks, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES
    (new_paper_id, 'APTITUDE', 1, 'What comes next in the sequence: 2, 6, 12, 20, 30, ...?', '40', '42', '44', '48', 'B'),
    (new_paper_id, 'APTITUDE', 1, 'If 5 machines take 5 minutes to make 5 widgets, how long would it take 100 machines to make 100 widgets?', '100 minutes', '50 minutes', '5 minutes', '1 minute', 'C'),
    (new_paper_id, 'APTITUDE', 1, 'Which word does NOT belong with the others?', 'Leopard', 'Cougar', 'Elephant', 'Lion', 'C'),
    (new_paper_id, 'APTITUDE', 1, 'A train travels 60 miles per hour. How long will it take to travel 150 miles?', '2 hours', '2.5 hours', '3 hours', '3.5 hours', 'B'),
    (new_paper_id, 'APTITUDE', 1, 'If A = 1, B = 2, C = 3, what is the sum of the numerical values in the word CAB?', '5', '6', '7', '8', 'B'),
    (new_paper_id, 'APTITUDE', 1, 'What is the next prime number after 13?', '14', '15', '16', '17', 'D'),
    (new_paper_id, 'APTITUDE', 1, 'If no cats are dogs, and all dogs are animals, which is logically true?', 'No cats are animals', 'Some cats are animals', 'No dogs are cats', 'All animals are dogs', 'C'),
    (new_paper_id, 'APTITUDE', 1, 'What is 15% of 200?', '15', '20', '30', '40', 'C'),
    (new_paper_id, 'APTITUDE', 1, 'Solve for x: 3x + 12 = 27', '3', '4', '5', '6', 'C'),
    (new_paper_id, 'APTITUDE', 1, 'How many non-overlapping 1x1 squares are on a standard 8x8 chessboard?', '64', '8', '1', '128', 'A');

    -- 3. Insert 15 Core / Coding Questions
    INSERT INTO questions (paper_id, section, marks, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES
    (new_paper_id, 'CORE', 2, 'Which of the following is not a reserved keyword in Java?', 'implements', 'extends', 'inherit', 'finally', 'C'),
    (new_paper_id, 'CORE', 2, 'What does HTML stand for?', 'Hyper Text Markup Language', 'Hyper Text Multiple Language', 'Hyper Tool Multi Language', 'Hyper Text Multi Links', 'A'),
    (new_paper_id, 'CORE', 2, 'Which data structure natively uses LIFO (Last In First Out)?', 'Queue', 'Stack', 'Tree', 'Graph', 'B'),
    (new_paper_id, 'CORE', 2, 'What is the average time complexity of a Binary Search?', 'O(1)', 'O(N)', 'O(log N)', 'O(N^2)', 'C'),
    (new_paper_id, 'CORE', 2, 'Which SQL constraint ensures all values in a column are entirely distinct?', 'NOT NULL', 'CHECK', 'FOREIGN KEY', 'UNIQUE', 'D'),
    (new_paper_id, 'CORE', 2, 'In Python, which keyword is used to define a function?', 'def', 'function', 'void', 'define', 'A'),
    (new_paper_id, 'CORE', 2, 'What does CSS stand for?', 'Cascading Style Sheets', 'Creative Style Sheets', 'Computer Style Sheets', 'Colorful Style Sheets', 'A'),
    (new_paper_id, 'CORE', 2, 'Which loop guarantees its code block will execute at least once?', 'for loop', 'while loop', 'do-while loop', 'foreach loop', 'C'),
    (new_paper_id, 'CORE', 2, 'Which protocol runs over port 443 by default?', 'HTTP', 'FTP', 'HTTPS', 'SMTP', 'C'),
    (new_paper_id, 'CORE', 2, 'What is a boolean variable?', 'Variables containing numbers', 'Variables containing text', 'Variables containing true or false', 'Variables containing array nodes', 'C'),
    (new_paper_id, 'CORE', 2, 'In OOP, defining multiple methods with the exact same name but different parameters is known as?', 'Overriding', 'Overloading', 'Encapsulation', 'Polymorphism', 'B'),
    (new_paper_id, 'CORE', 2, 'Which company originally designed and developed Java?', 'Microsoft', 'Google', 'Sun Microsystems', 'Apple', 'C'),
    (new_paper_id, 'CORE', 2, 'Which symbol sequence designates a single-line comment in JavaScript?', '//', '/* */', '<!-- -->', '#', 'A'),
    (new_paper_id, 'CORE', 2, 'What is the default listener port for a PostgreSQL database?', '3306', '5432', '27017', '8080', 'B'),
    (new_paper_id, 'CORE', 2, 'Under the hood, which data structure is fundamental to implementing recursion?', 'Array', 'Linked List', 'Stack', 'Queue', 'C');

END $$;
