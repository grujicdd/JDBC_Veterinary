package rs.ac.uns.ftn.db.jdbc.veterinary.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.db.jdbc.veterinary.connection.ConnectionUtil_HikariCP;
import rs.ac.uns.ftn.db.jdbc.veterinary.dao.PetDAO;
import rs.ac.uns.ftn.db.jdbc.veterinary.model.Pet;

public class PetDAOImpl implements PetDAO {

    @Override
    public int count() throws SQLException {
        String query = "SELECT COUNT(*) FROM Pet";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }else {
            	return -1;
            }
        }
    }

    @Override
    public List<Pet> findByBreedID(int breedID) throws SQLException {
        String query = "SELECT petID, name, birthYear, species, Owner_ownerID, Breed_breedID FROM Pet WHERE Breed_breedID = ? ORDER BY name";
        List<Pet> pets = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, breedID);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Pet pet = new Pet(
                        resultSet.getInt("petID"),
                        resultSet.getString("name"),
                        resultSet.getInt("birthYear"),
                        resultSet.getString("species"),
                        (Integer) resultSet.getObject("Owner_ownerID"),
                        resultSet.getInt("Breed_breedID")
                    );
                    pets.add(pet);
                }
            }
        }
        
        return pets;
    }

    @Override
    public List<Pet> findPetsWithoutAppointments() throws SQLException {
        String query = "SELECT p.petID, p.name, p.birthYear, p.species, p.Owner_ownerID, p.Breed_breedID " +
                      "FROM Pet p " +
                      "WHERE p.petID NOT IN (SELECT DISTINCT Pet_petID FROM Appointment) " +
                      "ORDER BY p.name";
        List<Pet> pets = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Pet pet = new Pet(
                    resultSet.getInt("petID"),
                    resultSet.getString("name"),
                    resultSet.getInt("birthYear"),
                    resultSet.getString("species"),
                    (Integer) resultSet.getObject("Owner_ownerID"),
                    resultSet.getInt("Breed_breedID")
                );
                pets.add(pet);
            }
        }
        
        return pets;
    }

    @Override
    public List<Pet> findPetsWithGeneticRisks() throws SQLException {
        String query = "SELECT DISTINCT p.petID, p.name, p.birthYear, p.species, p.Owner_ownerID, p.Breed_breedID " +
                      "FROM Pet p " +
                      "JOIN BreedGen bg ON p.Breed_breedID = bg.Breed_breedID " +
                      "WHERE bg.riskLevel IN ('High', 'Medium') " +
                      "ORDER BY p.name";
        List<Pet> pets = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Pet pet = new Pet(
                    resultSet.getInt("petID"),
                    resultSet.getString("name"),
                    resultSet.getInt("birthYear"),
                    resultSet.getString("species"),
                    (Integer) resultSet.getObject("Owner_ownerID"),
                    resultSet.getInt("Breed_breedID")
                );
                pets.add(pet);
            }
        }
        
        return pets;
    
    }

    @Override
    public boolean delete(Pet entity) throws SQLException {
        return deleteById(entity.getPetID());
    }

    @Override
    public int deleteAll() throws SQLException {
        String query = "DELETE FROM Pet";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            return preparedStatement.executeUpdate();
        }
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String query = "DELETE FROM Pet WHERE petID = ?";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }

    @Override
    public boolean existsById(Integer id) throws SQLException {
        String query = "SELECT 1 FROM Pet WHERE petID = ?";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public Iterable<Pet> findAll() throws SQLException {
        String query = "SELECT petID, name, birthYear, species, Owner_ownerID, Breed_breedID FROM Pet ORDER BY petID";
        List<Pet> petList = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Pet pet = new Pet(
                    resultSet.getInt("petID"),
                    resultSet.getString("name"),
                    resultSet.getInt("birthYear"),
                    resultSet.getString("species"),
                    (Integer) resultSet.getObject("Owner_ownerID"),
                    resultSet.getInt("Breed_breedID")
                );
                petList.add(pet);
            }
        }
        
        return petList;
    }

    @Override
    public Iterable<Pet> findAllById(Iterable<Integer> ids) throws SQLException {
        List<Pet> petList = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT petID, name, birthYear, species, Owner_ownerID, Breed_breedID FROM Pet WHERE petID IN (");
        
        for (Integer id : ids) {
            queryBuilder.append("?,");
        }
        
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        queryBuilder.append(")");
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString())) {
            
            int paramIndex = 1;
            for (Integer id : ids) {
                preparedStatement.setInt(paramIndex++, id);
            }
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Pet pet = new Pet(
                        resultSet.getInt("petID"),
                        resultSet.getString("name"),
                        resultSet.getInt("birthYear"),
                        resultSet.getString("species"),
                        (Integer) resultSet.getObject("Owner_ownerID"),
                        resultSet.getInt("Breed_breedID")
                    );
                    petList.add(pet);
                }
            }
        }
        
        return petList;
    }

    @Override
    public Pet findById(Integer id) throws SQLException {
        String query = "SELECT petID, name, birthYear, species, Owner_ownerID, Breed_breedID FROM Pet WHERE petID = ?";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, id);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Pet(
                        resultSet.getInt("petID"),
                        resultSet.getString("name"),
                        resultSet.getInt("birthYear"),
                        resultSet.getString("species"),
                        (Integer) resultSet.getObject("Owner_ownerID"),
                        resultSet.getInt("Breed_breedID")
                    );
                }
            }
        }
        
        return null;
    }

    @Override
    public boolean save(Pet entity) throws SQLException {
        if (existsById(entity.getPetID())) {
            return update(entity);
        } else {
            return insert(entity);
        }
    }

    private boolean insert(Pet pet) throws SQLException {
        String query = "INSERT INTO Pet (petID, name, birthYear, species, Owner_ownerID, Breed_breedID) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, pet.getPetID());
            preparedStatement.setString(2, pet.getName());
            preparedStatement.setInt(3, pet.getBirthYear());
            preparedStatement.setString(4, pet.getSpecies());
            
            if (pet.getOwnerID() != null) {
                preparedStatement.setInt(5, pet.getOwnerID());
            } else {
                preparedStatement.setNull(5, java.sql.Types.INTEGER);
            }
            
            preparedStatement.setInt(6, pet.getBreedID());
            
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }

    private boolean update(Pet pet) throws SQLException {
        String query = "UPDATE Pet SET name = ?, birthYear = ?, species = ?, Owner_ownerID = ?, Breed_breedID = ? WHERE petID = ?";
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, pet.getName());
            preparedStatement.setInt(2, pet.getBirthYear());
            preparedStatement.setString(3, pet.getSpecies());
            
            if (pet.getOwnerID() != null) {
                preparedStatement.setInt(4, pet.getOwnerID());
            } else {
                preparedStatement.setNull(4, java.sql.Types.INTEGER);
            }
            
            preparedStatement.setInt(5, pet.getBreedID());
            preparedStatement.setInt(6, pet.getPetID());
            
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;
        }
    }

    @Override
    public int saveAll(Iterable<Pet> entities) throws SQLException {
        int saved = 0;
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection()) {
            connection.setAutoCommit(false);
            
            for (Pet pet : entities) {
                if (save(pet)) {
                    saved++;
                }
            }
            
            connection.commit();
        }
        
        return saved;
    }

    @Override
    public List<Pet> findByOwnerID(Integer ownerID) throws SQLException {
        String query = "SELECT petID, name, birthYear, species, Owner_ownerID, Breed_breedID FROM Pet WHERE Owner_ownerID = ? ORDER BY name";
        List<Pet> pets = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, ownerID);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Pet pet = new Pet(
                        resultSet.getInt("petID"),
                        resultSet.getString("name"),
                        resultSet.getInt("birthYear"),
                        resultSet.getString("species"),
                        (Integer) resultSet.getObject("Owner_ownerID"),
                        resultSet.getInt("Breed_breedID")
                    );
                    pets.add(pet);
                }
            }
        }
        
        return pets;
    }

    @Override
    public List<Pet> findBySpecies(String species) throws SQLException {
        String query = "SELECT petID, name, birthYear, species, Owner_ownerID, Breed_breedID FROM Pet WHERE UPPER(species) = ? ORDER BY name";
        List<Pet> pets = new ArrayList<>();
        
        try (Connection connection = ConnectionUtil_HikariCP.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, species.toUpperCase());
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Pet pet = new Pet(
                        resultSet.getInt("petID"),
                        resultSet.getString("name"),
                        resultSet.getInt("birthYear"),
                        resultSet.getString("species"),
                        (Integer) resultSet.getObject("Owner_ownerID"),
                        resultSet.getInt("Breed_breedID")
                    );
                    pets.add(pet);
                }
            }
        }
        
        return pets;
    }
}