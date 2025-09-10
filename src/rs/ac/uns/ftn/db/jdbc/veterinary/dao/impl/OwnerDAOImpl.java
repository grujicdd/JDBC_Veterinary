package rs.ac.uns.ftn.db.jdbc.veterinary.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.db.jdbc.veterinary.connection.ConnectionUtil_HikariCP;
import rs.ac.uns.ftn.db.jdbc.veterinary.dao.OwnerDAO;
import rs.ac.uns.ftn.db.jdbc.veterinary.model.Owner;

public class OwnerDAOImpl implements OwnerDAO {

    @Override
    public int count() throws SQLException {
        String query = "SELECT COUNT(*) FROM Owner";
        
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
    public boolean delete(Owner entity) throws SQLException {
        return deleteById(entity.getOwnerID());
    }

    @Override
    public int deleteAll() throws SQLException {
        String query = "DELETE FROM Owner";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            return preparedStatement.executeUpdate();
        }
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String query = "DELETE FROM Owner WHERE ownerID = ?";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }

    @Override
    public boolean existsById(Integer id) throws SQLException {
        String query = "SELECT 1 FROM Owner WHERE ownerID = ?";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public Iterable<Owner> findAll() throws SQLException {
        String query = "SELECT ownerID, firstName, lastName, phoneNumber, birthDate FROM Owner ORDER BY ownerID";
        List<Owner> ownerList = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Owner owner = new Owner(
                    resultSet.getInt("ownerID"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getString("phoneNumber"),
                    resultSet.getDate("birthDate")
                );
                ownerList.add(owner);
            }
        }
        
        return ownerList;
    }

    @Override
    public Iterable<Owner> findAllById(Iterable<Integer> ids) throws SQLException {
        List<Owner> ownerList = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT ownerID, firstName, lastName, phoneNumber, birthDate FROM Owner WHERE ownerID IN (");
        
        for (Integer id : ids) {
            queryBuilder.append("?,");
        }
        
        queryBuilder.deleteCharAt(queryBuilder.length() - 1); // Remove last comma
        queryBuilder.append(")");
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString())) {
            
            int paramIndex = 1;
            for (Integer id : ids) {
                preparedStatement.setInt(paramIndex++, id);
            }
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Owner owner = new Owner(
                        resultSet.getInt("ownerID"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getDate("birthDate")
                    );
                    ownerList.add(owner);
                }
            }
        }
        
        return ownerList;
    }

    @Override
    public Owner findById(Integer id) throws SQLException {
        String query = "SELECT ownerID, firstName, lastName, phoneNumber, birthDate FROM Owner WHERE ownerID = ?";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, id);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Owner(
                        resultSet.getInt("ownerID"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getDate("birthDate")
                    );
                }
            }
        }
        
        return null;
    }

    @Override
    public boolean save(Owner entity) throws SQLException {
        if (existsById(entity.getOwnerID())) {
            return update(entity);
        } else {
            return insert(entity);
        }
    }

    private boolean insert(Owner owner) throws SQLException {
        String query = "INSERT INTO Owner (ownerID, firstName, lastName, phoneNumber, birthDate) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, owner.getOwnerID());
            preparedStatement.setString(2, owner.getFirstName());
            preparedStatement.setString(3, owner.getLastName());
            preparedStatement.setString(4, owner.getPhoneNumber());
            preparedStatement.setDate(5, owner.getBirthDate() != null ? new java.sql.Date(owner.getBirthDate().getTime()) : null);
            
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }

    private boolean update(Owner owner) throws SQLException {
        String query = "UPDATE Owner SET firstName = ?, lastName = ?, phoneNumber = ?, birthDate = ? WHERE ownerID = ?";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, owner.getFirstName());
            preparedStatement.setString(2, owner.getLastName());
            preparedStatement.setString(3, owner.getPhoneNumber());
            preparedStatement.setDate(4, owner.getBirthDate() != null ? new java.sql.Date(owner.getBirthDate().getTime()) : null);
            preparedStatement.setInt(5, owner.getOwnerID());
            
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }

    @Override
    public int saveAll(Iterable<Owner> entities) throws SQLException {
        int saved = 0;
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection()) {
            connection.setAutoCommit(false);
            
            for (Owner owner : entities) {
                if (save(owner)) {
                    saved++;
                }
            }
            
            connection.commit();
        }
        
        return saved;
    }

    @Override
    public List<Owner> findByName(String name) throws SQLException {
        String query = "SELECT ownerID, firstName, lastName, phoneNumber, birthDate FROM Owner " +
                      "WHERE UPPER(firstName) LIKE ? OR UPPER(lastName) LIKE ? ORDER BY lastName, firstName";
        List<Owner> owners = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            String searchPattern = "%" + name.toUpperCase() + "%";
            preparedStatement.setString(1, searchPattern);
            preparedStatement.setString(2, searchPattern);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Owner owner = new Owner(
                        resultSet.getInt("ownerID"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getDate("birthDate")
                    );
                    owners.add(owner);
                }
            }
        }
        
        return owners;
    }

    @Override
    public List<Owner> findOwnersWithRecentAppointments(int days) throws SQLException {
        String query = "SELECT DISTINCT o.ownerID, o.firstName, o.lastName, o.phoneNumber, o.birthDate " +
                      "FROM Owner o " +
                      "JOIN Pet p ON o.ownerID = p.Owner_ownerID " +
                      "JOIN Appointment a ON p.petID = a.Pet_petID " +
                      "WHERE a.appDateTime >= SYSDATE - ? " +
                      "ORDER BY o.lastName, o.firstName";
        List<Owner> owners = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, days);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Owner owner = new Owner(
                        resultSet.getInt("ownerID"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getDate("birthDate")
                    );
                    owners.add(owner);
                }
            }
        }
        
        return owners;
    }
}
