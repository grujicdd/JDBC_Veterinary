package rs.ac.uns.ftn.db.jdbc.veterinary.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.db.jdbc.veterinary.dao.VeterinarianDAO;
import rs.ac.uns.ftn.db.jdbc.veterinary.dao.impl.VeterinarianDAOImpl;
import rs.ac.uns.ftn.db.jdbc.veterinary.model.Veterinarian;
import rs.ac.uns.ftn.db.jdbc.veterinary.dao.VeterinarianDAO.VeterinarianHierarchyDTO;

public class VeterinarianService {
    private static final VeterinarianDAO veterinarianDAO = new VeterinarianDAOImpl();

    public ArrayList<Veterinarian> getAll() throws SQLException {
        return (ArrayList<Veterinarian>) veterinarianDAO.findAll();
    }

    public Veterinarian getById(int id) throws SQLException {
        return veterinarianDAO.findById(id);
    }

    public boolean existsById(int id) throws SQLException {
        return veterinarianDAO.existsById(id);
    }

    public boolean save(Veterinarian veterinarian) throws SQLException {
        return veterinarianDAO.save(veterinarian);
    }

    public boolean deleteById(int id) throws SQLException {
        return veterinarianDAO.deleteById(id);
    }

    public int saveAll(List<Veterinarian> veterinarianList) throws SQLException {
        return veterinarianDAO.saveAll(veterinarianList);
    }

    public List<Veterinarian> findByName(String name) throws SQLException {
        return veterinarianDAO.findByName(name);
    }

    public List<Veterinarian> findBySupervisor(Integer supervisorID) throws SQLException {
        return veterinarianDAO.findBySupervisor(supervisorID);
    }

    public List<Veterinarian> findSupervisors() throws SQLException {
        return veterinarianDAO.findSupervisors();
    }

    public List<Veterinarian> findTopLevel() throws SQLException {
        return veterinarianDAO.findTopLevel();
    }

    public List<VeterinarianHierarchyDTO> getVeterinarianHierarchy() throws SQLException {
        return veterinarianDAO.getVeterinarianHierarchy();
    }

    public int getCount() throws SQLException {
        return veterinarianDAO.count();
    }
}