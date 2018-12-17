INSERT INTO obstetricOfficeVisit (OBhcpMID, patientMID, visitDate, numberDaysPregnant,
                                  blood_pressure, fhr, multiplet, llp,  ultrasound, weight)
VALUES (9000000012, 400, "2017-11-05 03:00:00", 80, '105/75', 130, 2, true, true,34);

INSERT INTO obstetricOfficeVisit (OBhcpMID, patientMID, visitDate, numberDaysPregnant,
                                  blood_pressure, fhr, multiplet, llp,  ultrasound, weight)
VALUES (9000000012, 401, "2017-11-05 03:00:00", 90, '95/65', 120, 1, false, false,30);

INSERT INTO obstetrec (patientMID, lmp_date, init_date) VALUES (402, DATE_SUB(CURDATE(),INTERVAL
  294 DAY ),CURDATE());

INSERT INTO obstetricOfficeVisit (OBhcpMID, patientMID, visitDate, numberDaysPregnant,
                                  blood_pressure, fhr, multiplet, llp,  ultrasound, weight)
VALUES (9000000012, 402, "2017-11-05 03:00:00", 100, '95/65', 120, 1, false, false,30);

INSERT INTO obstetrec (patientMID, lmp_date, init_date) VALUES (403, '2018-02-07', '2018-12-08');

INSERT INTO obstetricOfficeVisit (OBhcpMID, patientMID, visitDate, numberDaysPregnant,
                                  blood_pressure, fhr, multiplet, llp,  ultrasound, weight)
VALUES (9000000013, 403, "2017-11-05 03:00:00", 110, '105/75', 130, 2, true, true,34);

INSERT INTO obstetrec (patientMID, lmp_date, init_date) VALUES (404, '2018-04-07', '2018-12-08');

INSERT INTO obstetricOfficeVisit (OBhcpMID, patientMID, visitDate, numberDaysPregnant,
                                  blood_pressure, fhr, multiplet, llp,  ultrasound, weight)
VALUES (9000000013, 404, "2017-11-05 03:00:00", 120, '105/75', 130, 2, true, true,34);

INSERT INTO obstetrec (patientMID, lmp_date, init_date) VALUES (405, '2018-08-07', '2018-12-08');

INSERT INTO obstetricOfficeVisit (OBhcpMID, patientMID, visitDate, numberDaysPregnant,
                                  blood_pressure, fhr, multiplet, llp,  ultrasound, weight)
VALUES (9000000013, 405, "2017-11-05 03:00:00", 100, '105/75', 130, 2, true, true,34);

INSERT INTO obstetrec (patientMID, lmp_date, init_date) VALUES (406, '2018-11-07', '2018-12-08');

INSERT INTO obstetricOfficeVisit (OBhcpMID, patientMID, visitDate, numberDaysPregnant,
                                  blood_pressure, fhr, multiplet, llp,  ultrasound, weight)
VALUES (9000000013, 406, "2017-11-05 03:00:00", 200, '105/75', 130, 2, true, true,34);

INSERT INTO ultrasound_records (patient_mid, create_date, image_location, crown_rump_length,
                                biparietal_diameter,  head_circumference, femur_length,
                                occipitofrontal_diameter,  abdominal_circumference, humerus_length, estimated_fetal_weight, obstetric_office_visit_id)
VALUES (401, "2019-01-05 03:00:00", "iTrust\\image\\ultrasound\\1.png", 10.00, 11.00, 12.00, 13.00,  14.00, 15.00, 16.00, 7.00, 1);

INSERT INTO patients (lastName, firstName, email, address1, address2, city, state, zip,
                      phone, eName, ePhone,  iCName, iCAddress1, iCAddress2, iCCity, ICState, iCZip, iCPhone, iCID, dateofbirth, mothermid,  fathermid, bloodtype, ethnicity, gender, topicalnotes)
VALUES ('itrustcommon-test', 'itrustcommon-test1', 'itrustcommon@gmail.com', '1247 Noname Dr',
        'Suite 106', 'Raleigh', 'NC', '27606-1234', '919-971-0000', 'Online Unit',
        '704-532-2117',  'Google', '1234 Google', 'Suite 602', 'Charlotte', 'NC', '28215', '704-555-1234', 'ChetumNHowe',  '1950-05-10', 0, 0, 'AB-', 'African American', 'Female', '')
ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO patients (lastName, firstName, email, address1, address2, city, state, zip,
                      phone, eName, ePhone,  iCName, iCAddress1, iCAddress2, iCCity, ICState, iCZip, iCPhone, iCID, dateofbirth, mothermid,  fathermid, bloodtype, ethnicity, gender, topicalnotes)
VALUES ('itrustcommon-test', 'itrustcommon-test2', 'itrustcommon@gmail.com', '1247 Noname Dr',
        'Suite 106', 'Raleigh', 'NC', '27606-1234', '919-971-0000', 'Online Unit',
        '704-532-2117',  'Google', '1234 Google', 'Suite 602', 'Charlotte', 'NC', '28215', '704-555-1234', 'ChetumNHowe',  '1950-05-10', 0, 0, 'B-', 'African American', 'Female', '')
ON DUPLICATE KEY UPDATE MID = MID;
