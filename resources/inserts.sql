-- Updated insert script with appointmentID
-- Run this after creating the new schema

-- Insert Owners
INSERT INTO Owner (ownerID, firstName, lastName, phoneNumber, birthDate) VALUES 
(1, 'Marko', 'Petrović', '+381641234567', '1985-03-15'),
(2, 'Ana', 'Jovanović', '+381642345678', '1990-07-22'),
(3, 'Stefan', 'Nikolić', '+381643456789', '1982-11-08'),
(4, 'Milica', 'Stojanović', '+381644567890', '1995-05-12'),
(5, 'Nemanja', 'Mitrović', '+381645678901', '1988-09-30'),
(6, 'Jovana', 'Marković', '+381646789012', '1992-04-18'),
(7, 'Aleksandar', 'Đorđević', '+381647890123', '1987-12-03'),
(8, 'Milena', 'Simić', '+381648901234', '1993-06-27'),
(9, 'Nikola', 'Popović', '+381649012345', '1986-01-15'),
(10, 'Teodora', 'Mladenović', '+381650123456', '1991-09-08');

-- Insert Breeds
INSERT INTO Breed (breedID, breedName) VALUES 
(1, 'Labrador'),
(2, 'German Shepherd'),
(3, 'Persian Cat'),
(4, 'Siamese Cat'),
(5, 'Golden Retriever'),
(6, 'British Shorthair'),
(7, 'Beagle'),
(8, 'Maine Coon'),
(9, 'Border Collie'),
(10, 'Russian Blue');

-- Insert Pets
INSERT INTO Pet (petID, name, birthYear, species, Owner_ownerID, Breed_breedID) VALUES 
(1, 'Rex', 2018, 'Dog', 1, 1),
(2, 'Bella', 2019, 'Dog', 2, 2),
(3, 'Maca', 2020, 'Cat', 3, 3),
(4, 'Luna', 2021, 'Cat', 4, 4),
(5, 'Max', 2017, 'Dog', 5, 5),
(6, 'Whiskers', 2019, 'Cat', 1, 6),
(7, 'Buddy', 2019, 'Dog', 6, 7),
(8, 'Shadow', 2020, 'Cat', 7, 8),
(9, 'Rocky', 2018, 'Dog', 8, 9),
(10, 'Misty', 2021, 'Cat', 9, 10),
(11, 'Charlie', 2019, 'Dog', 10, 1),
(12, 'Fluffy', 2020, 'Cat', 6, 3),
(13, 'Oldie', 2012, 'Dog', 7, 2),
(14, 'Grandpa', 2010, 'Cat', 8, 4);

-- Insert Dogs (subclass of Pet)
INSERT INTO Dog (petID, sterilized, trainingLevel) VALUES 
(1, 1, 'Advanced'),
(2, 0, 'Basic'),
(5, 1, 'Intermediate'),
(7, 1, 'Basic'),
(9, 0, 'Advanced'),
(11, 1, 'Intermediate'),
(13, 1, 'Advanced');

-- Insert Cats (subclass of Pet)
INSERT INTO Cat (petID, sterilized) VALUES 
(3, 1),
(4, 0),
(6, 1),
(8, 0),
(10, 1),
(12, 1),
(14, 0);

-- Insert Veterinarians
INSERT INTO Veterinarian (VetID, firstName, lastName, phoneNumber, Veterinarian_VetID) VALUES 
(1, 'Dr. Petar', 'Radić', '+381651111111', NULL),
(2, 'Dr. Marija', 'Stanković', '+381652222222', 1),
(3, 'Dr. Nikola', 'Popović', '+381653333333', 1),
(4, 'Dr. Jelena', 'Milić', '+381654444444', 2);

-- Insert Appointments with appointmentID
INSERT INTO Appointment (appointmentID, Pet_petID, Veterinarian_VetID, appDateTime, reason) VALUES 
-- Original appointments
(1, 1, 1, '2024-01-15 10:00:00', 'Annual checkup'),
(2, 2, 2, '2024-01-20 14:30:00', 'Vaccination'),
(3, 3, 3, '2024-01-25 09:15:00', 'Skin irritation'),
(4, 4, 4, '2024-02-01 11:00:00', 'Routine examination'),
(5, 5, 1, '2024-02-05 16:45:00', 'Joint pain'),
(6, 6, 2, '2024-02-10 13:20:00', 'Eye infection'),

-- Additional appointments to create multiple visits for same pet-vet combinations
(7, 1, 2, '2024-03-15 09:30:00', 'Follow-up checkup'),
(8, 1, 3, '2024-04-20 14:15:00', 'Dental cleaning'),
(9, 2, 1, '2024-03-25 11:00:00', 'Skin allergy treatment'),
(10, 2, 4, '2024-05-10 16:30:00', 'Behavioral consultation'),
(11, 3, 2, '2024-04-05 10:45:00', 'Nail trimming'),
(12, 4, 1, '2024-04-15 13:20:00', 'Spaying procedure'),
(13, 5, 3, '2024-03-30 15:00:00', 'Hip examination'),
(14, 6, 4, '2024-05-01 08:30:00', 'Eye drops follow-up'),

