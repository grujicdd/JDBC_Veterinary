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
            System.out.println("1  - Show all owners and their pets. If owner has no pets, display: NO PETS");
            System.out.println();
            System.out.println("2  - Show appointment information. Display basic appointment info plus all diagnoses");
            System.out.println("     for each appointment. For each diagnosis show total medications prescribed and average cost.");
            System.out.println();
            System.out.println("3  - Show pets and vaccination history for pets whose breed has genetic predispositions");
            System.out.println("     with risk level +/- 20% of 'High' risk. For all vaccines given to these pets,");
            System.out.println("     calculate total side effects. Show only pets with more than 2 side effects total.");
            System.out.println("     For breeds with no vaccines, display a message.");
            System.out.println();
            System.out.println("4  - Show pet IDs, names and average appointment count for pets with the highest");
            System.out.println("     average appointment frequency. For these pets, show list of diagnoses.");
            System.out.println("     Additionally show how many male vs female pets there are.");
            System.out.println();
            System.out.println("5  - Show upcoming appointments where the number of diagnoses exceeds the");
            System.out.println("     appointment capacity. Delete those appointments and redistribute diagnoses");
            System.out.println("     to create new appointments with proper diagnosis distribution.");
            System.out.println();
            System.out.println("6  - Create CRUD operations for a new Assignment table and add several records.");
            System.out.println("     Create a report showing for each veterinarian: name, specialization, and");
            System.out.println("     highest paid assignment. For vets with no assignments show 'NONE' and 0 payment.");
            System.out.println();
            System.out.println("7  - Show all unassigned medical procedures. Then show list of veterinarians working");
            System.out.println("     in clinics where these procedures are scheduled but who have no current assignments.");
            System.out.println();
            System.out.println("8  - For each veterinarian assigned to a procedure, show vet_id, first_name, last_name,");
            System.out.println("     procedure_name, specialty and list of other vets assigned to same procedure.");
            System.out.println("     Show total count of other vets for same procedure.");
            System.out.println();
            System.out.println("9  - For each vet assigned to procedures, show vet_id, first_name, last_name, procedure_name,");
            System.out.println("     specialty and list of vets for assigned procedure. Show this vet's share of total");
            System.out.println("     payment for the procedure as percentage rounded to 2 decimals.");
            System.out.println();
            System.out.println("10 - Show vet ID, first name, last name, salary and list of other vets and their");
            System.out.println("     payments for procedures. Show only vets whose procedure payment is above");
            System.out.println("     average payment for that procedure type.");
            System.out.println();
            System.out.println("11 - For each veterinarian, show total payment from all their procedures. Consider");
            System.out.println("     only vets who work procedures outside their primary clinic specialty.");
            System.out.println();
            System.out.println("12 - Rebalance workload for veterinarians over 60 years old so they can work");
            System.out.println("     maximum 2 procedures per week. Reassign freed procedures to other qualified vets.");
            System.out.println("     If no qualified vets exist, show unassigned procedures. Handle scheduling conflicts.");
            System.out.println();
            System.out.println("13 - Interactive entry of new veterinary clinic with all basic data, equipment and");
            System.out.println("     clinic location. If location doesn't exist, add it to database. Create equipment records.");
            System.out.println();
            System.out.println("X - Return to main menu");

            answer = MainUIHandler.sc.nextLine();

            switch (answer) {
            case "1":
                showOwnersAndPets();
                break;
            case "2":
                showAppointmentReport();
                break;
            case "3":
                showPetsWithGeneticRisks();
                break;
            case "4":
                showMostFrequentlyVisitingPets();
                break;
            case "5":
                handleAppointmentRebalancing();
                break;
            case "6":
                // TODO: Implement Assignment CRUD and reporting
                System.out.println("TODO: Implement Assignment CRUD and reporting");
                break;
            case "7":
                // TODO: Implement unassigned procedures query
                System.out.println("TODO: Implement unassigned procedures query");
                break;
            case "8":
                // TODO: Implement veterinarian procedure assignments
                System.out.println("TODO: Implement veterinarian procedure assignments");
                break;
            case "9":
                // TODO: Implement payment share calculation
                System.out.println("TODO: Implement payment share calculation");
                break;
            case "10":
                // TODO: Implement above-average payment analysis
                System.out.println("TODO: Implement above-average payment analysis");
                break;
            case "11":
                // TODO: Implement cross-specialty procedure analysis
                System.out.println("TODO: Implement cross-specialty procedure analysis");
                break;
            case "12":
                // TODO: Implement workload rebalancing
                System.out.println("TODO: Implement workload rebalancing");
                break;
            case "13":
                // TODO: Implement interactive clinic entry
                System.out.println("TODO: Implement interactive clinic entry");
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
            System.out.println("TODO: Implement complex query 1 - Owners and their pets");
            // Implementation would call complexQueryService methods
        } catch (Exception e) {
            System.err.println("Error executing query: " + e.getMessage());
        }
    }

    public void showAppointmentReport() {
        try {
            System.out.println("TODO: Implement complex query 2 - Appointment report with diagnoses");
        } catch (Exception e) {
            System.err.println("Error executing query: " + e.getMessage());
        }
    }

    public void showPetsWithGeneticRisks() {
        try {
            System.out.println("TODO: Implement complex query 3 - Pets with genetic risks and vaccination history");
        } catch (Exception e) {
            System.err.println("Error executing query: " + e.getMessage());
        }
    }

    public void showMostFrequentlyVisitingPets() {
        try {
            System.out.println("TODO: Implement complex query 4 - Most frequently visiting pets");
        } catch (Exception e) {
            System.err.println("Error executing query: " + e.getMessage());
        }
    }

    public void handleAppointmentRebalancing() {
        try {
            System.out.println("TODO: Implement complex query 5 - Appointment rebalancing");
        } catch (Exception e) {
            System.err.println("Error executing query: " + e.getMessage());
        }
    }
}
