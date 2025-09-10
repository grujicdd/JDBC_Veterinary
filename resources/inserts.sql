-- DML Script for Veterinary Clinic Database
-- Insert sample data for all tables

-- Insert Owners
INSERT INTO Owner (ownerID, firstName, lastName, phoneNumber, birthDate) VALUES (1, 'Marko', 'Petrović', '+381641234567', DATE '1985-03-15');
INSERT INTO Owner (ownerID, firstName, lastName, phoneNumber, birthDate) VALUES (2, 'Ana', 'Jovanović', '+381642345678', DATE '1990-07-22');
INSERT INTO Owner (ownerID, firstName, lastName, phoneNumber, birthDate) VALUES (3, 'Stefan', 'Nikolić', '+381643456789', DATE '1982-11-08');
INSERT INTO Owner (ownerID, firstName, lastName, phoneNumber, birthDate) VALUES (4, 'Milica', 'Stojanović', '+381644567890', DATE '1995-05-12');
INSERT INTO Owner (ownerID, firstName, lastName, phoneNumber, birthDate) VALUES (5, 'Nemanja', 'Mitrović', '+381645678901', DATE '1988-09-30');

-- Insert Breeds
INSERT INTO Breed (breedID, breedName) VALUES (1, 'Labrador');
INSERT INTO Breed (breedID, breedName) VALUES (2, 'German Shepherd');
INSERT INTO Breed (breedID, breedName) VALUES (3, 'Persian Cat');
INSERT INTO Breed (breedID, breedName) VALUES (4, 'Siamese Cat');
INSERT INTO Breed (breedID, breedName) VALUES (5, 'Golden Retriever');
INSERT INTO Breed (breedID, breedName) VALUES (6, 'British Shorthair');

-- Insert Pets
INSERT INTO Pet (petID, name, birthYear, species, Owner_ownerID, Breed_breedID) VALUES (1, 'Rex', 2018, 'Dog', 1, 1);
INSERT INTO Pet (petID, name, birthYear, species, Owner_ownerID, Breed_breedID) VALUES (2, 'Bella', 2019, 'Dog', 2, 2);
INSERT INTO Pet (petID, name, birthYear, species, Owner_ownerID, Breed_breedID) VALUES (3, 'Maca', 2020, 'Cat', 3, 3);
INSERT INTO Pet (petID, name, birthYear, species, Owner_ownerID, Breed_breedID) VALUES (4, 'Luna', 2021, 'Cat', 4, 4);
INSERT INTO Pet (petID, name, birthYear, species, Owner_ownerID, Breed_breedID) VALUES (5, 'Max', 2017, 'Dog', 5, 5);
INSERT INTO Pet (petID, name, birthYear, species, Owner_ownerID, Breed_breedID) VALUES (6, 'Whiskers', 2019, 'Cat', 1, 6);

-- Insert Dogs (subclass of Pet)
INSERT INTO Dog (petID, sterilized, trainingLevel) VALUES (1, 1, 'Advanced');
INSERT INTO Dog (petID, sterilized, trainingLevel) VALUES (2, 0, 'Basic');
INSERT INTO Dog (petID, sterilized, trainingLevel) VALUES (5, 1, 'Intermediate');

-- Insert Cats (subclass of Pet)
INSERT INTO Cat (petID, sterilized) VALUES (3, 1);
INSERT INTO Cat (petID, sterilized) VALUES (4, 0);
INSERT INTO Cat (petID, sterilized) VALUES (6, 1);

-- Insert Veterinarians
INSERT INTO Veterinarian (VetID, firstName, lastName, phoneNumber, Veterinarian_VetID) VALUES (1, 'Dr. Petar', 'Radić', '+381651111111', NULL);
INSERT INTO Veterinarian (VetID, firstName, lastName, phoneNumber, Veterinarian_VetID) VALUES (2, 'Dr. Marija', 'Stanković', '+381652222222', 1);
INSERT INTO Veterinarian (VetID, firstName, lastName, phoneNumber, Veterinarian_VetID) VALUES (3, 'Dr. Nikola', 'Popović', '+381653333333', 1);
INSERT INTO Veterinarian (VetID, firstName, lastName, phoneNumber, Veterinarian_VetID) VALUES (4, 'Dr. Jelena', 'Milić', '+381654444444', 2);

-- Insert Appointments
INSERT INTO Appointment (Pet_petID, Veterinarian_VetID, appDateTime, reason) VALUES (1, 1, DATE '2024-01-15', 'Annual checkup');
INSERT INTO Appointment (Pet_petID, Veterinarian_VetID, appDateTime, reason) VALUES (2, 2, DATE '2024-01-20', 'Vaccination');
INSERT INTO Appointment (Pet_petID, Veterinarian_VetID, appDateTime, reason) VALUES (3, 3, DATE '2024-01-25', 'Skin irritation');
INSERT INTO Appointment (Pet_petID, Veterinarian_VetID, appDateTime, reason) VALUES (4, 4, DATE '2024-02-01', 'Routine examination');
INSERT INTO Appointment (Pet_petID, Veterinarian_VetID, appDateTime, reason) VALUES (5, 1, DATE '2024-02-05', 'Joint pain');
INSERT INTO Appointment (Pet_petID, Veterinarian_VetID, appDateTime, reason) VALUES (6, 2, DATE '2024-02-10', 'Eye infection');


COMMIT;