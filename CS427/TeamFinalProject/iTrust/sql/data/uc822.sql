/*Inserting the first record*/
INSERT INTO postnatal_care_record (
patient_mid, 
record_date, 
childbirth_date, 
delivery_type,
comment) 
VALUES (
400, 
'2018-11-17', 
'2018-11-15', 
'vaginal_delivery',
''
)ON DUPLICATE KEY UPDATE patient_mid = patient_mid;

/*Inserting the second record*/
INSERT INTO postnatal_care_record (
patient_mid, 
record_date, 
childbirth_date, 
delivery_type,
comment,
release_date) 
VALUES (
400, 
'2018-11-20', 
'2018-11-20', 
'caesarean_section',
'patient is stable',
'2018-11-26'
)ON DUPLICATE KEY UPDATE patient_mid = patient_mid;

/*Inserting the second record*/
INSERT INTO postnatal_care_record (
patient_mid, 
record_date, 
childbirth_date, 
delivery_type,
comment,
release_date) 
VALUES (
400, 
'2018-11-22', 
'2018-11-20', 
'vaginal_delivery',
'patient is stable',
'2018-11-22'
)ON DUPLICATE KEY UPDATE patient_mid = patient_mid;

/*Inserting the second record*/
INSERT INTO postnatal_care_record (
patient_mid, 
record_date, 
childbirth_date, 
delivery_type,
comment,
release_date) 
VALUES (
400, 
'2018-11-22', 
'2018-11-20', 
'vaginal_delivery',
'patient is stable',
'2018-11-22'
)ON DUPLICATE KEY UPDATE patient_mid = patient_mid;

/*Inserting the second record*/
INSERT INTO postnatal_care_record (
patient_mid, 
record_date, 
childbirth_date, 
delivery_type,
comment
) 
VALUES (
400, 
'2018-12-8', 
'2018-11-20', 
'vaginal_delivery',
''
)ON DUPLICATE KEY UPDATE patient_mid = patient_mid;