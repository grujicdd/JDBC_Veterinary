package rs.ac.uns.ftn.db.jdbc.veterinary.dao;

import java.sql.SQLException;
import java.util.List;

import rs.ac.uns.ftn.db.jdbc.veterinary.model.Breed;

public interface BreedDAO extends CRUDDao<Breed, Integer> {
    
    // Find breeds by name (partial match)
    List<Breed> findByName(String name) throws SQLException;
    
    // Find breeds with genetic predispositions
    List<Breed> findBreedsWithGeneticRisks() throws SQLException;
    
    // Find breeds by risk level
    List<Breed> findBreedsByRiskLevel(String riskLevel) throws SQLException;
    
    // Get breed statistics (number of pets per breed)
    List<BreedStatsDTO> getBreedStatistics() throws SQLException;
    
    // DTO for breed statistics
    public static class BreedStatsDTO {
        private int breedID;
        private String breedName;
        private int petCount;
        private int dogCount;
        private int catCount;
        
        public BreedStatsDTO(int breedID, String breedName, int petCount, int dogCount, int catCount) {
            this.breedID = breedID;
            this.breedName = breedName;
            this.petCount = petCount;
            this.dogCount = dogCount;
            this.catCount = catCount;
        }
        
        // Getters
        public int getBreedID() { return breedID; }
        public String getBreedName() { return breedName; }
        public int getPetCount() { return petCount; }
        public int getDogCount() { return dogCount; }
        public int getCatCount() { return catCount; }
        
        @Override
        public String toString() {
            return String.format("%-6d %-20s %-8d %-8d %-8d", 
                breedID, breedName, petCount, dogCount, catCount);
        }
        
        public static String getFormattedHeader() {
            return String.format("%-6s %-20s %-8s %-8s %-8s", 
                "ID", "BREED NAME", "TOTAL", "DOGS", "CATS");
        }
    }
}
