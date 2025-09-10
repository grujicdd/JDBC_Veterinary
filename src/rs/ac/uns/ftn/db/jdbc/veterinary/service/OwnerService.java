package rs.ac.uns.ftn.db.jdbc.veterinary.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.db.jdbc.veterinary.dao.OwnerDAO;
import rs.ac.uns.ftn.db.jdbc.veterinary.dao.impl.OwnerDAOImpl;
import rs.ac.uns.ftn.db.jdbc.veterinary.model.Owner;

public class OwnerService {
    private static final OwnerDAO ownerDAO = new OwnerDAOImpl();

    public ArrayList<Owner> getAll() throws SQLException {
        return (ArrayList<Owner>) ownerDAO.findAll();
    }

    public Owner getById(int id) throws SQLException {
        return ownerDAO.findById(id);
    }

    public boolean existsById(int id) throws SQLException {
        return ownerDAO.existsById(id);
    }

    public boolean save(Owner owner) throws SQLException {
        return ownerDAO.save(owner);
    }

    public boolean deleteById(int id) throws SQLException {
        return ownerDAO.deleteById(id);
    }

    public int saveAll(List<Owner> ownerList) throws SQLException {
        return ownerDAO.saveAll(ownerList);
    }

    public List<Owner> findByName(String name) throws SQLException {
        return ownerDAO.findByName(name);
    }

    public List<Owner> findOwnersWithRecentAppointments(int days) throws SQLException {
        return ownerDAO.findOwnersWithRecentAppointments(days);
    }

    public int getCount() throws SQLException {
        return ownerDAO.count();
    }
}
