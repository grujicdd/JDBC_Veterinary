package rs.ac.uns.ftn.db.jdbc.veterinary.main;

import rs.ac.uns.ftn.db.jdbc.veterinary.connection.ConnectionUtil_HikariCP;
import rs.ac.uns.ftn.db.jdbc.veterinary.ui_handler.MainUIHandler;

/**
 * Main application class for the Veterinary Clinic Management System
 * 
 * This CLI application provides:
 * - CRUD operations for owners, pets, appointments, etc.
 * - Complex queries and reports
 * - Transaction management
 * - Data aggregation and filtering
 * 
 * Database Requirements:
 * - Oracle Database with the veterinary clinic schema
 * - Update connection parameters in ConnectionParams.java
 * - Run the DDL script (DDLSkriptaBaze2.ddl) to create tables
 * - Run the DML script to populate with sample data
 */
public class VeterinaryApplication {

    public static void main(String[] args) {
        
        // Set application log level to reduce HikariCP noise
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "WARN");

        System.out.println("=== VETERINARY CLINIC MANAGEMENT SYSTEM ===");
        System.out.println("Starting application...");
        
        MainUIHandler mainUIHandler = new MainUIHandler();
        try {
            mainUIHandler.handleMainMenu();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Always close the connection pool
            ConnectionUtil_HikariCP.closeDataSource();
            System.out.println("Connection pool closed. Goodbye!");
        }
    }
}