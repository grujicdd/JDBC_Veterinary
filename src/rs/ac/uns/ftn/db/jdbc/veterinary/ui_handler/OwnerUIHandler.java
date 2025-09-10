package rs.ac.uns.ftn.db.jdbc.veterinary.ui_handler;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rs.ac.uns.ftn.db.jdbc.veterinary.model.Owner;
import rs.ac.uns.ftn.db.jdbc.veterinary.service.OwnerService;

public class OwnerUIHandler {

    private static final OwnerService ownerService = new OwnerService();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public void handleOwnerMenu() {
        String answer;
        do {
            System.out.println("\n=== OWNER MANAGEMENT ===");
            System.out.println("Select an option:");
            System.out.println("1 - Show all owners");
            System.out.println("2 - Show owner by ID");
            System.out.println("3 - Add new owner");
            System.out.println("4 - Update owner");
            System.out.println("5 - Delete owner by ID");
            System.out.println("6 - Search owners by name");
            System.out.println("7 - Show owners with recent appointments");
            System.out.println("8 - Show owner count");
            System.out.println("X - Return to main menu");
            System.out.print("Your choice: ");

            answer = MainUIHandler.sc.nextLine();

            switch (answer) {
            case "1":
                showAll();
                break;
            case "2":
                showById();
                break;
            case "3":
                addOwner();
                break;
            case "4":
                updateOwner();
                break;
            case "5":
                deleteOwner();
                break;
            case "6":
                searchByName();
                break;
            case "7":
                showOwnersWithRecentAppointments();
                break;
            case "8":
                showCount();
                break;
            case "x":
            case "X":
                break;
            default:
                System.out.println("Invalid option. Please try again.");
            }

        } while (!answer.equalsIgnoreCase("X"));
    }

