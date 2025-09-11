DROP TABLE IF EXISTS VacSideEff CASCADE;
DROP TABLE IF EXISTS PetVac CASCADE;
DROP TABLE IF EXISTS BreedGen CASCADE;
DROP TABLE IF EXISTS Prescribed CASCADE;
DROP TABLE IF EXISTS Diagnosis CASCADE;
DROP TABLE IF EXISTS Appointment CASCADE;
DROP TABLE IF EXISTS Dog CASCADE;
DROP TABLE IF EXISTS Cat CASCADE;
DROP TABLE IF EXISTS Pet CASCADE;
DROP TABLE IF EXISTS GenPredisp CASCADE;
DROP TABLE IF EXISTS SideEffect CASCADE;
DROP TABLE IF EXISTS Vaccine CASCADE;
DROP TABLE IF EXISTS Medication CASCADE;
DROP TABLE IF EXISTS Veterinarian CASCADE;
DROP TABLE IF EXISTS Breed CASCADE;
DROP TABLE IF EXISTS Owner CASCADE;

-- Create Owner table
CREATE TABLE Owner (
    ownerID INTEGER NOT NULL,
    firstName VARCHAR(64) NOT NULL,
    lastName VARCHAR(64) NOT NULL,
    phoneNumber VARCHAR(64) NOT NULL,
    birthDate DATE
);

ALTER TABLE Owner 
    ADD CONSTRAINT Owner_PK PRIMARY KEY (ownerID);

-- Create Breed table
CREATE TABLE Breed (
    breedID INTEGER NOT NULL,
    breedName VARCHAR(100) NOT NULL
);

ALTER TABLE Breed 
    ADD CONSTRAINT Breed_PK PRIMARY KEY (breedID);

-- Create Pet table
CREATE TABLE Pet (
    petID INTEGER NOT NULL,
    name VARCHAR(64) NOT NULL,
    birthYear INTEGER NOT NULL,
    species VARCHAR(3) NOT NULL CHECK (species IN ('Cat', 'Dog')),
    Owner_ownerID INTEGER,
    Breed_breedID INTEGER NOT NULL
);

ALTER TABLE Pet 
    ADD CONSTRAINT Pet_PK PRIMARY KEY (petID);

-- Create Dog table (inheritance)
CREATE TABLE Dog (
    petID INTEGER NOT NULL,
    sterilized INTEGER,
    trainingLevel VARCHAR(16)
);

ALTER TABLE Dog 
    ADD CONSTRAINT Dog_PK PRIMARY KEY (petID);

-- Create Cat table (inheritance)
CREATE TABLE Cat (
    petID INTEGER NOT NULL,
    sterilized INTEGER
);

ALTER TABLE Cat 
    ADD CONSTRAINT Cat_PK PRIMARY KEY (petID);

-- Create Veterinarian table (self-referencing for hierarchy)
CREATE TABLE Veterinarian (
    VetID INTEGER NOT NULL,
    firstName VARCHAR(64) NOT NULL,
    lastName VARCHAR(64) NOT NULL,
    phoneNumber VARCHAR(64) NOT NULL,
    Veterinarian_VetID INTEGER
);

ALTER TABLE Veterinarian 
    ADD CONSTRAINT Veterinarian_PK PRIMARY KEY (VetID);

-- Create Appointment table with appointmentID as primary key
CREATE TABLE Appointment (
    appointmentID INTEGER NOT NULL,
    Pet_petID INTEGER NOT NULL,
    Veterinarian_VetID INTEGER NOT NULL,
    appDateTime TIMESTAMP NOT NULL,
    reason VARCHAR(64)
);

ALTER TABLE Appointment 
    ADD CONSTRAINT Appointment_PK PRIMARY KEY (appointmentID);

-- Create Diagnosis table (now references appointmentID)
CREATE TABLE Diagnosis (
    diagnosisID INTEGER NOT NULL,
    diseaseName VARCHAR(64),
    notes VARCHAR(256),
    Appointment_appointmentID INTEGER NOT NULL
);

ALTER TABLE Diagnosis 
    ADD CONSTRAINT Diagnosis_PK PRIMARY KEY (diagnosisID);

-- Create Medication table
CREATE TABLE Medication (
    medID INTEGER NOT NULL,
    medName VARCHAR(64) NOT NULL
);

