
-- CLINICĂ
INSERT INTO CLINIC (id, address, name) VALUES (1, 'Str. Zambilelor 12', 'Clinica Dentica');
INSERT INTO CLINIC (id, address, name) VALUES (2, 'Bd. Carol 8', 'Clinica OrtoSmile');
INSERT INTO CLINIC (id, address, name) VALUES (3, 'Calea Mosilor 99', 'Clinica TotalDent');
INSERT INTO CLINIC (id, address, name) VALUES (4, 'Str. Mihai Viteazu 22', 'Clinica PerfectSmile');

-- DOCTORI
INSERT INTO DOCTOR (id, first_name, last_name, speciality, clinic_id) VALUES (1, 'Laura', 'Petrescu', 'ORTODONT', 1);
INSERT INTO DOCTOR (id, first_name, last_name, speciality, clinic_id) VALUES (2, 'Mihai', 'Dumitrescu', 'STOMATOLOG', 2);
INSERT INTO DOCTOR (id, first_name, last_name, speciality, clinic_id) VALUES (3, 'Alina', 'Radu', 'RADIOLOG', 3);
INSERT INTO DOCTOR (id, first_name, last_name, speciality, clinic_id) VALUES (4, 'Bogdan', 'Enache', 'ENDODONT', 4);

-- PACIENȚI
INSERT INTO PATIENT (id, dob, email, first_name, last_name, phone) VALUES (1, DATE '1990-04-22', 'ana@gmail.com', 'Ana', 'Ionescu', '0722123456');
INSERT INTO PATIENT (id, dob, email, first_name, last_name, phone) VALUES (2, DATE '1987-11-05', 'cristian@gmail.com', 'Cristian', 'Stan', '0723333444');
INSERT INTO PATIENT (id, dob, email, first_name, last_name, phone) VALUES (3, DATE '1995-07-15', 'elena@gmail.com', 'Elena', 'Popescu', '0725666777');
INSERT INTO PATIENT (id, dob, email, first_name, last_name, phone) VALUES (4, DATE '2000-01-01', 'vlad@gmail.com', 'Vlad', 'Rusu', '0729888777');

-- MEDICAȚII
INSERT INTO MEDICATION (id, brand, name) VALUES (1, 'Sensodyne', 'Rapid Relief');
INSERT INTO MEDICATION (id, brand, name) VALUES (2, 'Elmex', 'Erosion Protection');
INSERT INTO MEDICATION (id, brand, name) VALUES (3, 'Parodontax', 'Extra Fresh');
INSERT INTO MEDICATION (id, brand, name) VALUES (4, 'Listerine', 'Total Care');

-- PROGRAMĂRI
INSERT INTO APPOINTMENT (id, comments, start_date, end_date, doctor_id, patient_id) VALUES (1, 'Prima consultație ortodontică', TIMESTAMP '2025-05-15 09:00:00', TIMESTAMP '2025-05-15 09:30:00', 1, 1);
INSERT INTO APPOINTMENT (id, comments, start_date, end_date, doctor_id, patient_id) VALUES (2, 'Control post-operator', TIMESTAMP '2025-05-16 11:00:00', TIMESTAMP '2025-05-16 11:30:00', 2, 2);
INSERT INTO APPOINTMENT (id, comments, start_date, end_date, doctor_id, patient_id) VALUES (3, 'Consultație parodontală', TIMESTAMP '2025-05-17 14:00:00', TIMESTAMP '2025-05-17 14:30:00', 3, 3);
INSERT INTO APPOINTMENT (id, comments, start_date, end_date, doctor_id, patient_id) VALUES (4, 'Tratament endodontic', TIMESTAMP '2025-05-18 16:00:00', TIMESTAMP '2025-05-18 16:30:00', 4, 4);

-- PRESCRIPȚII
INSERT INTO PRESCRIPTION (id, comments, appointment_id) VALUES (1, 'Folosit dimineața și seara', 1);
INSERT INTO PRESCRIPTION (id, comments, appointment_id) VALUES (2, 'După fiecare masă', 2);
INSERT INTO PRESCRIPTION (id, comments, appointment_id) VALUES (3, 'O dată pe zi seara', 3);
INSERT INTO PRESCRIPTION (id, comments, appointment_id) VALUES (4, 'Timp de 7 zile consecutiv', 4);

-- PRESCRIPTION_MEDICATION
INSERT INTO PRESCRIPTION_MEDICATION (prescription_id, medication_id) VALUES (1, 1);
INSERT INTO PRESCRIPTION_MEDICATION (prescription_id, medication_id) VALUES (1, 3);
INSERT INTO PRESCRIPTION_MEDICATION (prescription_id, medication_id) VALUES (2, 2);
INSERT INTO PRESCRIPTION_MEDICATION (prescription_id, medication_id) VALUES (3, 3);
INSERT INTO PRESCRIPTION_MEDICATION (prescription_id, medication_id) VALUES (3, 4);
INSERT INTO PRESCRIPTION_MEDICATION (prescription_id, medication_id) VALUES (4, 1);
INSERT INTO PRESCRIPTION_MEDICATION (prescription_id, medication_id) VALUES (4, 2);
INSERT INTO PRESCRIPTION_MEDICATION (prescription_id, medication_id) VALUES (4, 4);

