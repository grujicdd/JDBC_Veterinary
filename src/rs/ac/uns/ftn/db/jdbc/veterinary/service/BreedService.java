package rs.ac.uns.ftn.db.jdbc.veterinary.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.db.jdbc.veterinary.dao.BreedDAO;
import rs.ac.uns.ftn.db.jdbc.veterinary.dao.impl.BreedDAOImpl;
import rs.ac.uns.ftn.db.jdbc.veterinary.model.Breed;
import rs.ac.uns.ftn.db.jdbc.veterinary.dao.BreedDAO.BreedStatsDTO;

public class BreedService {
    private static final BreedDAO breedDAO = new BreedDAOImpl();

    public ArrayList<Breed> getAll() throws SQLException {
        return (ArrayList<Breed>) breedDAO.findAll();
    }

    public Breed getById(int id) throws SQLException {
        return breedDAO.findById(id);
    }

    public boolean existsById(int id) throws SQLException {
        return breedDAO.existsById(id);
    }

    public boolean save(Breed breed) throws SQLException {
        return breedDAO.save(breed);
    }

    public boolean deleteById(int id) throws SQLException {
        return breedDAO.deleteById(id);
    }

    public int saveAll(List<Breed> breedList) throws SQLException {
        return breedDAO.saveAll(breedList);
    }

    public List<Breed> findByName(String name) throws SQLException {
        return breedDAO.findByName(name);
    }

    public List<Breed> findBreedsWithGeneticRisks() throws SQLException {
        return breedDAO.findBreedsWithGeneticRisks();
    }

    public List<Breed> findBreedsByRiskLevel(String riskLevel) throws SQLException {
        return breedDAO.findBreedsByRiskLevel(riskLevel);
    }

    public List<BreedStatsDTO> getBreedStatistics() throws SQLException {
        return breedDAO.getBreedStatistics();
    }

    public int getCount() throws SQLException {
        return breedDAO.count();
    }
}