-- Appointments for new pets
(15, 7, 1, '2024-02-15 10:00:00', 'Initial puppy checkup'),
(16, 7, 1, '2024-03-20 14:30:00', 'Vaccination booster'),
(17, 7, 2, '2024-04-25 09:15:00', 'Deworming'),
(18, 8, 3, '2024-02-20 11:30:00', 'Kitten wellness exam'),
(19, 8, 3, '2024-03-25 16:00:00', 'Spaying consultation'),
(20, 8, 4, '2024-04-30 13:45:00', 'Post-surgery checkup'),
(21, 9, 2, '2024-01-30 10:30:00', 'Training assessment'),
(22, 9, 2, '2024-03-05 15:15:00', 'Injury follow-up'),
(23, 9, 1, '2024-04-10 09:00:00', 'Annual physical'),
(24, 10, 4, '2024-02-25 14:00:00', 'Declawing consultation'),
(25, 10, 4, '2024-03-30 11:15:00', 'Vaccination update'),
(26, 11, 1, '2024-01-15 16:45:00', 'Puppy training eval'),
(27, 11, 3, '2024-02-20 08:45:00', 'Growth checkup'),
(28, 11, 3, '2024-04-01 12:30:00', 'Nutrition consultation'),
(29, 12, 2, '2024-03-10 10:15:00', 'Feline wellness'),
(30, 12, 4, '2024-04-05 15:30:00', 'Hairball treatment'),

-- Additional appointments to create heavy workloads
(31, 1, 1, '2024-05-15 09:00:00', 'Geriatric screening'),
(32, 3, 1, '2024-05-20 14:30:00', 'Dental surgery prep'),
(33, 5, 1, '2024-05-25 11:15:00', 'Joint medication review'),
(34, 7, 3, '2024-05-12 16:00:00', 'Behavioral training'),
(35, 9, 3, '2024-05-18 13:30:00', 'Competition physical'),
(36, 11, 2, '2024-05-22 10:45:00', 'Agility assessment'),
(37, 12, 2, '2024-05-28 15:15:00', 'Senior cat wellness'),

-- Recent appointments
(38, 1, 4, CURRENT_DATE - INTERVAL '10 days', 'Recent followup'),
(39, 2, 1, CURRENT_DATE - INTERVAL '5 days', 'Current treatment'),
(40, 7, 2, CURRENT_DATE - INTERVAL '15 days', 'Recent puppy visit'),
(41, 9, 4, CURRENT_DATE - INTERVAL '20 days', 'Recent training session'),
(42, 11, 1, CURRENT_DATE - INTERVAL '7 days', 'Very recent checkup'),

-- Multiple appointments for same pet-vet combination (now possible!)
(43, 1, 1, '2024-06-01 10:00:00', 'Third visit with Dr. Radić'),
(44, 1, 1, '2024-06-15 14:30:00', 'Fourth visit with Dr. Radić'),
(45, 2, 2, '2024-06-05 11:00:00', 'Second visit with Dr. Stanković'),
(46, 7, 1, '2024-06-10 09:30:00', 'Third puppy visit with Dr. Radić');

-- Insert some sample medications
INSERT INTO Medication (medID, medName) VALUES 
(1, 'Antibiotics'),
(2, 'Pain Relief'),
(3, 'Eye Drops'),
(4, 'Antihistamine'),
(5, 'Deworming Tablets'),
(6, 'Joint Supplement'),
(7, 'Diet Food'),
(8, 'Respiratory Medicine');

-- Insert some sample vaccines
INSERT INTO Vaccine (vaccineID, diseaseName) VALUES 
(1, 'Rabies'),
(2, 'Distemper'),
(3, 'Feline Leukemia');

-- Insert some sample side effects
INSERT INTO SideEffect (sideEffectID, description) VALUES 
(1, 'Mild fever'),
(2, 'Drowsiness'),
(3, 'Loss of appetite');

-- Insert sample diagnoses (now using appointmentID)
INSERT INTO Diagnosis (diagnosisID, diseaseName, notes, Appointment_appointmentID) VALUES 
(1, 'Dental Disease', 'Moderate tartar buildup, recommend cleaning', 1),
(2, 'Skin Allergy', 'Environmental allergies, prescribed antihistamines', 2),
(3, 'Parasites', 'Intestinal worms found, deworming administered', 3),
(4, 'Eye Infection', 'Bacterial conjunctivitis, antibiotic drops prescribed', 6),
(5, 'Joint Pain', 'Mild arthritis in hips, pain management plan', 5),
(6, 'Healthy', 'Routine checkup, no issues found', 15),
(7, 'Obesity', 'Overweight, diet and exercise plan recommended', 21),
(8, 'Respiratory Issues', 'Upper respiratory infection, antibiotics prescribed', 18),
(9, 'Behavioral Issues', 'Excessive barking, training recommended', 26),
(10, 'Vaccination Update', 'All vaccinations current and up to date', 24);

-- Link medications to diagnoses
INSERT INTO Prescribed (Medication_medID, Diagnosis_diagnosisID) VALUES 
(1, 2), (4, 2),  -- Antibiotics and antihistamine for skin allergy
(2, 5), (6, 5),  -- Pain relief and joint supplement for joint pain
(3, 4),          -- Eye drops for eye infection
(5, 3),          -- Deworming tablets for parasites
(7, 7),          -- Diet food for obesity
(8, 8);          -- Respiratory medicine for breathing issues

COMMIT;

-- Verify the new structure
SELECT 'Database updated successfully with appointmentID!' as status;
SELECT COUNT(*) as total_appointments FROM Appointment;
SELECT Pet_petID, Veterinarian_VetID, COUNT(*) as appointment_count 
FROM Appointment 
GROUP BY Pet_petID, Veterinarian_VetID 
HAVING COUNT(*) > 1 
ORDER BY appointment_count DESC;