-- Majors
INSERT INTO majors (id, name) VALUES (1, 'Software Engineering');
INSERT INTO majors (id, name) VALUES (2, 'Artificial Intelligence');

-- Semesters
INSERT INTO semesters (id, name, major_id) VALUES (1, 'Semester 1', 1);
INSERT INTO semesters (id, name, major_id) VALUES (2, 'Semester 2', 1);
INSERT INTO semesters (id, name, major_id) VALUES (3, 'Semester 1', 2);

-- Courses (using teacher@test.com user ID which will be created by TestDataInitializer)
INSERT INTO courses (id, name, description, semester_id, teacher_id) VALUES (1, 'Intro to Programming', 'Basics of Java Programming', 1, 2);
INSERT INTO courses (id, name, description, semester_id, teacher_id) VALUES (2, 'Data Structures', 'Learn stacks, queues, and linked lists', 2, 2);
INSERT INTO courses (id, name, description, semester_id, teacher_id) VALUES (3, 'AI Basics', 'Introduction to Artificial Intelligence', 3, 2);

-- Lessons
INSERT INTO lessons (id, title, description, course_id) VALUES (1, 'Variables and Types', 'Introduction to variables and data types in Java', 1);
INSERT INTO lessons (id, title, description, course_id) VALUES (2, 'Control Structures', 'If statements, loops, and switch cases', 1);
INSERT INTO lessons (id, title, description, course_id) VALUES (3, 'Linked Lists', 'Understanding linked list data structure', 2);
INSERT INTO lessons (id, title, description, course_id) VALUES (4, 'Stacks and Queues', 'Stack and queue implementations', 2);
INSERT INTO lessons (id, title, description, course_id) VALUES (5, 'Machine Learning Overview', 'Basics of machine learning algorithms', 3);
INSERT INTO lessons (id, title, description, course_id) VALUES (6, 'Neural Networks', 'Introduction to neural networks', 3);

-- Course Teachers (assigning teacher@test.com to all courses)
INSERT INTO course_teachers (id, course_id, teacher_id) VALUES (1, 1, 2);
INSERT INTO course_teachers (id, course_id, teacher_id) VALUES (2, 2, 2);
INSERT INTO course_teachers (id, course_id, teacher_id) VALUES (3, 3, 2);

-- Course Students (assigning student@test.com to all courses)
INSERT INTO course_students (id, course_id, student_id) VALUES (1, 1, 1);
INSERT INTO course_students (id, course_id, student_id) VALUES (2, 2, 1);
INSERT INTO course_students (id, course_id, student_id) VALUES (3, 3, 1);