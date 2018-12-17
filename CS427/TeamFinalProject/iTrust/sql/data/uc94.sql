
INSERT INTO obstetricOfficeVisit
(OBhcpMID, patientMID, visitDate, numberDaysPregnant, blood_pressure, fhr, weight, multiplet, llp, ultrasound)
VALUES 
(9000000012, 402, "2018-10-14 10:00:00", 90, '95/65', 120, 150, 1, false, false);

INSERT INTO ultrasound_records 
(patient_mid, create_date, image_location, crown_rump_length, biparietal_diameter, head_circumference, femur_length, occipitofrontal_diameter,
abdominal_circumference, humerus_length, estimated_fetal_weight, obstetric_office_visit_id)
VALUES
(402, "2018-10-14 10:00:00", "C:\\Users\\kyle_\\source\\CS427\\T822\\iTrust\\WebRoot\\image\\ultrasound", 10.00, 11.00, 12.00, 13.00, 14.00, 15.00, 16.00, 7.00, 1);