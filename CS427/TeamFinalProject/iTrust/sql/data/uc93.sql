/*Inserting Kathryn Evans*/
INSERT INTO personnel(
MID,
AMID,
role,
lastName, 
firstName, 
address1,
address2,
city,
state,
zip,
phone,
specialty,
email)
VALUES (
9000000012,
null,
'hcp',
'Evans',
'Kathryn',
'10078 Avent Ferry Road',
'',
'Capitol City',
'NC',
'28700-0458',
'555-877-5100',
'OB/GYN',
'evans@iTrust.org'
)ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) VALUES(9000000012, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'hcp', 'first letter?', 'a')
ON DUPLICATE KEY UPDATE MID = MID;
/*password: pw*/

INSERT INTO hcpassignedhos(HCPID, HosID) VALUES(9000000012,'4'), (9000000012,'4')
ON DUPLICATE KEY UPDATE HCPID = HCPID;

/*Inserting Harry Potter*/
INSERT INTO personnel(
MID,
AMID,
role,
lastName, 
firstName, 
address1,
address2,
city,
state,
zip,
phone,
specialty,
email)
VALUES (
9000000013,
null,
'hcp',
'Potter',
'Harry',
'10078 Imaginary Lane',
'',
'Capitol City',
'NC',
'28700-0458',
'555-877-5100',
'surgeon',
'sorcery@iTrust.org'
)ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) VALUES(9000000013, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'hcp', 'first letter?', 'a')
ON DUPLICATE KEY UPDATE MID = MID;
/*password: pw*/

/*Inserting patient Daria Griffin*/
INSERT INTO patients
(MID, 
lastName, 
firstName,
email,
address1,
address2,
city,
state,
zip,
phone,
dateofbirth,
Gender,
obstetricEligible)
VALUES
(400,
'Griffin', 
'Daria', 
'dgrif@gmail.com', 
'1333 Who Cares Road', 
'Suite 102', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-971-0000',
'1993-10-25',
'Female',
True)
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (400, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'blue')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/

/*Inserting patient Brenna Lowery*/
INSERT INTO patients
(MID, 
lastName, 
firstName,
email,
address1,
address2,
city,
state,
zip,
phone,
dateofbirth,
Gender,
obstetricEligible)
VALUES
(401,
'Lowery', 
'Brenna', 
'lowery@gmail.com', 
'1333 Who Cares Road', 
'Suite 103', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-971-0001',
'1977-03-15',
'Female',
True)
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (401, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'blue')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
  
/*Inserting patient Amelia Davidson*/
INSERT INTO patients
(MID, 
lastName, 
firstName,
email,
address1,
address2,
city,
state,
zip,
phone,
dateofbirth,
Gender,
obstetricEligible)
VALUES
(402,
'Davidson', 
'Amelia', 
'amelia@gmail.com', 
'1333 Who Cares Road', 
'Suite 104', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-971-0002',
'1985-06-27',
'Female',
True)
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (402, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'blue')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/

/*Inserting patient Thane Ross*/
INSERT INTO patients
(MID, 
lastName, 
firstName,
email,
address1,
address2,
city,
state,
zip,
phone,
dateofbirth,
Gender,
obstetricEligible)
VALUES
(403,
'Ross', 
'Thane', 
'ross@gmail.com', 
'1333 Who Cares Road', 
'Suite 105', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-971-0003',
'1985-06-27',
'Male',
False)
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (403, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'blue')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/

/*Inserting patient Mary Hadalamb*/
INSERT INTO patients
(MID, 
lastName, 
firstName,
email,
address1,
address2,
city,
state,
zip,
phone,
dateofbirth,
Gender,
obstetricEligible)
VALUES
(404,
'Hadalamb', 
'Mary', 
'snowlamb@gmail.com', 
'1333 Who Cares Road', 
'Suite 106', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-971-0004',
'1985-06-27',
'Female',
True)
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (404, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'blue')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/

/*Inserting patient Rock Solid*/
INSERT INTO patients
(MID, 
lastName, 
firstName,
email,
address1,
address2,
city,
state,
zip,
phone,
dateofbirth,
Gender,
obstetricEligible)
VALUES
(405,
'Solid', 
'Rock', 
'androll@gmail.com', 
'1333 Who Cares Road', 
'Suite 107', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-971-0005',
'1985-06-27',
'Female',
True)
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (405, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'blue')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/

/*Inserting patient Sing Along*/
INSERT INTO patients
(MID, 
lastName, 
firstName,
email,
address1,
address2,
city,
state,
zip,
phone,
dateofbirth,
Gender,
obstetricEligible)
VALUES
(406,
'Along', 
'Sing', 
'song@gmail.com', 
'1333 Who Cares Road', 
'Suite 108', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-971-0006',
'1985-06-27',
'Female',
True)
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (406, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'blue')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/


/*create prior pregnancies for Brenna Lowery */
INSERT INTO obstetrec(patientMID, lmp_date, init_date)
			VALUES (401, '2007-12-31', '2008-01-21')
 ON DUPLICATE KEY UPDATE obstetrec_id = obstetrec_id;

INSERT INTO obstetrec(patientMID, lmp_date, init_date)
		VALUES (401, '2009-12-31', '2010-01-21')
ON DUPLICATE KEY UPDATE obstetrec_id = obstetrec_id;

INSERT INTO obstetpriorpreg(patientMID, conception_year, pregnant_weeks, pregnant_days, labor_hours, weight_gain, delivery_type, multiplet)
		VALUES (401, 2008, 40, 0, 6.5, 15.5, 'vaginal_delivery_vacuum_assist', 1)
ON DUPLICATE KEY UPDATE obstetpriorpreg_id = obstetpriorpreg_id;

INSERT INTO obstetpriorpreg(patientMID, conception_year, pregnant_weeks, pregnant_days, labor_hours, weight_gain, delivery_type, multiplet)
		VALUES (401, 2010, 40, 0, 6.5, 15.5, 'vaginal_delivery_vacuum_assist', 1)
ON DUPLICATE KEY UPDATE obstetpriorpreg_id = obstetpriorpreg_id;

