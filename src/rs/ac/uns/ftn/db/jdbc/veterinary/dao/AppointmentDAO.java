package rs.ac.uns.ftn.db.jdbc.veterinary.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import rs.ac.uns.ftn.db.jdbc.veterinary.model.Appointment;

public interface AppointmentDAO {
    
    // Basic CRUD operations
    boolean save(Appointment appointment) throws SQLException;
    
    boolean deleteById(int appointmentID) throws SQLException;
    
    Appointment findById(int appointmentID) throws SQLException;
    
    List<Appointment> findAll() throws SQLException;
    
    // Find appointments by pet
    List<Appointment> findByPetID(int petID) throws SQLException;
    
    // Find appointments by veterinarian
    List<Appointment> findByVetID(int vetID) throws SQLException;
    
    // Find appointments in date range
    List<Appointment> findByDateRange(Date startDate, Date endDate) throws SQLException;
    
    // Find appointments by reason
    List<Appointment> findByReason(String reason) throws SQLException;
    
    // Get appointment statistics
    int getAppointmentCount() throws SQLException;
    
    // Get appointments with diagnosis count
    List<Appointment> findAppointmentsWithDiagnosis() throws SQLException;
    
    // Check if appointment exists by pet and vet (for business logic)
    boolean existsByPetAndVet(int petID, int vetID) throws SQLException;
    
    // Find appointments by pet and vet (can be multiple now)
    List<Appointment> findByPetAndVet(int petID, int vetID) throws SQLException;
}