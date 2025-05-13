-- Majors
INSERT INTO majors (id, name) VALUES (1, 'Software Engineering');
INSERT INTO majors (id, name) VALUES (2, 'Artificial Intelligence');

-- Semesters
INSERT INTO semesters (id, name, major_id) VALUES (1, 'Semester 1', 1);
INSERT INTO semesters (id, name, major_id) VALUES (2, 'Semester 2', 1);
INSERT INTO semesters (id, name, major_id) VALUES (3, 'Semester 1', 2);

-- Courses
INSERT INTO courses (id, name, description, semester_id, teacher_id) VALUES (1, 'Intro to Programming', 'Basics of Java', 1, 101);
INSERT INTO courses (id, name, description, semester_id, teacher_id) VALUES (2, 'Data Structures', 'Learn stacks, queues', 2, 102);
INSERT INTO courses (id, name, description, semester_id, teacher_id) VALUES (3, 'AI Basics', 'Introduction to AI', 3, 103);

-- Lessons
INSERT INTO lessons (id, title, description, course_id) VALUES (1, 'Variables and Types', 'Intro to variables', 1);
INSERT INTO lessons (id, title, description, course_id) VALUES (2, 'Linked Lists', 'How lists work', 2);
INSERT INTO lessons (id, title, description, course_id) VALUES (3, 'Machine Learning Overview', 'Basics of ML', 3);

-- Course Teachers
INSERT INTO course_teachers (id, course_id, teacher_id) VALUES (1, 1, 101);
INSERT INTO course_teachers (id, course_id, teacher_id) VALUES (2, 2, 102);
INSERT INTO course_teachers (id, course_id, teacher_id) VALUES (3, 3, 103);

-- Course Students
INSERT INTO course_students (id, course_id, student_id) VALUES (1, 1, 1001);
INSERT INTO course_students (id, course_id, student_id) VALUES (2, 2, 1002);
INSERT INTO course_students (id, course_id, student_id) VALUES (3, 3, 1003);