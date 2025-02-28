INSERT INTO users(id, name, email, password, role) VALUES(1, 'Den', 'admin1_mogilev@yopmail.com', '$2a$10$/WZQUmfLaAEkwAf8cEnnvO9QVfkIvEhIHPxDeNfc/yEVizn/S45IC', 'ROLE_ADMIN');
INSERT INTO users(id, name, email, password, role) VALUES(2, 'Peter', 'peter_mogilev@yopmail.com', '$2a$10$S1puZVqdRQ4b16CBZQp3JORkb98I6Zix8Y2Xhe9Qa7SYMyDEvIF.K', 'ROLE_BUYER');
INSERT INTO users(id, name, email, password, role) VALUES(3, 'Asya', 'asya_mogilev@yopmail.com','$2a$10$m4U3WdMcRqBCoKGfEezbtuevVwQOrHslworaCTYePEOrAMhf1bdfO', 'ROLE_BUYER');
INSERT INTO users(id, name, email, password, role) VALUES(4, 'Jimmy', 'admin2_mogilev@yopmail.com', '$2a$10$UgZD3QCdfXKXaWph3qdDgeqmxvzVW7DJIwy8504.S.fZ.SDD5WtdO', 'ROLE_ADMIN');
INSERT INTO users(id, name, email, password, role) VALUES(5, 'Maricel', 'maricel_mogilev@yopmail.com', '$2a$10$LoP4QX/iLp6VpHKJT9gOiu3kL1FPm6h59W3LoqEc26PYcMJplrGq2', 'ROLE_BUYER');

SELECT setval('user_id_seq', (SELECT MAX(id) from users));