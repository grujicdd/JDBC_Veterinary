package rs.ac.uns.ftn.db.jdbc.veterinary.ui_handler;

import java.sql.SQLException;
import java.util.List;

import rs.ac.uns.ftn.db.jdbc.veterinary.model.Breed;
import rs.ac.uns.ftn.db.jdbc.veterinary.service.BreedService;
import rs.ac.uns.ftn.db.jdbc.veterinary.dao.BreedDAO.BreedStatsDTO;

public class BreedUIHandler {

    private static final BreedService breedService = new BreedService();

    public void handleBreedMenu() {
        String answer;
        do {
            System.out.println("\n=== BREED MANAGEMENT ===");
            System.out.println("Select an option:");
            System.out.println("1 - Show all breeds");
            System.out.println("2 - Show breed by ID");
            System.out.println("3 - Add new breed");
            System.out.println("4 - Update breed");
            System.out.println("5 - Delete breed by ID");
            System.out.println("6 - Search breeds by name");
            System.out.println("7 - Show breeds with genetic risks");
            System.out.println("8 - Show breeds by risk level");
            System.out.println("9 - Show breed statistics");
            System.out.println("10 - Show breed count");
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
                addBreed();
                break;
            case "4":
                updateBreed();
                break;
            case "5":
                deleteBreed();
                break;
            case "6":
                searchByName();
                break;
            case "7":
                showBreedsWithGeneticRisks();
                break;
            case "8":
                showBreedsByRiskLevel();
                break;
            case "9":
                showBreedStatistics();
                break;
            case "10":
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
        System.out.println("\n=== ALL BREEDS ===");
        System.out.println(Breed.getFormattedHeader());
        System.out.println("─────────────────────────────");

        try {
            for (Breed breed : breedService.getAll()) {
                System.out.println(breed);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving breeds: " + e.getMessage());
        }
    }

    private void showById() {
        System.out.print("Enter Breed ID: ");
        try {
            int id = Integer.parseInt(MainUIHandler.sc.nextLine());
            Breed breed = breedService.getById(id);
            
            if (breed != null) {
                System.out.println("\n" + Breed.getFormattedHeader());
                System.out.println("─────────────────────────────");
                System.out.println(breed);
            } else {
                System.out.println("Breed with ID " + id + " not found.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a number.");
        } catch (SQLException e) {
            System.err.println("Error retrieving breed: " + e.getMessage());
        }
    }

    private void addBreed() {
        System.out.println("\n=== ADD NEW BREED ===");
        
        try {
            System.out.print("Enter Breed ID: ");
            int id = Integer.parseInt(MainUIHandler.sc.nextLine());
            
            if (breedService.existsById(id)) {
                System.out.println("Breed with ID " + id + " already exists.");
                return;
            }
            
            System.out.print("Enter Breed Name: ");
            String name = MainUIHandler.sc.nextLine();
            
            if (name.trim().isEmpty()) {
                System.out.println("Breed name cannot be empty.");
                return;
            }
            
            Breed breed = new Breed(id, name.trim());
            
            if (breedService.save(breed)) {
                System.out.println("Breed added successfully!");
            } else {
                System.out.println("Failed to add breed.");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a number.");
        } catch (SQLException e) {
            System.err.println("Error adding breed: " + e.getMessage());
        }
    }

    private void updateBreed() {
        System.out.println("\n=== UPDATE BREED ===");
        
        try {
            System.out.print("Enter Breed ID to update: ");
            int id = Integer.parseInt(MainUIHandler.sc.nextLine());
            
            Breed existingBreed = breedService.getById(id);
            if (existingBreed == null) {
                System.out.println("Breed with ID " + id + " not found.");
                return;
            }
            
            System.out.println("Current breed details:");
            System.out.println(Breed.getFormattedHeader());
            System.out.println(existingBreed);
            
            System.out.print("Enter new Breed Name (current: " + existingBreed.getBreedName() + "): ");
            String name = MainUIHandler.sc.nextLine();
            if (name.trim().isEmpty()) {
                name = existingBreed.getBreedName();
            }
            
            Breed updatedBreed = new Breed(id, name.trim());
            
            if (breedService.save(updatedBreed)) {
                System.out.println("Breed updated successfully!");
            } else {
                System.out.println("Failed to update breed.");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a number.");
        } catch (SQLException e) {
            System.err.println("Error updating breed: " + e.getMessage());
        }
    }

    private void deleteBreed() {
        System.out.print("Enter Breed ID to delete: ");
        try {
            int id = Integer.parseInt(MainUIHandler.sc.nextLine());
            
            Breed breed = breedService.getById(id);
            if (breed == null) {
                System.out.println("Breed with ID " + id + " not found.");
                return;
            }
            
            System.out.println("Breed to delete:");
            System.out.println(Breed.getFormattedHeader());
            System.out.println(breed);
            
            System.out.print("Are you sure you want to delete this breed? (y/N): ");
            String confirmation = MainUIHandler.sc.nextLine();
            
            if (confirmation.equalsIgnoreCase("y") || confirmation.equalsIgnoreCase("yes")) {
                if (breedService.deleteById(id)) {
                    System.out.println("Breed deleted successfully!");
                } else {
                    System.out.println("Failed to delete breed. There may be pets of this breed.");
                }
            } else {
                System.out.println("Deletion cancelled.");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a number.");
        } catch (SQLException e) {
            System.err.println("Error deleting breed: " + e.getMessage());
        }
    }

    private void searchByName() {
        System.out.print("Enter breed name to search for: ");
        String name = MainUIHandler.sc.nextLine();
        
        try {
            List<Breed> breeds = breedService.findByName(name);
            
            if (breeds.isEmpty()) {
                System.out.println("No breeds found with name containing: " + name);
            } else {
                System.out.println("\n=== SEARCH RESULTS ===");
                System.out.println(Breed.getFormattedHeader());
                System.out.println("─────────────────────────────");
                for (Breed breed : breeds) {
                    System.out.println(breed);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching breeds: " + e.getMessage());
        }
    }

    private void showBreedsWithGeneticRisks() {
        try {
            List<Breed> breeds = breedService.findBreedsWithGeneticRisks();
            
            if (breeds.isEmpty()) {
                System.out.println("No breeds found with genetic risk factors.");
            } else {
                System.out.println("\n=== BREEDS WITH GENETIC RISK FACTORS ===");
                System.out.println(Breed.getFormattedHeader());
                System.out.println("─────────────────────────────");
                for (Breed breed : breeds) {
                    System.out.println(breed);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching breeds: " + e.getMessage());
        }
    }

    private void showBreedsByRiskLevel() {
        System.out.println("Available risk levels: Low, Medium, High");
        System.out.print("Enter risk level: ");
        String riskLevel = MainUIHandler.sc.nextLine();
        
        try {
            List<Breed> breeds = breedService.findBreedsByRiskLevel(riskLevel);
            
            if (breeds.isEmpty()) {
                System.out.println("No breeds found with risk level: " + riskLevel);
            } else {
                System.out.println("\n=== BREEDS WITH " + riskLevel.toUpperCase() + " RISK LEVEL ===");
                System.out.println(Breed.getFormattedHeader());
                System.out.println("─────────────────────────────");
                for (Breed breed : breeds) {
                    System.out.println(breed);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching breeds: " + e.getMessage());
        }
    }

    private void showBreedStatistics() {
        try {
            List<BreedStatsDTO> stats = breedService.getBreedStatistics();
            
            if (stats.isEmpty()) {
                System.out.println("No breed statistics available.");
            } else {
                System.out.println("\n=== BREED STATISTICS ===");
                System.out.println(BreedStatsDTO.getFormattedHeader());
                System.out.println("─────────────────────────────────────────────────────────────");
                for (BreedStatsDTO stat : stats) {
                    System.out.println(stat);
                }
                
                // Summary statistics
                int totalBreeds = stats.size();
                int totalPets = stats.stream().mapToInt(BreedStatsDTO::getPetCount).sum();
                int totalDogs = stats.stream().mapToInt(BreedStatsDTO::getDogCount).sum();
                int totalCats = stats.stream().mapToInt(BreedStatsDTO::getCatCount).sum();
                
                System.out.println("─────────────────────────────────────────────────────────────");
                System.out.printf("SUMMARY: %-6d breeds | %-8d total pets | %-8d dogs | %-8d cats%n",
                    totalBreeds, totalPets, totalDogs, totalCats);
                
                // Show top 3 most popular breeds
                System.out.println("\n--- TOP 3 MOST POPULAR BREEDS ---");
                int count = 0;
                for (BreedStatsDTO stat : stats) {
                    if (stat.getPetCount() > 0 && count < 3) {
                        System.out.printf("%d. %s (%d pets)%n", 
                            count + 1, stat.getBreedName(), stat.getPetCount());
                        count++;
                    }
                }
                
                // Show breeds with no pets
                System.out.println("\n--- BREEDS WITH NO REGISTERED PETS ---");
                boolean foundEmpty = false;
                for (BreedStatsDTO stat : stats) {
                    if (stat.getPetCount() == 0) {
                        System.out.println("• " + stat.getBreedName());
                        foundEmpty = true;
                    }
                }
                if (!foundEmpty) {
                    System.out.println("All breeds have registered pets.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving breed statistics: " + e.getMessage());
        }
    }

    private void showCount() {
        try {
            int count = breedService.getCount();
            System.out.println("Total number of breeds: " + count);
        } catch (SQLException e) {
            System.err.println("Error retrieving breed count: " + e.getMessage());
        }
    }
}