INSERT INTO chvappointment(
doctor_id,
patient_id,
sched_date,
appt_type,
prefer_dmethod,
when_scheduled,
comment	)
VALUES (
9000000012,
402,
"2019-10-14 10:00:00",
"Childbirth",
"vaginal_delivery",
"office_visit",
"This is a comment."
)ON DUPLICATE KEY UPDATE appt_id = appt_id;

INSERT INTO chvdelivery(
	doctor_id,
	patient_id,
	delivery_date,
	delivery_method,
	dosPitocin,
	dosNitrous,
	dosPethidine,
	dosEpidural,
	dosMagnesium,
	dosRHimmune
)
VALUES (
9000000012,
402,
"2018-10-14 10:00:00",
"vaginal_delivery",
1,
1,
1,
1,
1,
1
)ON DUPLICATE KEY UPDATE delivery_id = delivery_id;

INSERT INTO chvbaby (
  delivery_id,
  mid,
  first_name,
  last_name,
  gender
)
VALUES (
1,
9000000012,
"SEYMORE",
"BUTTS",
"Male"
)ON DUPLICATE KEY UPDATE baby_id = baby_id;