delete from obstetricOfficeVisit where OBhcpMID in (9000000012,9000000013) and patientMID in
(400,401,402,403,404,405,406) and visitDate in ("2017-11-05 03:00:00");

delete from patients where lastName = 'itrustcommon-test';

delete from ultrasound_records where create_date in ('2017-11-05 03:00:00','2019-01-05 03:00:00')
                                     and obstetric_office_visit_id = 1;

delete from obstetrec where patientMID in (402,403,404,405,406) and init_date = '2018-12-08';
delete from obstetrec where patientMID in (402,403,404,405,406) and init_date = CURDATE();