package rs.ac.uns.ftn.db.jdbc.veterinary.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.db.jdbc.veterinary.connection.ConnectionUtil_HikariCP;
import rs.ac.uns.ftn.db.jdbc.veterinary.dao.BreedDAO;
import rs.ac.uns.ftn.db.jdbc.veterinary.model.Breed;

public class BreedDAOImpl implements BreedDAO {

    @Override
    public int count() throws SQLException {
        String query = "SELECT COUNT(*) FROM Breed";
        
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
    public boolean delete(Breed entity) throws SQLException {
        return deleteById(entity.getBreedID());
    }

    @Override
    public int deleteAll() throws SQLException {
        String query = "DELETE FROM Breed";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            return preparedStatement.executeUpdate();
        }
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String query = "DELETE FROM Breed WHERE breedID = ?";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }

    @Override
    public boolean existsById(Integer id) throws SQLException {
        String query = "SELECT 1 FROM Breed WHERE breedID = ?";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public Iterable<Breed> findAll() throws SQLException {
        String query = "SELECT breedID, breedName FROM Breed ORDER BY breedName";
        List<Breed> breedList = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Breed breed = new Breed(
                    resultSet.getInt("breedID"),
                    resultSet.getString("breedName")
                );
                breedList.add(breed);
            }
        }
        
        return breedList;
    }

    @Override
    public Iterable<Breed> findAllById(Iterable<Integer> ids) throws SQLException {
        List<Breed> breedList = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT breedID, breedName FROM Breed WHERE breedID IN (");
        
        for (Integer id : ids) {
            queryBuilder.append("?,");
        }
        
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        queryBuilder.append(") ORDER BY breedName");
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString())) {
            
            int paramIndex = 1;
            for (Integer id : ids) {
                preparedStatement.setInt(paramIndex++, id);
            }
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Breed breed = new Breed(
                        resultSet.getInt("breedID"),
                        resultSet.getString("breedName")
                    );
                    breedList.add(breed);
                }
            }
        }
        
        return breedList;
    }

    @Override
    public Breed findById(Integer id) throws SQLException {
        String query = "SELECT breedID, breedName FROM Breed WHERE breedID = ?";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, id);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Breed(
                        resultSet.getInt("breedID"),
                        resultSet.getString("breedName")
                    );
                }
            }
        }
        
        return null;
    }

    @Override
    public boolean save(Breed entity) throws SQLException {
        if (existsById(entity.getBreedID())) {
            return update(entity);
        } else {
            return insert(entity);
        }
    }

    private boolean insert(Breed breed) throws SQLException {
        String query = "INSERT INTO Breed (breedID, breedName) VALUES (?, ?)";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, breed.getBreedID());
            preparedStatement.setString(2, breed.getBreedName());
            
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }

    private boolean update(Breed breed) throws SQLException {
        String query = "UPDATE Breed SET breedName = ? WHERE breedID = ?";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, breed.getBreedName());
            preparedStatement.setInt(2, breed.getBreedID());
            
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }

    @Override
    public int saveAll(Iterable<Breed> entities) throws SQLException {
        int saved = 0;
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection()) {
            connection.setAutoCommit(false);
            
            for (Breed breed : entities) {
                if (save(breed)) {
                    saved++;
                }
            }
            
            connection.commit();
        }
        
        return saved;
    }

    @Override
    public List<Breed> findByName(String name) throws SQLException {
        String query = "SELECT breedID, breedName FROM Breed WHERE UPPER(breedName) LIKE UPPER(?) ORDER BY breedName";
        List<Breed> breeds = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, "%" + name + "%");
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Breed breed = new Breed(
                        resultSet.getInt("breedID"),
                        resultSet.getString("breedName")
                    );
                    breeds.add(breed);
                }
            }
        }
        
        return breeds;
    }

    @Override
    public List<Breed> findBreedsWithGeneticRisks() throws SQLException {
        String query = "SELECT DISTINCT b.breedID, b.breedName " +
            "FROM Breed b " +
            "JOIN BreedGen bg ON b.breedID = bg.Breed_breedID " +
            "ORDER BY b.breedName ";
        
        List<Breed> breeds = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Breed breed = new Breed(
                    resultSet.getInt("breedID"),
                    resultSet.getString("breedName")
                );
                breeds.add(breed);
            }
        }
        
        return breeds;
    }

    @Override
    public List<Breed> findBreedsByRiskLevel(String riskLevel) throws SQLException {
        String query = "SELECT DISTINCT b.breedID, b.breedName " +
            "FROM Breed b " +
            "JOIN BreedGen bg ON b.breedID = bg.Breed_breedID " +
            "WHERE bg.riskLevel = ? " +
            "ORDER BY b.breedName";
        
        List<Breed> breeds = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, riskLevel);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Breed breed = new Breed(
                        resultSet.getInt("breedID"),
                        resultSet.getString("breedName")
                    );
                    breeds.add(breed);
                }
            }
        }
        
        return breeds;
    }

    @Override
    public List<BreedStatsDTO> getBreedStatistics() throws SQLException {
        String query = "SELECT b.breedID, b.breedName, " +
                   "COUNT(p.petID) as totalPets, " +
                   "COUNT(CASE WHEN p.species = 'Dog' THEN 1 END) as dogCount, " +
                   "COUNT(CASE WHEN p.species = 'Cat' THEN 1 END) as catCount " +
            "FROM Breed b " +
            "LEFT JOIN Pet p ON b.breedID = p.Breed_breedID " +
            "GROUP BY b.breedID, b.breedName " +
            "ORDER BY totalPets DESC, b.breedName";
        
        List<BreedStatsDTO> stats = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                BreedStatsDTO stat = new BreedStatsDTO(
                    resultSet.getInt("breedID"),
                    resultSet.getString("breedName"),
                    resultSet.getInt("totalPets"),
                    resultSet.getInt("dogCount"),
                    resultSet.getInt("catCount")
                );
                stats.add(stat);
            }
        }
        
        return stats;
    }
}