    private void showAll() {
        System.out.println("\n=== ALL OWNERS ===");
        System.out.println(Owner.getFormattedHeader());
        System.out.println("---------------------------------------------------------------------");

        try {
            for (Owner owner : ownerService.getAll()) {
                System.out.println(owner);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving owners: " + e.getMessage());
        }
    }

    private void showById() {
        System.out.print("Enter Owner ID: ");
        try {
            int id = Integer.parseInt(MainUIHandler.sc.nextLine());
            Owner owner = ownerService.getById(id);
            
            if (owner != null) {
                System.out.println("\n" + Owner.getFormattedHeader());
                System.out.println("---------------------------------------------------------------------");
                System.out.println(owner);
            } else {
                System.out.println("Owner with ID " + id + " not found.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a number.");
        } catch (SQLException e) {
            System.err.println("Error retrieving owner: " + e.getMessage());
        }
    }

    private void addOwner() {
        System.out.println("\n=== ADD NEW OWNER ===");
        
        try {
            System.out.print("Enter Owner ID: ");
            int id = Integer.parseInt(MainUIHandler.sc.nextLine());
            
            if (ownerService.existsById(id)) {
                System.out.println("Owner with ID " + id + " already exists.");
                return;
            }
            
            System.out.print("Enter First Name: ");
            String firstName = MainUIHandler.sc.nextLine();
            
            System.out.print("Enter Last Name: ");
            String lastName = MainUIHandler.sc.nextLine();
            
            System.out.print("Enter Phone Number: ");
            String phoneNumber = MainUIHandler.sc.nextLine();
            
            System.out.print("Enter Birth Date (dd.MM.yyyy) or press Enter to skip: ");
            String birthDateStr = MainUIHandler.sc.nextLine();
            Date birthDate = null;
            
            if (!birthDateStr.trim().isEmpty()) {
                try {
                    birthDate = dateFormat.parse(birthDateStr);
                } catch (ParseException e) {
                    System.err.println("Invalid date format. Birth date will be set to null.");
                }
            }
            
            Owner owner = new Owner(id, firstName, lastName, phoneNumber, birthDate);
            
            if (ownerService.save(owner)) {
                System.out.println("Owner added successfully!");
            } else {
                System.out.println("Failed to add owner.");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a number.");
        } catch (SQLException e) {
            System.err.println("Error adding owner: " + e.getMessage());
        }
    }

    private void updateOwner() {
        System.out.println("\n=== UPDATE OWNER ===");
        
        try {
            System.out.print("Enter Owner ID to update: ");
            int id = Integer.parseInt(MainUIHandler.sc.nextLine());
            
            Owner existingOwner = ownerService.getById(id);
            if (existingOwner == null) {
                System.out.println("Owner with ID " + id + " not found.");
                return;
            }
            
            System.out.println("Current owner details:");
            System.out.println(Owner.getFormattedHeader());
            System.out.println(existingOwner);
            
            System.out.print("Enter new First Name (current: " + existingOwner.getFirstName() + "): ");
            String firstName = MainUIHandler.sc.nextLine();
            if (firstName.trim().isEmpty()) firstName = existingOwner.getFirstName();
            
            System.out.print("Enter new Last Name (current: " + existingOwner.getLastName() + "): ");
            String lastName = MainUIHandler.sc.nextLine();
            if (lastName.trim().isEmpty()) lastName = existingOwner.getLastName();
            
            System.out.print("Enter new Phone Number (current: " + existingOwner.getPhoneNumber() + "): ");
            String phoneNumber = MainUIHandler.sc.nextLine();
            if (phoneNumber.trim().isEmpty()) phoneNumber = existingOwner.getPhoneNumber();
            
            Date birthDate = existingOwner.getBirthDate();
            System.out.print("Enter new Birth Date (dd.MM.yyyy) (current: " + 
                           (birthDate != null ? dateFormat.format(birthDate) : "N/A") + ") or press Enter to keep current: ");
            String birthDateStr = MainUIHandler.sc.nextLine();
            
            if (!birthDateStr.trim().isEmpty()) {
                try {
                    birthDate = dateFormat.parse(birthDateStr);
                } catch (ParseException e) {
                    System.err.println("Invalid date format. Keeping current birth date.");
                }
            }
            
            Owner updatedOwner = new Owner(id, firstName, lastName, phoneNumber, birthDate);
            
            if (ownerService.save(updatedOwner)) {
                System.out.println("Owner updated successfully!");
            } else {
                System.out.println("Failed to update owner.");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a number.");
        } catch (SQLException e) {
            System.err.println("Error updating owner: " + e.getMessage());
        }
    }

    private void deleteOwner() {
        System.out.print("Enter Owner ID to delete: ");
        try {
            int id = Integer.parseInt(MainUIHandler.sc.nextLine());
            
            Owner owner = ownerService.getById(id);
            if (owner == null) {
                System.out.println("Owner with ID " + id + " not found.");
                return;
            }
            
            System.out.println("Owner to delete:");
            System.out.println(Owner.getFormattedHeader());
            System.out.println(owner);
            
            System.out.print("Are you sure you want to delete this owner? (y/N): ");
            String confirmation = MainUIHandler.sc.nextLine();
            
            if (confirmation.equalsIgnoreCase("y") || confirmation.equalsIgnoreCase("yes")) {
                if (ownerService.deleteById(id)) {
                    System.out.println("Owner deleted successfully!");
                } else {
                    System.out.println("Failed to delete owner. They may have pets or other dependencies.");
                }
            } else {
                System.out.println("Deletion cancelled.");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a number.");
        } catch (SQLException e) {
            System.err.println("Error deleting owner: " + e.getMessage());
        }
    }

    private void searchByName() {
        System.out.print("Enter name to search for: ");
        String name = MainUIHandler.sc.nextLine();
        
        try {
            List<Owner> owners = ownerService.findByName(name);
            
            if (owners.isEmpty()) {
                System.out.println("No owners found with name containing: " + name);
            } else {
                System.out.println("\n=== SEARCH RESULTS ===");
                System.out.println(Owner.getFormattedHeader());
                System.out.println("---------------------------------------------------------------------");
                for (Owner owner : owners) {
                    System.out.println(owner);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching owners: " + e.getMessage());
        }
    }

    private void showOwnersWithRecentAppointments() {
        System.out.print("Enter number of days to look back (default 30): ");
        String daysStr = MainUIHandler.sc.nextLine();
        int days = 30;
        
        if (!daysStr.trim().isEmpty()) {
            try {
                days = Integer.parseInt(daysStr);
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format. Using default 30 days.");
            }
        }
        
        try {
            List<Owner> owners = ownerService.findOwnersWithRecentAppointments(days);
            
            if (owners.isEmpty()) {
                System.out.println("No owners found with appointments in the last " + days + " days.");
            } else {
                System.out.println("\n=== OWNERS WITH RECENT APPOINTMENTS (Last " + days + " days) ===");
                System.out.println(Owner.getFormattedHeader());
                System.out.println("---------------------------------------------------------------------");
                for (Owner owner : owners) {
                    System.out.println(owner);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving owners with recent appointments: " + e.getMessage());
        }
    }

    private void showCount() {
        try {
            int count = ownerService.getCount();
            System.out.println("Total number of owners: " + count);
        } catch (SQLException e) {
            System.err.println("Error retrieving owner count: " + e.getMessage());
        }
    }
}
