package rs.ac.uns.ftn.db.jdbc.veterinary.dao;

import java.sql.SQLException;
import java.util.List;

import rs.ac.uns.ftn.db.jdbc.veterinary.model.Pet;

public interface PetDAO extends CRUDDao<Pet, Integer> {
    
    // Find pets by owner ID
    List<Pet> findByOwnerID(Integer ownerID) throws SQLException;
    
    // Find pets by species
    List<Pet> findBySpecies(String species) throws SQLException;
    
    // Find pets by breed
    List<Pet> findByBreedID(int breedID) throws SQLException;
    
    // Find pets with no appointments
    List<Pet> findPetsWithoutAppointments() throws SQLException;
    
    // Find pets with genetic predispositions
    List<Pet> findPetsWithGeneticRisks() throws SQLException;
}
