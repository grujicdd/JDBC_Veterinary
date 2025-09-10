package rs.ac.uns.ftn.db.jdbc.veterinary.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.db.jdbc.veterinary.connection.ConnectionUtil_HikariCP;
import rs.ac.uns.ftn.db.jdbc.veterinary.dao.VeterinarianDAO;
import rs.ac.uns.ftn.db.jdbc.veterinary.model.Veterinarian;

public class VeterinarianDAOImpl implements VeterinarianDAO {

    @Override
    public int count() throws SQLException {
        String query = "SELECT COUNT(*) FROM Veterinarian";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return -1;
            }
        }
    }

    @Override
    public boolean delete(Veterinarian entity) throws SQLException {
        return deleteById(entity.getVetID());
    }

    @Override
    public int deleteAll() throws SQLException {
        String query = "DELETE FROM Veterinarian";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            return preparedStatement.executeUpdate();
        }
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String query = "DELETE FROM Veterinarian WHERE VetID = ?";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }

    @Override
    public boolean existsById(Integer id) throws SQLException {
        String query = "SELECT 1 FROM Veterinarian WHERE VetID = ?";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public Iterable<Veterinarian> findAll() throws SQLException {
        String query = "SELECT VetID, firstName, lastName, phoneNumber, Veterinarian_VetID FROM Veterinarian ORDER BY lastName, firstName";
        List<Veterinarian> veterinarianList = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Veterinarian veterinarian = new Veterinarian(
                    resultSet.getInt("VetID"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getString("phoneNumber"),
                    (Integer) resultSet.getObject("Veterinarian_VetID")
                );
                veterinarianList.add(veterinarian);
            }
        }
        
        return veterinarianList;
    }

    @Override
    public Iterable<Veterinarian> findAllById(Iterable<Integer> ids) throws SQLException {
        List<Veterinarian> veterinarianList = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT VetID, firstName, lastName, phoneNumber, Veterinarian_VetID FROM Veterinarian WHERE VetID IN (");
        
        for (Integer id : ids) {
            queryBuilder.append("?,");
        }
        
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        queryBuilder.append(") ORDER BY lastName, firstName");
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString())) {
            
            int paramIndex = 1;
            for (Integer id : ids) {
                preparedStatement.setInt(paramIndex++, id);
            }
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Veterinarian veterinarian = new Veterinarian(
                        resultSet.getInt("VetID"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("phoneNumber"),
                        (Integer) resultSet.getObject("Veterinarian_VetID")
                    );
                    veterinarianList.add(veterinarian);
                }
            }
        }
        
        return veterinarianList;
    }

    @Override
    public Veterinarian findById(Integer id) throws SQLException {
        String query = "SELECT VetID, firstName, lastName, phoneNumber, Veterinarian_VetID FROM Veterinarian WHERE VetID = ?";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, id);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Veterinarian(
                        resultSet.getInt("VetID"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("phoneNumber"),
                        (Integer) resultSet.getObject("Veterinarian_VetID")
                    );
                }
            }
        }
        
        return null;
    }

    @Override
    public boolean save(Veterinarian entity) throws SQLException {
        if (existsById(entity.getVetID())) {
            return update(entity);
        } else {
            return insert(entity);
        }
    }

    private boolean insert(Veterinarian veterinarian) throws SQLException {
        String query = "INSERT INTO Veterinarian (VetID, firstName, lastName, phoneNumber, Veterinarian_VetID) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, veterinarian.getVetID());
            preparedStatement.setString(2, veterinarian.getFirstName());
            preparedStatement.setString(3, veterinarian.getLastName());
            preparedStatement.setString(4, veterinarian.getPhoneNumber());
            
            if (veterinarian.getSupervisorID() != null) {
                preparedStatement.setInt(5, veterinarian.getSupervisorID());
            } else {
                preparedStatement.setNull(5, java.sql.Types.INTEGER);
            }
            
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }

    private boolean update(Veterinarian veterinarian) throws SQLException {
        String query = "UPDATE Veterinarian SET firstName = ?, lastName = ?, phoneNumber = ?, Veterinarian_VetID = ? WHERE VetID = ?";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, veterinarian.getFirstName());
            preparedStatement.setString(2, veterinarian.getLastName());
            preparedStatement.setString(3, veterinarian.getPhoneNumber());
            
            if (veterinarian.getSupervisorID() != null) {
                preparedStatement.setInt(4, veterinarian.getSupervisorID());
            } else {
                preparedStatement.setNull(4, java.sql.Types.INTEGER);
            }
            
            preparedStatement.setInt(5, veterinarian.getVetID());
            
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }

    @Override
    public int saveAll(Iterable<Veterinarian> entities) throws SQLException {
        int saved = 0;
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection()) {
            connection.setAutoCommit(false);
            
            for (Veterinarian veterinarian : entities) {
                if (save(veterinarian)) {
                    saved++;
                }
            }
            
            connection.commit();
        }
        
        return saved;
    }

    @Override
    public List<Veterinarian> findByName(String name) throws SQLException {
        String query = "SELECT VetID, firstName, lastName, phoneNumber, Veterinarian_VetID " +
            "FROM Veterinarian " +
            "WHERE UPPER(firstName) LIKE UPPER(?) OR UPPER(lastName) LIKE UPPER(?) " + 
            "ORDER BY lastName, firstName ";
        List<Veterinarian> veterinarians = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            String searchPattern = "%" + name + "%";
            preparedStatement.setString(1, searchPattern);
            preparedStatement.setString(2, searchPattern);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Veterinarian veterinarian = new Veterinarian(
                        resultSet.getInt("VetID"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("phoneNumber"),
                        (Integer) resultSet.getObject("Veterinarian_VetID")
                    );
                    veterinarians.add(veterinarian);
                }
            }
        }
        
        return veterinarians;
    }

    @Override
    public List<Veterinarian> findBySupervisor(Integer supervisorID) throws SQLException {
        String query = "SELECT VetID, firstName, lastName, phoneNumber, Veterinarian_VetID " +
            "FROM Veterinarian " + 
            "WHERE Veterinarian_VetID = ? " + 
            "ORDER BY lastName, firstName ";
        List<Veterinarian> veterinarians = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, supervisorID);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Veterinarian veterinarian = new Veterinarian(
                        resultSet.getInt("VetID"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("phoneNumber"),
                        (Integer) resultSet.getObject("Veterinarian_VetID")
                    );
                    veterinarians.add(veterinarian);
                }
            }
        }
        
        return veterinarians;
    }

    @Override
    public List<Veterinarian> findSupervisors() throws SQLException {
        String query = "SELECT DISTINCT v.VetID, v.firstName, v.lastName, v.phoneNumber, v.Veterinarian_VetID " +
            "FROM Veterinarian v " + 
            "WHERE v.VetID IN (SELECT DISTINCT Veterinarian_VetID FROM Veterinarian WHERE Veterinarian_VetID IS NOT NULL) " +
            "ORDER BY v.lastName, v.firstName";
        List<Veterinarian> supervisors = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Veterinarian veterinarian = new Veterinarian(
                    resultSet.getInt("VetID"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getString("phoneNumber"),
                    (Integer) resultSet.getObject("Veterinarian_VetID")
                );
                supervisors.add(veterinarian);
            }
        }
        
        return supervisors;
    }

    @Override
    public List<Veterinarian> findTopLevel() throws SQLException {
        String query = "SELECT VetID, firstName, lastName, phoneNumber, Veterinarian_VetID " +
            "FROM Veterinarian " + 
            "WHERE Veterinarian_VetID IS NULL " + 
            "ORDER BY lastName, firstName ";
        List<Veterinarian> topLevel = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Veterinarian veterinarian = new Veterinarian(
                    resultSet.getInt("VetID"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getString("phoneNumber"),
                    (Integer) resultSet.getObject("Veterinarian_VetID")
                );
                topLevel.add(veterinarian);
            }
        }
        
        return topLevel;
    }

    @Override
    public List<VeterinarianHierarchyDTO> getVeterinarianHierarchy() throws SQLException {
        String query = "SELECT v.VetID, v.firstName, v.lastName, v.phoneNumber, v.Veterinarian_VetID, " +
                   "s.firstName || ' ' || s.lastName AS supervisorName, " +
                   "COUNT(sub.VetID) AS subordinateCount " +
            "FROM Veterinarian v " +
            "LEFT JOIN Veterinarian s ON v.Veterinarian_VetID = s.VetID " +
            "LEFT JOIN Veterinarian sub ON v.VetID = sub.Veterinarian_VetID " +
            "GROUP BY v.VetID, v.firstName, v.lastName, v.phoneNumber, v.Veterinarian_VetID, s.firstName, s.lastName " +
            "ORDER BY v.Veterinarian_VetID NULLS FIRST, v.lastName, v.firstName";
        
        List<VeterinarianHierarchyDTO> hierarchy = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Veterinarian veterinarian = new Veterinarian(
                    resultSet.getInt("VetID"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getString("phoneNumber"),
                    (Integer) resultSet.getObject("Veterinarian_VetID")
                );
                
                VeterinarianHierarchyDTO dto = new VeterinarianHierarchyDTO(
                    veterinarian,
                    resultSet.getString("supervisorName"),
                    resultSet.getInt("subordinateCount")
                );
                
                hierarchy.add(dto);
            }
        }
        
        return hierarchy;
    }
}