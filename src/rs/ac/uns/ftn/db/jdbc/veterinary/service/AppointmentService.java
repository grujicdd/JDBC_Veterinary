package rs.ac.uns.ftn.db.jdbc.veterinary.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import rs.ac.uns.ftn.db.jdbc.veterinary.dao.AppointmentDAO;
import rs.ac.uns.ftn.db.jdbc.veterinary.dao.impl.AppointmentDAOImpl;
import rs.ac.uns.ftn.db.jdbc.veterinary.model.Appointment;

public class AppointmentService {
    private static final AppointmentDAO appointmentDAO = new AppointmentDAOImpl();

    public boolean save(Appointment appointment) throws SQLException {
        return appointmentDAO.save(appointment);
    }

    public boolean deleteById(int appointmentID) throws SQLException {
        return appointmentDAO.deleteById(appointmentID);
    }

    public Appointment findById(int appointmentID) throws SQLException {
        return appointmentDAO.findById(appointmentID);
    }

    public List<Appointment> getAll() throws SQLException {
        return appointmentDAO.findAll();
    }

    public List<Appointment> findByPetID(int petID) throws SQLException {
        return appointmentDAO.findByPetID(petID);
    }

    public List<Appointment> findByVetID(int vetID) throws SQLException {
        return appointmentDAO.findByVetID(vetID);
    }

    public List<Appointment> findByDateRange(Date startDate, Date endDate) throws SQLException {
        return appointmentDAO.findByDateRange(startDate, endDate);
    }

    public List<Appointment> findByReason(String reason) throws SQLException {
        return appointmentDAO.findByReason(reason);
    }

    public int getAppointmentCount() throws SQLException {
        return appointmentDAO.getAppointmentCount();
    }

    public List<Appointment> findAppointmentsWithDiagnosis() throws SQLException {
        return appointmentDAO.findAppointmentsWithDiagnosis();
    }

    // New methods for the updated schema
    public boolean existsByPetAndVet(int petID, int vetID) throws SQLException {
        return appointmentDAO.existsByPetAndVet(petID, vetID);
    }

    public List<Appointment> findByPetAndVet(int petID, int vetID) throws SQLException {
        return appointmentDAO.findByPetAndVet(petID, vetID);
    }

    // Convenience method to get the most recent appointment for a pet-vet combination
    public Appointment findMostRecentByPetAndVet(int petID, int vetID) throws SQLException {
        List<Appointment> appointments = appointmentDAO.findByPetAndVet(petID, vetID);
        return appointments.isEmpty() ? null : appointments.get(0); // First one is most recent due to ORDER BY
    }

    // Business logic method to check if a pet can have another appointment with the same vet
    public boolean canScheduleAppointment(int petID, int vetID, Date proposedDateTime) throws SQLException {
        List<Appointment> existingAppointments = findByPetAndVet(petID, vetID);
        
        // Business rule: no appointments within 24 hours of each other for same pet-vet combination
        long oneDayInMillis = 24 * 60 * 60 * 1000;
        
        for (Appointment existing : existingAppointments) {
            long timeDifference = Math.abs(existing.getAppDateTime().getTime() - proposedDateTime.getTime());
            if (timeDifference < oneDayInMillis) {
                return false; // Too close to existing appointment
            }
        }
        
        return true;
    }
}