package rs.ac.uns.ftn.db.jdbc.veterinary.connection;

public class ConnectionParams {
    public static final String DRIVER = "org.postgresql.Driver";
    
    public static final String LOCAL_CONNECTION_STRING = "jdbc:postgresql://localhost:5432/veterinary_clinic";
    public static final String DOCKER_CONNECTION_STRING = "jdbc:postgresql://localhost:5432/veterinary_clinic";
    
    public static final String USERNAME = "postgres"; 
    public static final String PASSWORD = "super";
}