ALTER TABLE Medication 
    ADD CONSTRAINT Medication_PK PRIMARY KEY (medID);

-- Create Prescribed table (many-to-many between Medication and Diagnosis)
CREATE TABLE Prescribed (
    Medication_medID INTEGER NOT NULL,
    Diagnosis_diagnosisID INTEGER NOT NULL
);

ALTER TABLE Prescribed 
    ADD CONSTRAINT Prescribed_PK PRIMARY KEY (Medication_medID, Diagnosis_diagnosisID);

-- Create SideEffect table
CREATE TABLE SideEffect (
    sideEffectID INTEGER NOT NULL,
    description VARCHAR(64) NOT NULL
);

ALTER TABLE SideEffect 
    ADD CONSTRAINT SideEffect_PK PRIMARY KEY (sideEffectID);

-- Create Vaccine table
CREATE TABLE Vaccine (
    vaccineID INTEGER NOT NULL,
    diseaseName VARCHAR(64) NOT NULL
);

ALTER TABLE Vaccine 
    ADD CONSTRAINT Vaccine_PK PRIMARY KEY (vaccineID);

-- Create PetVac table (many-to-many between Pet and Vaccine)
CREATE TABLE PetVac (
    Pet_petID INTEGER NOT NULL,
    Vaccine_vaccineID INTEGER NOT NULL,
    vaccinationDate DATE DEFAULT CURRENT_DATE
);

ALTER TABLE PetVac 
    ADD CONSTRAINT PetVac_PK PRIMARY KEY (Pet_petID, Vaccine_vaccineID);

-- Create VacSideEff table (many-to-many between Vaccine and SideEffect)
CREATE TABLE VacSideEff (
    Vaccine_vaccineID INTEGER NOT NULL,
    SideEffect_sideEffectID INTEGER NOT NULL
);

ALTER TABLE VacSideEff 
    ADD CONSTRAINT VacSideEff_PK PRIMARY KEY (Vaccine_vaccineID, SideEffect_sideEffectID);

-- Create GenPredisp table (Genetic Predisposition)
CREATE TABLE GenPredisp (
    GP_ID INTEGER NOT NULL,
    diseaseName VARCHAR(64) NOT NULL,
    geneticMarker VARCHAR(64),
    genID INTEGER NOT NULL
);

ALTER TABLE GenPredisp 
    ADD CONSTRAINT GenPredisp_PK PRIMARY KEY (diseaseName, genID);

-- Create BreedGen table (many-to-many between Breed and GenPredisp)
CREATE TABLE BreedGen (
    Breed_breedID INTEGER NOT NULL,
    GenPredisp_diseaseName VARCHAR(64),
    GenPredisp_GP_ID INTEGER NOT NULL,
    GenPredisp_genID INTEGER NOT NULL,
    riskLevel VARCHAR(16) NOT NULL CHECK (riskLevel IN ('Low', 'Medium', 'High'))
);

ALTER TABLE BreedGen 
    ADD CONSTRAINT BreedGen_PKv2 PRIMARY KEY (Breed_breedID, GenPredisp_genID);

ALTER TABLE BreedGen 
    ADD CONSTRAINT BreedGen_PK UNIQUE (Breed_breedID, GenPredisp_GP_ID);

-- Add Foreign Key Constraints
ALTER TABLE Pet 
    ADD CONSTRAINT Pet_Owner_FK FOREIGN KEY (Owner_ownerID) 
    REFERENCES Owner (ownerID) ON DELETE SET NULL;

ALTER TABLE Pet 
    ADD CONSTRAINT Pet_Breed_FK FOREIGN KEY (Breed_breedID) 
    REFERENCES Breed (breedID);

ALTER TABLE Dog 
    ADD CONSTRAINT Dog_Pet_FK FOREIGN KEY (petID) 
    REFERENCES Pet (petID) ON DELETE CASCADE;

ALTER TABLE Cat 
    ADD CONSTRAINT Cat_Pet_FK FOREIGN KEY (petID) 
    REFERENCES Pet (petID) ON DELETE CASCADE;

ALTER TABLE Veterinarian 
    ADD CONSTRAINT Veterinarian_Veterinarian_FK FOREIGN KEY (Veterinarian_VetID) 
    REFERENCES Veterinarian (VetID);

