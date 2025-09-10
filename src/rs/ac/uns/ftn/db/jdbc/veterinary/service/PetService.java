package rs.ac.uns.ftn.db.jdbc.veterinary.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.db.jdbc.veterinary.dao.PetDAO;
import rs.ac.uns.ftn.db.jdbc.veterinary.dao.impl.PetDAOImpl;
import rs.ac.uns.ftn.db.jdbc.veterinary.model.Pet;

public class PetService {
    private static final PetDAO petDAO = new PetDAOImpl();

    public ArrayList<Pet> getAll() throws SQLException {
        return (ArrayList<Pet>) petDAO.findAll();
    }

    public Pet getById(int id) throws SQLException {
        return petDAO.findById(id);
    }

    public boolean existsById(int id) throws SQLException {
        return petDAO.existsById(id);
    }

    public boolean save(Pet pet) throws SQLException {
        return petDAO.save(pet);
    }

    public boolean deleteById(int id) throws SQLException {
        return petDAO.deleteById(id);
    }

    public int saveAll(List<Pet> petList) throws SQLException {
        return petDAO.saveAll(petList);
    }

    public List<Pet> findByOwnerID(Integer ownerID) throws SQLException {
        return petDAO.findByOwnerID(ownerID);
    }

    public List<Pet> findBySpecies(String species) throws SQLException {
        return petDAO.findBySpecies(species);
    }

    public List<Pet> findByBreedID(int breedID) throws SQLException {
        return petDAO.findByBreedID(breedID);
    }

    public List<Pet> findPetsWithoutAppointments() throws SQLException {
        return petDAO.findPetsWithoutAppointments();
    }

    public List<Pet> findPetsWithGeneticRisks() throws SQLException {
        return petDAO.findPetsWithGeneticRisks();
    }

    public int getCount() throws SQLException {
        return petDAO.count();
    }
}
