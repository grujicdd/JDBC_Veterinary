package rs.ac.uns.ftn.db.jdbc.veterinary.ui_handler;

import java.sql.SQLException;
import java.util.List;

import rs.ac.uns.ftn.db.jdbc.veterinary.model.Pet;
import rs.ac.uns.ftn.db.jdbc.veterinary.service.PetService;

public class PetUIHandler {

    private static final PetService petService = new PetService();

    public void handlePetMenu() {
        String answer;
        do {
            System.out.println("\n=== PET MANAGEMENT ===");
            System.out.println("Select an option:");
            System.out.println("1 - Show all pets");
            System.out.println("2 - Show pet by ID");
            System.out.println("3 - Add new pet");
            System.out.println("4 - Update pet");
            System.out.println("5 - Delete pet by ID");
            System.out.println("6 - Find pets by owner ID");
            System.out.println("7 - Find pets by species");
            System.out.println("8 - Find pets by breed ID");
            System.out.println("9 - Find pets without appointments");
            System.out.println("10 - Find pets with genetic risks");
            System.out.println("11 - Show pet count");
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
                addPet();
                break;
            case "4":
                updatePet();
                break;
            case "5":
                deletePet();
                break;
            case "6":
                findByOwnerID();
                break;
            case "7":
                findBySpecies();
                break;
            case "8":
                findByBreedID();
                break;
            case "9":
                findPetsWithoutAppointments();
                break;
            case "10":
                findPetsWithGeneticRisks();
                break;
            case "11":
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
        System.out.println("\n=== ALL PETS ===");
        System.out.println(Pet.getFormattedHeader());
        System.out.println("---------------------------------------------------------------------");

        try {
            for (Pet pet : petService.getAll()) {
                System.out.println(pet);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving pets: " + e.getMessage());
        }
    }

    private void showById() {
        System.out.print("Enter Pet ID: ");
        try {
            int id = Integer.parseInt(MainUIHandler.sc.nextLine());
            Pet pet = petService.getById(id);
            
            if (pet != null) {
                System.out.println("\n" + Pet.getFormattedHeader());
                System.out.println("---------------------------------------------------------------------");
                System.out.println(pet);
            } else {
                System.out.println("Pet with ID " + id + " not found.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a number.");
        } catch (SQLException e) {
            System.err.println("Error retrieving pet: " + e.getMessage());
        }
    }

    private void addPet() {
        System.out.println("\n=== ADD NEW PET ===");
        
        try {
            System.out.print("Enter Pet ID: ");
            int id = Integer.parseInt(MainUIHandler.sc.nextLine());
            
            if (petService.existsById(id)) {
                System.out.println("Pet with ID " + id + " already exists.");
                return;
            }
            
            System.out.print("Enter Pet Name: ");
            String name = MainUIHandler.sc.nextLine();
            
            System.out.print("Enter Birth Year: ");
            int birthYear = Integer.parseInt(MainUIHandler.sc.nextLine());
            
            System.out.print("Enter Species (Cat/Dog): ");
            String species = MainUIHandler.sc.nextLine();
            
            System.out.print("Enter Owner ID (or press Enter for no owner): ");
            String ownerIdStr = MainUIHandler.sc.nextLine();
            Integer ownerID = null;
            if (!ownerIdStr.trim().isEmpty()) {
                ownerID = Integer.parseInt(ownerIdStr);
            }
            
            System.out.print("Enter Breed ID: ");
            int breedID = Integer.parseInt(MainUIHandler.sc.nextLine());
            
            Pet pet = new Pet(id, name, birthYear, species, ownerID, breedID);
            
            if (petService.save(pet)) {
                System.out.println("Pet added successfully!");
            } else {
                System.out.println("Failed to add pet.");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format. Please enter valid numbers.");
        } catch (SQLException e) {
            System.err.println("Error adding pet: " + e.getMessage());
        }
    }

    private void updatePet() {
        System.out.println("\n=== UPDATE PET ===");
        
        try {
            System.out.print("Enter Pet ID to update: ");
            int id = Integer.parseInt(MainUIHandler.sc.nextLine());
            
            Pet existingPet = petService.getById(id);
            if (existingPet == null) {
                System.out.println("Pet with ID " + id + " not found.");
                return;
            }
            
            System.out.println("Current pet details:");
            System.out.println(Pet.getFormattedHeader());
            System.out.println(existingPet);
            
            System.out.print("Enter new Name (current: " + existingPet.getName() + "): ");
            String name = MainUIHandler.sc.nextLine();
            if (name.trim().isEmpty()) name = existingPet.getName();
            
            System.out.print("Enter new Birth Year (current: " + existingPet.getBirthYear() + "): ");
            String birthYearStr = MainUIHandler.sc.nextLine();
            int birthYear = existingPet.getBirthYear();
            if (!birthYearStr.trim().isEmpty()) {
                birthYear = Integer.parseInt(birthYearStr);
            }
            
            System.out.print("Enter new Species (current: " + existingPet.getSpecies() + "): ");
            String species = MainUIHandler.sc.nextLine();
            if (species.trim().isEmpty()) species = existingPet.getSpecies();
            
            System.out.print("Enter new Owner ID (current: " + existingPet.getOwnerID() + ") or press Enter to keep current: ");
            String ownerIdStr = MainUIHandler.sc.nextLine();
            Integer ownerID = existingPet.getOwnerID();
            if (!ownerIdStr.trim().isEmpty()) {
                ownerID = Integer.parseInt(ownerIdStr);
            }
            
            System.out.print("Enter new Breed ID (current: " + existingPet.getBreedID() + "): ");
            String breedIdStr = MainUIHandler.sc.nextLine();
            int breedID = existingPet.getBreedID();
            if (!breedIdStr.trim().isEmpty()) {
                breedID = Integer.parseInt(breedIdStr);
            }
            
            Pet updatedPet = new Pet(id, name, birthYear, species, ownerID, breedID);
            
            if (petService.save(updatedPet)) {
                System.out.println("Pet updated successfully!");
            } else {
                System.out.println("Failed to update pet.");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format. Please enter valid numbers.");
        } catch (SQLException e) {
            System.err.println("Error updating pet: " + e.getMessage());
        }
    }

    private void deletePet() {
        System.out.print("Enter Pet ID to delete: ");
        try {
            int id = Integer.parseInt(MainUIHandler.sc.nextLine());
            
            Pet pet = petService.getById(id);
            if (pet == null) {
                System.out.println("Pet with ID " + id + " not found.");
                return;
            }
            
            System.out.println("Pet to delete:");
            System.out.println(Pet.getFormattedHeader());
            System.out.println(pet);
            
            System.out.print("Are you sure you want to delete this pet? (y/N): ");
            String confirmation = MainUIHandler.sc.nextLine();
            
            if (confirmation.equalsIgnoreCase("y") || confirmation.equalsIgnoreCase("yes")) {
                if (petService.deleteById(id)) {
                    System.out.println("Pet deleted successfully!");
                } else {
                    System.out.println("Failed to delete pet. They may have appointments or other dependencies.");
                }
            } else {
                System.out.println("Deletion cancelled.");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a number.");
        } catch (SQLException e) {
            System.err.println("Error deleting pet: " + e.getMessage());
        }
    }

    private void findByOwnerID() {
        System.out.print("Enter Owner ID: ");
        try {
            int ownerID = Integer.parseInt(MainUIHandler.sc.nextLine());
            List<Pet> pets = petService.findByOwnerID(ownerID);
            
            if (pets.isEmpty()) {
                System.out.println("No pets found for owner ID: " + ownerID);
            } else {
                System.out.println("\n=== PETS FOR OWNER ID " + ownerID + " ===");
                System.out.println(Pet.getFormattedHeader());
                System.out.println("---------------------------------------------------------------------");
                for (Pet pet : pets) {
                    System.out.println(pet);
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a number.");
        } catch (SQLException e) {
            System.err.println("Error searching pets: " + e.getMessage());
        }
    }

    private void findBySpecies() {
        System.out.print("Enter species (Cat/Dog): ");
        String species = MainUIHandler.sc.nextLine();
        
        try {
            List<Pet> pets = petService.findBySpecies(species);
            
            if (pets.isEmpty()) {
                System.out.println("No " + species + "s found.");
            } else {
                System.out.println("\n=== " + species.toUpperCase() + "S ===");
                System.out.println(Pet.getFormattedHeader());
                System.out.println("---------------------------------------------------------------------");
                for (Pet pet : pets) {
                    System.out.println(pet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching pets: " + e.getMessage());
        }
    }

    private void findByBreedID() {
        System.out.print("Enter Breed ID: ");
        try {
            int breedID = Integer.parseInt(MainUIHandler.sc.nextLine());
            List<Pet> pets = petService.findByBreedID(breedID);
            
            if (pets.isEmpty()) {
                System.out.println("No pets found for breed ID: " + breedID);
            } else {
                System.out.println("\n=== PETS OF BREED ID " + breedID + " ===");
                System.out.println(Pet.getFormattedHeader());
                System.out.println("---------------------------------------------------------------------");
                for (Pet pet : pets) {
                    System.out.println(pet);
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a number.");
        } catch (SQLException e) {
            System.err.println("Error searching pets: " + e.getMessage());
        }
    }

    private void findPetsWithoutAppointments() {
        try {
            List<Pet> pets = petService.findPetsWithoutAppointments();
            
            if (pets.isEmpty()) {
                System.out.println("All pets have appointments scheduled.");
            } else {
                System.out.println("\n=== PETS WITHOUT APPOINTMENTS ===");
                System.out.println(Pet.getFormattedHeader());
                System.out.println("---------------------------------------------------------------------");
                for (Pet pet : pets) {
                    System.out.println(pet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching pets: " + e.getMessage());
        }
    }

    private void findPetsWithGeneticRisks() {
        try {
            List<Pet> pets = petService.findPetsWithGeneticRisks();
            
            if (pets.isEmpty()) {
                System.out.println("No pets found with genetic risk factors.");
            } else {
                System.out.println("\n=== PETS WITH GENETIC RISK FACTORS ===");
                System.out.println(Pet.getFormattedHeader());
                System.out.println("---------------------------------------------------------------------");
                for (Pet pet : pets) {
                    System.out.println(pet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching pets: " + e.getMessage());
        }
    }

    private void showCount() {
        try {
            int count = petService.getCount();
            System.out.println("Total number of pets: " + count);
        } catch (SQLException e) {
            System.err.println("Error retrieving pet count: " + e.getMessage());
        }
    }
}