package rs.ac.uns.ftn.db.jdbc.veterinary.ui_handler;

import java.sql.SQLException;

import rs.ac.uns.ftn.db.jdbc.veterinary.service.ComplexQueryService;

public class ComplexQueryUIHandler {

    private static final ComplexQueryService complexQueryService = new ComplexQueryService();

    public void handleComplexQueryMenu() {
        String answer;
        do {
            System.out.println("\n=== COMPLEX QUERIES & REPORTS ===");
            System.out.println("Select a functionality:");
            System.out.println();
            System.out.println("1 - Show all owners and their pets");
            System.out.println("2 - Show active veterinarians workload analysis");
            System.out.println("3 - Show veterinarian workload and hierarchy report");
            System.out.println("4 - Transfer pet ownership (Complex Transaction)");
            System.out.println();
            System.out.println("X - Return to main menu");
            System.out.print("Your choice: ");

            answer = MainUIHandler.sc.nextLine();

            switch (answer) {
            case "1":
                showOwnersAndPets();
                break;
            case "2":
                showActiveVeterinariansWorkloadAnalysis();
                break;
            case "3":
                showVeterinarianWorkloadReport();
                break;
            case "4":
                transferPetOwnership();
                break;
            case "x":
            case "X":
                break;
            default:
                System.out.println("Invalid option. Please try again.");
            }

        } while (!answer.equalsIgnoreCase("X"));
    }

    public void showOwnersAndPets() {
        try {
            complexQueryService.getOwnersWithPets();
        } catch (SQLException e) {
            System.err.println("Error executing owners and pets query: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void showActiveVeterinariansWorkloadAnalysis() {
        try {
            complexQueryService.getActiveVeterinariansWorkloadAnalysis();
        } catch (SQLException e) {
            System.err.println("Error executing active veterinarians workload analysis: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void showVeterinarianWorkloadReport() {
        try {
            complexQueryService.getVeterinarianWorkloadReport();
        } catch (SQLException e) {
            System.err.println("Error executing veterinarian workload report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void transferPetOwnership() {
        try {
            complexQueryService.transferPetOwnership();
        } catch (SQLException e) {
            System.err.println("Error executing pet ownership transfer: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Invalid input format. Please enter valid numbers for Pet ID and Owner ID.");
        } catch (Exception e) {
            System.err.println("Unexpected error during pet ownership transfer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}