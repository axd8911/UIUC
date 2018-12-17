To modify the patients table definition, you can use:

ALTER TABLE patients
ADD COLUMN obstetricEligible BOOLEAN default False AFTER Gender;