ALTER TABLE Appointment 
    ADD CONSTRAINT Appointment_Pet_FK FOREIGN KEY (Pet_petID) 
    REFERENCES Pet (petID) ON DELETE CASCADE;

ALTER TABLE Appointment 
    ADD CONSTRAINT Appointment_Veterinarian_FK FOREIGN KEY (Veterinarian_VetID) 
    REFERENCES Veterinarian (VetID);

-- Diagnosis now references appointmentID instead of composite key
ALTER TABLE Diagnosis 
    ADD CONSTRAINT Diagnosis_Appointment_FK FOREIGN KEY (Appointment_appointmentID) 
    REFERENCES Appointment (appointmentID) ON DELETE CASCADE;

ALTER TABLE Prescribed 
    ADD CONSTRAINT Prescribed_Medication_FK FOREIGN KEY (Medication_medID) 
    REFERENCES Medication (medID);

ALTER TABLE Prescribed 
    ADD CONSTRAINT Prescribed_Diagnosis_FK FOREIGN KEY (Diagnosis_diagnosisID) 
    REFERENCES Diagnosis (diagnosisID) ON DELETE CASCADE;

ALTER TABLE PetVac 
    ADD CONSTRAINT PetVac_Pet_FK FOREIGN KEY (Pet_petID) 
    REFERENCES Pet (petID) ON DELETE CASCADE;

ALTER TABLE PetVac 
    ADD CONSTRAINT PetVac_Vaccine_FK FOREIGN KEY (Vaccine_vaccineID) 
    REFERENCES Vaccine (vaccineID);

ALTER TABLE VacSideEff 
    ADD CONSTRAINT VacSideEff_Vaccine_FK FOREIGN KEY (Vaccine_vaccineID) 
    REFERENCES Vaccine (vaccineID);

ALTER TABLE VacSideEff 
    ADD CONSTRAINT VacSideEff_SideEffect_FK FOREIGN KEY (SideEffect_sideEffectID) 
    REFERENCES SideEffect (sideEffectID);

ALTER TABLE BreedGen 
    ADD CONSTRAINT BreedGen_Breed_FK FOREIGN KEY (Breed_breedID) 
    REFERENCES Breed (breedID);

ALTER TABLE BreedGen 
    ADD CONSTRAINT BreedGen_GenPredisp_FK FOREIGN KEY (GenPredisp_diseaseName, GenPredisp_genID) 
    REFERENCES GenPredisp (diseaseName, genID);

-- Create indexes for better performance
CREATE INDEX idx_pet_owner ON Pet(Owner_ownerID);
CREATE INDEX idx_pet_breed ON Pet(Breed_breedID);
CREATE INDEX idx_pet_species ON Pet(species);
CREATE INDEX idx_appointment_pet ON Appointment(Pet_petID);
CREATE INDEX idx_appointment_vet ON Appointment(Veterinarian_VetID);
CREATE INDEX idx_appointment_date ON Appointment(appDateTime);
CREATE INDEX idx_diagnosis_appointment ON Diagnosis(Appointment_appointmentID);
CREATE INDEX idx_petvac_pet ON PetVac(Pet_petID);
CREATE INDEX idx_petvac_vaccine ON PetVac(Vaccine_vaccineID);

-- Add triggers for inheritance constraint validation
CREATE OR REPLACE FUNCTION validate_dog_inheritance() 
RETURNS TRIGGER AS $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM Pet 
        WHERE petID = NEW.petID AND species = 'Dog'
    ) THEN
        RAISE EXCEPTION 'Pet must be a Dog to insert into Dog table';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_dog_inheritance
    BEFORE INSERT OR UPDATE ON Dog
    FOR EACH ROW EXECUTE FUNCTION validate_dog_inheritance();

CREATE OR REPLACE FUNCTION validate_cat_inheritance() 
RETURNS TRIGGER AS $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM Pet 
        WHERE petID = NEW.petID AND species = 'Cat'
    ) THEN
        RAISE EXCEPTION 'Pet must be a Cat to insert into Cat table';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_cat_inheritance
    BEFORE INSERT OR UPDATE ON Cat
    FOR EACH ROW EXECUTE FUNCTION validate_cat_inheritance();