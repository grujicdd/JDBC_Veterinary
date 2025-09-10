package rs.ac.uns.ftn.db.jdbc.veterinary.dao;

import java.sql.SQLException;
import java.util.List;

import rs.ac.uns.ftn.db.jdbc.veterinary.model.Veterinarian;

public interface VeterinarianDAO extends CRUDDao<Veterinarian, Integer> {
    
    // Find veterinarians by name (partial match)
    List<Veterinarian> findByName(String name) throws SQLException;
    
    // Find veterinarians by supervisor ID
    List<Veterinarian> findBySupervisor(Integer supervisorID) throws SQLException;
    
    // Find all supervisors (veterinarians who supervise others)
    List<Veterinarian> findSupervisors() throws SQLException;
    
    // Find veterinarians without supervisors (top-level)
    List<Veterinarian> findTopLevel() throws SQLException;
    
    // Get veterinarian hierarchy
    List<VeterinarianHierarchyDTO> getVeterinarianHierarchy() throws SQLException;
    
    // DTO for veterinarian hierarchy
    public static class VeterinarianHierarchyDTO {
        private Veterinarian veterinarian;
        private String supervisorName;
        private int subordinateCount;
        
        public VeterinarianHierarchyDTO(Veterinarian veterinarian, String supervisorName, int subordinateCount) {
            this.veterinarian = veterinarian;
            this.supervisorName = supervisorName;
            this.subordinateCount = subordinateCount;
        }
        
        // Getters
        public Veterinarian getVeterinarian() { return veterinarian; }
        public String getSupervisorName() { return supervisorName; }
        public int getSubordinateCount() { return subordinateCount; }
        
        @Override
        public String toString() {
            return String.format("%-6d %-15s %-15s %-15s %-15s %-12d", 
                veterinarian.getVetID(),
                veterinarian.getFirstName(),
                veterinarian.getLastName(),
                veterinarian.getPhoneNumber(),
                supervisorName != null ? supervisorName : "N/A",
                subordinateCount);
        }
        
        public static String getFormattedHeader() {
            return String.format("%-6s %-15s %-15s %-15s %-15s %-12s", 
                "ID", "FIRST NAME", "LAST NAME", "PHONE", "SUPERVISOR", "SUBORDINATES");
        }
    }
}
