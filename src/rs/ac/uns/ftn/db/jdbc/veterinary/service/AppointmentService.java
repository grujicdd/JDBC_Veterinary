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

    public boolean deleteByPetAndVet(int petID, int vetID) throws SQLException {
        return appointmentDAO.deleteByPetAndVet(petID, vetID);
    }

    public Appointment findByPetAndVet(int petID, int vetID) throws SQLException {
        return appointmentDAO.findByPetAndVet(petID, vetID);
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
}