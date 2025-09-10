package rs.ac.uns.ftn.db.jdbc.veterinary.ui_handler;

import java.util.Scanner;

public class MainUIHandler {

    public static Scanner sc = new Scanner(System.in);

    private final OwnerUIHandler ownerUIHandler = new OwnerUIHandler();
    private final PetUIHandler petUIHandler = new PetUIHandler();
    private final BreedUIHandler breedUIHandler = new BreedUIHandler();
    private final AppointmentUIHandler appointmentUIHandler = new AppointmentUIHandler();
    private final ComplexQueryUIHandler complexQueryUIHandler = new ComplexQueryUIHandler();

    public void handleMainMenu() {

        String answer;
        do {
            System.out.println("\n=== VETERINARY CLINIC MANAGEMENT SYSTEM ===");
            System.out.println("Select an option:");
            System.out.println("1 - Owner Management");
            System.out.println("2 - Pet Management"); 
            System.out.println("3 - Breed Management");
            System.out.println("4 - Appointment Management");
            System.out.println("5 - Complex Queries & Reports");
            System.out.println("X - Exit Program");
            System.out.print("Your choice: ");

            answer = sc.nextLine();

            switch (answer) {
            case "1":
                ownerUIHandler.handleOwnerMenu();
                break;
            case "2":
                petUIHandler.handlePetMenu();
                break;
            case "3":
                breedUIHandler.handleBreedMenu();
                break;
            case "4":
                appointmentUIHandler.handleAppointmentMenu();
                break;
            case "5":
                complexQueryUIHandler.handleComplexQueryMenu();
                break;
            case "x":
            case "X":
                System.out.println("Thank you for using the Veterinary Clinic Management System!");
                break;
            default:
                System.out.println("Invalid option. Please try again.");
            }

        } while (!answer.equalsIgnoreCase("X"));

        sc.close();
    }
}