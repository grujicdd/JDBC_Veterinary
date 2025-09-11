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
import rs.ac.uns.ftn.db.jdbc.veterinary.dto.complexquery1.OwnerWithPetsDTO;
import rs.ac.uns.ftn.db.jdbc.veterinary.dto.complexquery1.PetInfoDTO;
import rs.ac.uns.ftn.db.jdbc.veterinary.dto.complexquery2.ComplexVetAnalysisDTO;
import rs.ac.uns.ftn.db.jdbc.veterinary.dto.complexquery2.VetWorkloadDTO;
import rs.ac.uns.ftn.db.jdbc.veterinary.model.Owner;
import rs.ac.uns.ftn.db.jdbc.veterinary.model.Pet;
import rs.ac.uns.ftn.db.jdbc.veterinary.ui_handler.MainUIHandler;

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
            
            System.out.printf("SUMMARY: %d vets analyzed | %.1f avg appointments | %.1f avg pet age | %d heavy workload%n",
                totalVetsAnalyzed, avgAppointments, avgPetAge, heavyWorkload);
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
    
    /**
     * Complex Transaction: Transfer pet ownership from one owner to another
     * This affects multiple tables and maintains data integrity using transactions
     */
    public void transferPetOwnership() throws SQLException {
        System.out.println("\n=== PET OWNERSHIP TRANSFER TRANSACTION ===");
        System.out.println("This complex transaction will:");
        System.out.println("1. Validate pet and owners exist");
        System.out.println("2. Update pet ownership in Pet table");
        System.out.println("3. Check and update future appointments");
        System.out.println("4. Create transfer appointment and log entry");
        System.out.println("=" .repeat(80));
        
        // Get input from user
        System.out.print("Enter Pet ID to transfer: ");
        int petID = Integer.parseInt(MainUIHandler.sc.nextLine());
        
        System.out.print("Enter new Owner ID: ");
        int newOwnerID = Integer.parseInt(MainUIHandler.sc.nextLine());
        
        System.out.print("Enter reason for transfer: ");
        String transferReason = MainUIHandler.sc.nextLine();
        if (transferReason.trim().isEmpty()) {
            transferReason = "Ownership transfer";
        }
        
        Connection connection = null;
        
        try {
            connection = ConnectionUtil_HikariCP.getConnection();
            connection.setAutoCommit(false); // Start transaction
            
            // Step 1: Validate pet exists and get current details
            Pet pet = validateAndGetPet(connection, petID);
            if (pet == null) {
                System.out.println("ERROR: Pet with ID " + petID + " not found");
                return;
            }
            
            if (pet.getOwnerID() == null) {
                System.out.println("ERROR: Pet has no current owner to transfer from");
                return;
            }
            
            if (pet.getOwnerID().equals(newOwnerID)) {
                System.out.println("ERROR: Pet is already owned by owner " + newOwnerID);
                return;
            }
            
            // Get current owner details
            Owner currentOwner = validateAndGetOwner(connection, pet.getOwnerID());
            
            // Step 2: Validate new owner exists
            Owner newOwner = validateAndGetOwner(connection, newOwnerID);
            if (newOwner == null) {
                System.out.println("ERROR: New owner with ID " + newOwnerID + " not found");
                return;
            }
            
            System.out.printf("\nTransferring %s (ID: %d) from %s %s to %s %s%n",
                pet.getName(), pet.getPetID(),
                currentOwner.getFirstName(), currentOwner.getLastName(),
                newOwner.getFirstName(), newOwner.getLastName());
            
            // Step 3: Check for future appointments that will be affected
            int futureAppointments = countFutureAppointments(connection, petID);
            if (futureAppointments > 0) {
                System.out.printf("Note: %d future appointments will be updated with transfer information%n", futureAppointments);
            }
            
            // Step 4: Update pet ownership
            updatePetOwnership(connection, petID, newOwnerID);
            System.out.println("✓ Updated pet ownership in Pet table");
            
            // Step 5: Update future appointments with transfer notes
            int updatedAppointments = updateFutureAppointments(connection, petID, transferReason);
            if (updatedAppointments > 0) {
                System.out.printf("✓ Updated %d future appointments with transfer note%n", updatedAppointments);
            }
            
            // Step 6: Create transfer appointment for new owner orientation
            int transferAppointmentID = createTransferAppointment(connection, petID, transferReason);
            System.out.printf("✓ Created transfer appointment (ID: %d) for new owner orientation%n", transferAppointmentID);
            
            // Commit transaction
            connection.commit();
            System.out.println("\n🎉 OWNERSHIP TRANSFER COMPLETED SUCCESSFULLY!");
            
            // Show final status
            System.out.println("\nTransfer Summary:");
            System.out.printf("  Pet: %s (ID: %d)%n", pet.getName(), pet.getPetID());
            System.out.printf("  From: %s %s (ID: %d)%n", 
                currentOwner.getFirstName(), currentOwner.getLastName(), currentOwner.getOwnerID());
            System.out.printf("  To: %s %s (ID: %d)%n", 
                newOwner.getFirstName(), newOwner.getLastName(), newOwner.getOwnerID());
            System.out.printf("  Reason: %s%n", transferReason);
            System.out.printf("  Appointments affected: %d%n", updatedAppointments);
            System.out.printf("  Transfer appointment ID: %d%n", transferAppointmentID);
            
        } catch (SQLException e) {
            // Rollback transaction on error
            if (connection != null) {
                try {
                    connection.rollback();
                    System.err.println("❌ TRANSACTION ROLLED BACK due to error: " + e.getMessage());
                } catch (SQLException rollbackEx) {
                    System.err.println("ERROR: Could not rollback transaction: " + rollbackEx.getMessage());
                }
            }
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
    
    private Pet validateAndGetPet(Connection connection, int petID) throws SQLException {
        String query = "SELECT petID, name, birthYear, species, Owner_ownerID, Breed_breedID FROM Pet WHERE petID = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, petID);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Pet(
                        rs.getInt("petID"),
                        rs.getString("name"),
                        rs.getInt("birthYear"),
                        rs.getString("species"),
                        (Integer) rs.getObject("Owner_ownerID"),
                        rs.getInt("Breed_breedID")
                    );
                }
            }
        }
        return null;
    }
    
    private Owner validateAndGetOwner(Connection connection, int ownerID) throws SQLException {
        String query = "SELECT ownerID, firstName, lastName, phoneNumber, birthDate FROM Owner WHERE ownerID = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, ownerID);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Owner(
                        rs.getInt("ownerID"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("phoneNumber"),
                        rs.getDate("birthDate")
                    );
                }
            }
        }
        return null;
    }
    
    private int countFutureAppointments(Connection connection, int petID) throws SQLException {
        String query = "SELECT COUNT(*) FROM Appointment WHERE Pet_petID = ? AND appDateTime > CURRENT_TIMESTAMP";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, petID);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    
    private void updatePetOwnership(Connection connection, int petID, int newOwnerID) throws SQLException {
        String query = "UPDATE Pet SET Owner_ownerID = ? WHERE petID = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, newOwnerID);
            ps.setInt(2, petID);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new SQLException("Failed to update pet ownership - unexpected number of rows affected: " + rowsAffected);
            }
        }
    }
    
    private int updateFutureAppointments(Connection connection, int petID, String transferReason) throws SQLException {
        String query = """
            UPDATE Appointment 
            SET reason = CASE 
                WHEN reason IS NULL THEN 'Owner changed: ' || ?
                ELSE reason || ' (Owner changed: ' || ? || ')'
            END
            WHERE Pet_petID = ? AND appDateTime > CURRENT_TIMESTAMP
            """;
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, transferReason);
            ps.setString(2, transferReason);
            ps.setInt(3, petID);
            
            return ps.executeUpdate();
        }
    }
    
    private int createTransferAppointment(Connection connection, int petID, String transferReason) throws SQLException {
        String query = """
            INSERT INTO Appointment (Pet_petID, Veterinarian_VetID, appDateTime, reason) 
            VALUES (?, 1, CURRENT_TIMESTAMP + INTERVAL '7 days', ?)
            """;
        
        String appointmentReason = "New owner orientation meeting - " + transferReason;
        
        try (PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, petID);
            ps.setString(2, appointmentReason);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new SQLException("Failed to create transfer appointment");
            }
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve generated appointment ID");
                }
            }
        }
    }
}