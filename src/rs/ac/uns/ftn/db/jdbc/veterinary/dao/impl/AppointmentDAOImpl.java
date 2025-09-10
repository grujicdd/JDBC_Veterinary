package rs.ac.uns.ftn.db.jdbc.veterinary.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rs.ac.uns.ftn.db.jdbc.veterinary.connection.ConnectionUtil_HikariCP;
import rs.ac.uns.ftn.db.jdbc.veterinary.dao.AppointmentDAO;
import rs.ac.uns.ftn.db.jdbc.veterinary.model.Appointment;

public class AppointmentDAOImpl implements AppointmentDAO {

    @Override
    public boolean save(Appointment appointment) throws SQLException {
        String query = """
            INSERT INTO Appointment (Pet_petID, Veterinarian_VetID, appDateTime, reason) 
            VALUES (?, ?, ?, ?)
            ON CONFLICT (Pet_petID, Veterinarian_VetID) 
            DO UPDATE SET appDateTime = EXCLUDED.appDateTime, reason = EXCLUDED.reason
            """;
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, appointment.getPetID());
            preparedStatement.setInt(2, appointment.getVetID());
            preparedStatement.setTimestamp(3, new Timestamp(appointment.getAppDateTime().getTime()));
            preparedStatement.setString(4, appointment.getReason());
            
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    @Override
    public boolean deleteByPetAndVet(int petID, int vetID) throws SQLException {
        String query = "DELETE FROM Appointment WHERE Pet_petID = ? AND Veterinarian_VetID = ?";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, petID);
            preparedStatement.setInt(2, vetID);
            
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }

    @Override
    public Appointment findByPetAndVet(int petID, int vetID) throws SQLException {
        String query = """
            SELECT Pet_petID, Veterinarian_VetID, appDateTime, reason 
            FROM Appointment 
            WHERE Pet_petID = ? AND Veterinarian_VetID = ?
            """;
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, petID);
            preparedStatement.setInt(2, vetID);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Appointment(
                        resultSet.getInt("Pet_petID"),
                        resultSet.getInt("Veterinarian_VetID"),
                        resultSet.getTimestamp("appDateTime"),
                        resultSet.getString("reason")
                    );
                }
            }
        }
        
        return null;
    }

    @Override
    public List<Appointment> findAll() throws SQLException {
        String query = """
            SELECT a.Pet_petID, a.Veterinarian_VetID, a.appDateTime, a.reason,
                   p.name AS pet_name, v.firstName || ' ' || v.lastName AS vet_name
            FROM Appointment a
            JOIN Pet p ON a.Pet_petID = p.petID
            JOIN Veterinarian v ON a.Veterinarian_VetID = v.VetID
            ORDER BY a.appDateTime DESC
            """;
        
        List<Appointment> appointments = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Appointment appointment = new Appointment(
                    resultSet.getInt("Pet_petID"),
                    resultSet.getInt("Veterinarian_VetID"),
                    resultSet.getTimestamp("appDateTime"),
                    resultSet.getString("reason")
                );
                appointments.add(appointment);
            }
        }
        
        return appointments;
    }

    @Override
    public List<Appointment> findByPetID(int petID) throws SQLException {
        String query = """
            SELECT a.Pet_petID, a.Veterinarian_VetID, a.appDateTime, a.reason,
                   v.firstName || ' ' || v.lastName AS vet_name
            FROM Appointment a
            JOIN Veterinarian v ON a.Veterinarian_VetID = v.VetID
            WHERE a.Pet_petID = ?
            ORDER BY a.appDateTime DESC
            """;
        
        List<Appointment> appointments = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, petID);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Appointment appointment = new Appointment(
                        resultSet.getInt("Pet_petID"),
                        resultSet.getInt("Veterinarian_VetID"),
                        resultSet.getTimestamp("appDateTime"),
                        resultSet.getString("reason")
                    );
                    appointments.add(appointment);
                }
            }
        }
        
        return appointments;
    }

    @Override
    public List<Appointment> findByVetID(int vetID) throws SQLException {
        String query = """
            SELECT a.Pet_petID, a.Veterinarian_VetID, a.appDateTime, a.reason,
                   p.name AS pet_name
            FROM Appointment a
            JOIN Pet p ON a.Pet_petID = p.petID
            WHERE a.Veterinarian_VetID = ?
            ORDER BY a.appDateTime DESC
            """;
        
        List<Appointment> appointments = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, vetID);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Appointment appointment = new Appointment(
                        resultSet.getInt("Pet_petID"),
                        resultSet.getInt("Veterinarian_VetID"),
                        resultSet.getTimestamp("appDateTime"),
                        resultSet.getString("reason")
                    );
                    appointments.add(appointment);
                }
            }
        }
        
        return appointments;
    }

    @Override
    public List<Appointment> findByDateRange(Date startDate, Date endDate) throws SQLException {
        String query = """
            SELECT a.Pet_petID, a.Veterinarian_VetID, a.appDateTime, a.reason,
                   p.name AS pet_name, v.firstName || ' ' || v.lastName AS vet_name
            FROM Appointment a
            JOIN Pet p ON a.Pet_petID = p.petID
            JOIN Veterinarian v ON a.Veterinarian_VetID = v.VetID
            WHERE a.appDateTime BETWEEN ? AND ?
            ORDER BY a.appDateTime
            """;
        
        List<Appointment> appointments = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setTimestamp(1, new Timestamp(startDate.getTime()));
            preparedStatement.setTimestamp(2, new Timestamp(endDate.getTime()));
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Appointment appointment = new Appointment(
                        resultSet.getInt("Pet_petID"),
                        resultSet.getInt("Veterinarian_VetID"),
                        resultSet.getTimestamp("appDateTime"),
                        resultSet.getString("reason")
                    );
                    appointments.add(appointment);
                }
            }
        }
        
        return appointments;
    }

    @Override
    public List<Appointment> findByReason(String reason) throws SQLException {
        String query = """
            SELECT a.Pet_petID, a.Veterinarian_VetID, a.appDateTime, a.reason,
                   p.name AS pet_name, v.firstName || ' ' || v.lastName AS vet_name
            FROM Appointment a
            JOIN Pet p ON a.Pet_petID = p.petID
            JOIN Veterinarian v ON a.Veterinarian_VetID = v.VetID
            WHERE UPPER(a.reason) LIKE UPPER(?)
            ORDER BY a.appDateTime DESC
            """;
        
        List<Appointment> appointments = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, "%" + reason + "%");
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Appointment appointment = new Appointment(
                        resultSet.getInt("Pet_petID"),
                        resultSet.getInt("Veterinarian_VetID"),
                        resultSet.getTimestamp("appDateTime"),
                        resultSet.getString("reason")
                    );
                    appointments.add(appointment);
                }
            }
        }
        
        return appointments;
    }

    @Override
    public int getAppointmentCount() throws SQLException {
        String query = "SELECT COUNT(*) FROM Appointment";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        
        return 0;
    }

    @Override
    public List<Appointment> findAppointmentsWithDiagnosis() throws SQLException {
        String query = """
            SELECT DISTINCT a.Pet_petID, a.Veterinarian_VetID, a.appDateTime, a.reason,
                   p.name AS pet_name, v.firstName || ' ' || v.lastName AS vet_name,
                   COUNT(d.diagnosisID) AS diagnosis_count
            FROM Appointment a
            JOIN Pet p ON a.Pet_petID = p.petID
            JOIN Veterinarian v ON a.Veterinarian_VetID = v.VetID
            JOIN Diagnosis d ON a.Pet_petID = d.Appointment_Pet_petID 
                AND a.Veterinarian_VetID = d.Appointment_Veterinarian_VetID
            GROUP BY a.Pet_petID, a.Veterinarian_VetID, a.appDateTime, a.reason, p.name, v.firstName, v.lastName
            HAVING COUNT(d.diagnosisID) > 0
            ORDER BY diagnosis_count DESC, a.appDateTime DESC
            """;
        
        List<Appointment> appointments = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Appointment appointment = new Appointment(
                    resultSet.getInt("Pet_petID"),
                    resultSet.getInt("Veterinarian_VetID"),
                    resultSet.getTimestamp("appDateTime"),
                    resultSet.getString("reason")
                );
                appointments.add(appointment);
            }
        }
        
        return appointments;
    }
}