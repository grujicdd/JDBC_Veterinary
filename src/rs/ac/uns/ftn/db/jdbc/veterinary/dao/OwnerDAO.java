package rs.ac.uns.ftn.db.jdbc.veterinary.dao;

import java.sql.SQLException;
import java.util.List;

import rs.ac.uns.ftn.db.jdbc.veterinary.model.Owner;

public interface OwnerDAO extends CRUDDao<Owner, Integer> {
    
    // Find owners by name (partial match)
    List<Owner> findByName(String name) throws SQLException;
    
    // Find owners with pets that have appointments in a date range
    List<Owner> findOwnersWithRecentAppointments(int days) throws SQLException;
}
