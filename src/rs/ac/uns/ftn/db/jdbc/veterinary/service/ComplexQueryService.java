package rs.ac.uns.ftn.db.jdbc.veterinary.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rs.ac.uns.ftn.db.jdbc.veterinary.connection.ConnectionUtil_HikariCP;

/**
 * Service class for handling complex queries and business logic
 * that spans multiple tables and entities.
 */
public class ComplexQueryService {
    
    /**
     * Complex Query 1: Show all owners and their pets
     * If owner has no pets, display: NO PETS
     */
    public void getOwnersWithPets() throws SQLException {
        String query = """
            SELECT o.ownerID, o.firstName, o.lastName, o.phoneNumber, o.birthDate,
                   p.petID, p.name as petName, p.birthYear, p.species, b.breedName
            FROM Owner o
            LEFT JOIN Pet p ON o.ownerID = p.Owner_ownerID
            LEFT JOIN Breed b ON p.Breed_breedID = b.breedID
            ORDER BY o.lastName, o.firstName, p.name
            """;
        
        Map<Integer, OwnerWithPetsDTO> ownersMap = new HashMap<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                int ownerID = resultSet.getInt("ownerID");
                
                // Get or create owner entry
                OwnerWithPetsDTO owner = ownersMap.get(ownerID);
                if (owner == null) {
                    owner = new OwnerWithPetsDTO(
                        ownerID,
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getDate("birthDate")
                    );
                    ownersMap.put(ownerID, owner);
                }
                
                // Add pet if exists
                if (resultSet.getObject("petID") != null) {
                    PetInfoDTO pet = new PetInfoDTO(
                        resultSet.getInt("petID"),
                        resultSet.getString("petName"),
                        resultSet.getInt("birthYear"),
                        resultSet.getString("species"),
                        resultSet.getString("breedName")
                    );
                    owner.addPet(pet);
                }
            }
        }
        
        // Display results
        System.out.println("\n=== OWNERS AND THEIR PETS ===");
        System.out.println("=" .repeat(80));
        
        for (OwnerWithPetsDTO owner : ownersMap.values()) {
            owner.display();
            System.out.println("-".repeat(80));
        }
        
        System.out.println("Total owners: " + ownersMap.size());
    }

    public void getActiveVeterinariansWorkloadAnalysis() throws SQLException {
        String query = """
            SELECT v.VetID, 
                   v.firstName, 
                   v.lastName, 
                   v.phoneNumber,
                   supervisor.firstName || ' ' || supervisor.lastName as supervisorName,
                   COUNT(DISTINCT a.Pet_petID || '-' || a.Veterinarian_VetID) as totalAppointments,
                   COUNT(DISTINCT p.petID) as uniquePetsServed,
                   COUNT(DISTINCT o.ownerID) as uniqueOwnersServed,
                   COUNT(DISTINCT b.breedID) as breedVariety,
                   AVG(EXTRACT(YEAR FROM CURRENT_DATE) - p.birthYear) as avgPetAge,
                   MAX(a.appDateTime) as lastAppointmentDate,
                   CASE 
                       WHEN COUNT(DISTINCT a.Pet_petID || '-' || a.Veterinarian_VetID) >= 4 THEN 'Very Heavy'
                       WHEN COUNT(DISTINCT a.Pet_petID || '-' || a.Veterinarian_VetID) >= 3 THEN 'Heavy'
                       WHEN COUNT(DISTINCT a.Pet_petID || '-' || a.Veterinarian_VetID) >= 2 THEN 'Moderate'
                       ELSE 'Light'
                   END as workloadCategory
            FROM Veterinarian v
            LEFT JOIN Veterinarian supervisor ON v.Veterinarian_VetID = supervisor.VetID
            INNER JOIN Appointment a ON v.VetID = a.Veterinarian_VetID
            INNER JOIN Pet p ON a.Pet_petID = p.petID
            INNER JOIN Owner o ON p.Owner_ownerID = o.ownerID
            INNER JOIN Breed b ON p.Breed_breedID = b.breedID
            WHERE a.appDateTime >= CURRENT_DATE - INTERVAL '365 days'
              AND p.birthYear >= 2015
            GROUP BY v.VetID, v.firstName, v.lastName, v.phoneNumber, supervisor.firstName, supervisor.lastName
            HAVING COUNT(DISTINCT a.Pet_petID || '-' || a.Veterinarian_VetID) >= 2
               AND COUNT(DISTINCT p.petID) >= 2
            ORDER BY totalAppointments DESC, 
                     avgPetAge ASC, 
                     v.lastName, 
                     v.firstName
            """;
        
        List<ComplexVetAnalysisDTO> analysisResults = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                ComplexVetAnalysisDTO analysis = new ComplexVetAnalysisDTO(
                    resultSet.getInt("VetID"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getString("phoneNumber"),
                    resultSet.getString("supervisorName"),
                    resultSet.getInt("totalAppointments"),
                    resultSet.getInt("uniquePetsServed"),
                    resultSet.getInt("uniqueOwnersServed"),
                    resultSet.getInt("breedVariety"),
                    resultSet.getDouble("avgPetAge"),
                    resultSet.getTimestamp("lastAppointmentDate"),
                    resultSet.getString("workloadCategory")
                );
                analysisResults.add(analysis);
            }
        }
        
        // Display results
        System.out.println("\n=== ACTIVE VETERINARIANS WORKLOAD ANALYSIS ===");
        System.out.println("Analysis of vets with moderate+ workload in the last year");
        System.out.println("=" .repeat(120));
        System.out.printf("%-4s %-12s %-12s %-15s %-15s %-8s %-8s %-8s %-8s %-8s %-12s%n",
            "ID", "FIRST", "LAST", "SUPERVISOR", "PHONE", "APPTS", "PETS", "OWNERS", "BREEDS", "AVG_AGE", "WORKLOAD");
        System.out.println("-".repeat(120));
        
        for (ComplexVetAnalysisDTO analysis : analysisResults) {
            analysis.display();
        }
        
        System.out.println("-".repeat(120));
        
        // Summary statistics
        if (!analysisResults.isEmpty()) {
            int totalVetsAnalyzed = analysisResults.size();
            double avgAppointments = analysisResults.stream().mapToInt(ComplexVetAnalysisDTO::getTotalAppointments).average().orElse(0.0);
            double avgPetAge = analysisResults.stream().mapToDouble(ComplexVetAnalysisDTO::getAvgPetAge).average().orElse(0.0);
            int heavyWorkload = (int) analysisResults.stream().filter(v -> v.getWorkloadCategory().contains("Heavy")).count();
            
                
        } else {
            System.out.println("No veterinarians found matching the criteria.");
            System.out.println("(This query requires vets with 2+ appointments for pets born after 2015)");
        }
    }
    
    /**
     * Complex Query 2: Veterinarian workload and hierarchy report
     */
    public void getVeterinarianWorkloadReport() throws SQLException {
        String query = """
            SELECT v.VetID, v.firstName, v.lastName, v.phoneNumber,
                   s.firstName || ' ' || s.lastName as supervisorName,
                   COUNT(DISTINCT a.appointmentID) as appointmentCount,
                   COUNT(DISTINCT sub.VetID) as subordinateCount,
                   CASE 
                       WHEN COUNT(DISTINCT a.appointmentID) = 0 THEN 'Light'
                       WHEN COUNT(DISTINCT a.appointmentID) <= 10 THEN 'Moderate'
                       ELSE 'Heavy'
                   END as workloadLevel
            FROM Veterinarian v
            LEFT JOIN Veterinarian s ON v.Veterinarian_VetID = s.VetID
            LEFT JOIN Appointment a ON v.VetID = a.Veterinarian_VetID
            LEFT JOIN Veterinarian sub ON v.VetID = sub.Veterinarian_VetID
            GROUP BY v.VetID, v.firstName, v.lastName, v.phoneNumber, s.firstName, s.lastName
            ORDER BY appointmentCount DESC, v.lastName, v.firstName
            """;
        
        List<VetWorkloadDTO> workloadReport = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                VetWorkloadDTO workload = new VetWorkloadDTO(
                    resultSet.getInt("VetID"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getString("phoneNumber"),
                    resultSet.getString("supervisorName"),
                    resultSet.getInt("appointmentCount"),
                    resultSet.getInt("subordinateCount"),
                    resultSet.getString("workloadLevel")
                );
                workloadReport.add(workload);
            }
        }
        
        // Display results
        System.out.println("\n=== VETERINARIAN WORKLOAD & HIERARCHY REPORT ===");
        System.out.println("=" .repeat(100));
        System.out.printf("%-4s %-15s %-15s %-15s %-15s %-12s %-12s %-10s%n",
            "ID", "FIRST NAME", "LAST NAME", "PHONE", "SUPERVISOR", "APPOINTMENTS", "SUBORDINATES", "WORKLOAD");
        System.out.println("-".repeat(100));
        
        for (VetWorkloadDTO workload : workloadReport) {
            workload.display();
        }
        
        System.out.println("-".repeat(100));
        
        // Summary statistics
        int totalVets = workloadReport.size();
        int supervisors = (int) workloadReport.stream().filter(v -> v.getSubordinateCount() > 0).count();
        int heavyWorkload = (int) workloadReport.stream().filter(v -> "Heavy".equals(v.getWorkloadLevel())).count();
        double avgAppointments = workloadReport.stream().mapToInt(VetWorkloadDTO::getAppointmentCount).average().orElse(0.0);
        
        System.out.printf("SUMMARY: %d total vets | %d supervisors | %d with heavy workload | %.1f avg appointments%n",
            totalVets, supervisors, heavyWorkload, avgAppointments);
    }
    
    // DTO Classes
    public static class OwnerWithPetsDTO {
        private int ownerID;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private java.util.Date birthDate;
        private List<PetInfoDTO> pets;
        
        public OwnerWithPetsDTO(int ownerID, String firstName, String lastName, String phoneNumber, java.util.Date birthDate) {
            this.ownerID = ownerID;
            this.firstName = firstName;
            this.lastName = lastName;
            this.phoneNumber = phoneNumber;
            this.birthDate = birthDate;
            this.pets = new ArrayList<>();
        }
        
        public void addPet(PetInfoDTO pet) {
            this.pets.add(pet);
        }
        
        public void display() {
            System.out.printf("Owner: %s %s (ID: %d) | Phone: %s%n", 
                firstName, lastName, ownerID, phoneNumber);
            
            if (pets.isEmpty()) {
                System.out.println("  Pets: NO PETS");
            } else {
                System.out.println("  Pets:");
                for (PetInfoDTO pet : pets) {
                    System.out.printf("    • %s (%s, %s, born %d)%n", 
                        pet.getName(), pet.getSpecies(), pet.getBreedName(), pet.getBirthYear());
                }
                System.out.printf("  Total pets: %d%n", pets.size());
            }
        }
        
        // Getters
        public int getOwnerID() { return ownerID; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public List<PetInfoDTO> getPets() { return pets; }
    }
    
    public static class PetInfoDTO {
        private int petID;
        private String name;
        private int birthYear;
        private String species;
        private String breedName;
        
        public PetInfoDTO(int petID, String name, int birthYear, String species, String breedName) {
            this.petID = petID;
            this.name = name;
            this.birthYear = birthYear;
            this.species = species;
            this.breedName = breedName;
        }
        
        // Getters
        public int getPetID() { return petID; }
        public String getName() { return name; }
        public int getBirthYear() { return birthYear; }
        public String getSpecies() { return species; }
        public String getBreedName() { return breedName; }
    }
    
    public static class VetWorkloadDTO {
        private int vetID;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String supervisorName;
        private int appointmentCount;
        private int subordinateCount;
        private String workloadLevel;
        
        public VetWorkloadDTO(int vetID, String firstName, String lastName, String phoneNumber,
                             String supervisorName, int appointmentCount, int subordinateCount, String workloadLevel) {
            this.vetID = vetID;
            this.firstName = firstName;
            this.lastName = lastName;
            this.phoneNumber = phoneNumber;
            this.supervisorName = supervisorName;
            this.appointmentCount = appointmentCount;
            this.subordinateCount = subordinateCount;
            this.workloadLevel = workloadLevel;
        }
        
        public void display() {
            System.out.printf("%-4d %-15s %-15s %-15s %-15s %-12d %-12d %-10s%n",
                vetID, firstName, lastName, phoneNumber,
                supervisorName != null ? supervisorName : "N/A",
                appointmentCount, subordinateCount, workloadLevel);
        }
        
        // Getters
        public int getVetID() { return vetID; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public int getAppointmentCount() { return appointmentCount; }
        public int getSubordinateCount() { return subordinateCount; }
        public String getWorkloadLevel() { return workloadLevel; }
    }

    public static class ComplexVetAnalysisDTO {
        private int vetID;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String supervisorName;
        private int totalAppointments;
        private int uniquePetsServed;
        private int uniqueOwnersServed;
        private int breedVariety;
        private double avgPetAge;
        private java.sql.Timestamp lastAppointmentDate;
        private String workloadCategory;
        
        public ComplexVetAnalysisDTO(int vetID, String firstName, String lastName, String phoneNumber,
                                   String supervisorName, int totalAppointments, int uniquePetsServed, 
                                   int uniqueOwnersServed, int breedVariety, double avgPetAge,
                                   java.sql.Timestamp lastAppointmentDate, String workloadCategory) {
            this.vetID = vetID;
            this.firstName = firstName;
            this.lastName = lastName;
            this.phoneNumber = phoneNumber;
            this.supervisorName = supervisorName;
            this.totalAppointments = totalAppointments;
            this.uniquePetsServed = uniquePetsServed;
            this.uniqueOwnersServed = uniqueOwnersServed;
            this.breedVariety = breedVariety;
            this.avgPetAge = avgPetAge;
            this.lastAppointmentDate = lastAppointmentDate;
            this.workloadCategory = workloadCategory;
        }
        
        public void display() {
            System.out.printf("%-4d %-12s %-12s %-15s %-15s %-8d %-8d %-8d %-8d %-8.1f %-12s%n",
                vetID, firstName, lastName,
                supervisorName != null ? supervisorName : "N/A",
                phoneNumber.length() > 15 ? phoneNumber.substring(0, 12) + "..." : phoneNumber,
                totalAppointments, uniquePetsServed, uniqueOwnersServed, breedVariety, 
                avgPetAge, workloadCategory);
        }
        
        // Getters
        public int getVetID() { return vetID; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public int getTotalAppointments() { return totalAppointments; }
        public double getAvgPetAge() { return avgPetAge; }
        public String getWorkloadCategory() { return workloadCategory; }
    }
}