package rs.ac.uns.ftn.db.jdbc.veterinary.ui_handler;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rs.ac.uns.ftn.db.jdbc.veterinary.model.Appointment;
import rs.ac.uns.ftn.db.jdbc.veterinary.service.AppointmentService;

public class AppointmentUIHandler {

    private static final AppointmentService appointmentService = new AppointmentService();
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public void handleAppointmentMenu() {
        String answer;
        do {
            System.out.println("\n=== APPOINTMENT MANAGEMENT ===");
            System.out.println("Select an option:");
            System.out.println("1 - Show all appointments");
            System.out.println("2 - Show appointment by ID");
            System.out.println("3 - Show appointments by pet ID");
            System.out.println("4 - Show appointments by veterinarian ID");
            System.out.println("5 - Add new appointment");
            System.out.println("6 - Update appointment");
            System.out.println("7 - Delete appointment");
            System.out.println("8 - Show appointments by date range");
            System.out.println("9 - Search appointments by reason");
            System.out.println("10 - Show appointments between specific pet and vet");
            System.out.println("11 - Show appointments with diagnoses");
            System.out.println("12 - Show appointment statistics");
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
                showByPetID();
                break;
            case "4":
                showByVetID();
                break;
            case "5":
                addAppointment();
                break;
            case "6":
                updateAppointment();
                break;
            case "7":
                deleteAppointment();
                break;
            case "8":
                showByDateRange();
                break;
            case "9":
                searchByReason();
                break;
            case "10":
                showByPetAndVet();
                break;
            case "11":
                showAppointmentsWithDiagnoses();
                break;
            case "12":
                showStatistics();
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
        System.out.println("\n=== ALL APPOINTMENTS ===");
        System.out.println(Appointment.getFormattedHeader());
        System.out.println("─────────────────────────────────────────────────────────────────────────────────────────");

        try {
            List<Appointment> appointments = appointmentService.getAll();
            if (appointments.isEmpty()) {
                System.out.println("No appointments found.");
            } else {
                for (Appointment appointment : appointments) {
                    System.out.println(appointment);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving appointments: " + e.getMessage());
        }
    }

    private void showById() {
        System.out.print("Enter Appointment ID: ");
        try {
            int id = Integer.parseInt(MainUIHandler.sc.nextLine());
            Appointment appointment = appointmentService.findById(id);
            
            if (appointment != null) {
                System.out.println("\n" + Appointment.getFormattedHeader());
                System.out.println("─────────────────────────────────────────────────────────────────────────────────────────");
                System.out.println(appointment);
            } else {
                System.out.println("Appointment with ID " + id + " not found.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a number.");
        } catch (SQLException e) {
            System.err.println("Error retrieving appointment: " + e.getMessage());
        }
    }

    private void showByPetID() {
        System.out.print("Enter Pet ID: ");
        try {
            int petID = Integer.parseInt(MainUIHandler.sc.nextLine());
            List<Appointment> appointments = appointmentService.findByPetID(petID);
            
            if (appointments.isEmpty()) {
                System.out.println("No appointments found for Pet ID: " + petID);
            } else {
                System.out.println("\n=== APPOINTMENTS FOR PET ID " + petID + " ===");
                System.out.println(Appointment.getFormattedHeader());
                System.out.println("─────────────────────────────────────────────────────────────────────────────────────────");
                for (Appointment appointment : appointments) {
                    System.out.println(appointment);
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid Pet ID format. Please enter a number.");
        } catch (SQLException e) {
            System.err.println("Error retrieving appointments: " + e.getMessage());
        }
    }

    private void showByVetID() {
        System.out.print("Enter Veterinarian ID: ");
        try {
            int vetID = Integer.parseInt(MainUIHandler.sc.nextLine());
            List<Appointment> appointments = appointmentService.findByVetID(vetID);
            
            if (appointments.isEmpty()) {
                System.out.println("No appointments found for Veterinarian ID: " + vetID);
            } else {
                System.out.println("\n=== APPOINTMENTS FOR VETERINARIAN ID " + vetID + " ===");
                System.out.println(Appointment.getFormattedHeader());
                System.out.println("─────────────────────────────────────────────────────────────────────────────────────────");
                for (Appointment appointment : appointments) {
                    System.out.println(appointment);
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid Veterinarian ID format. Please enter a number.");
        } catch (SQLException e) {
            System.err.println("Error retrieving appointments: " + e.getMessage());
        }
    }

    private void addAppointment() {
        System.out.println("\n=== ADD NEW APPOINTMENT ===");
        
        try {
            System.out.print("Enter Pet ID: ");
            int petID = Integer.parseInt(MainUIHandler.sc.nextLine());
            
            System.out.print("Enter Veterinarian ID: ");
            int vetID = Integer.parseInt(MainUIHandler.sc.nextLine());
            
            System.out.print("Enter appointment date and time (dd.MM.yyyy HH:mm): ");
            String dateTimeStr = MainUIHandler.sc.nextLine();
            Date appointmentDateTime;
            
            try {
                appointmentDateTime = dateTimeFormat.parse(dateTimeStr);
            } catch (ParseException e) {
                System.err.println("Invalid date format. Please use dd.MM.yyyy HH:mm");
                return;
            }
            
            // Check if appointment can be scheduled (business rule)
            if (!appointmentService.canScheduleAppointment(petID, vetID, appointmentDateTime)) {
                System.out.println("Cannot schedule appointment: Too close to existing appointment (must be at least 24 hours apart)");
                return;
            }
            
            System.out.print("Enter reason for appointment: ");
            String reason = MainUIHandler.sc.nextLine();
            
            Appointment appointment = new Appointment(petID, vetID, appointmentDateTime, reason);
            
            if (appointmentService.save(appointment)) {
                System.out.println("Appointment added successfully! Appointment ID: " + appointment.getAppointmentID());
            } else {
                System.out.println("Failed to add appointment.");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter valid numbers.");
        } catch (SQLException e) {
            System.err.println("Error adding appointment: " + e.getMessage());
        }
    }

    private void updateAppointment() {
        System.out.println("\n=== UPDATE APPOINTMENT ===");
        
        try {
            System.out.print("Enter Appointment ID to update: ");
            int appointmentID = Integer.parseInt(MainUIHandler.sc.nextLine());
            
            Appointment existing = appointmentService.findById(appointmentID);
            if (existing == null) {
                System.out.println("No appointment found with ID " + appointmentID);
                return;
            }
            
            System.out.println("Current appointment details:");
            System.out.println(Appointment.getFormattedHeader());
            System.out.println(existing);
            
            System.out.print("Enter new Pet ID (current: " + existing.getPetID() + "): ");
            String petIDStr = MainUIHandler.sc.nextLine();
            int petID = existing.getPetID();
            if (!petIDStr.trim().isEmpty()) {
                petID = Integer.parseInt(petIDStr);
            }
            
            System.out.print("Enter new Veterinarian ID (current: " + existing.getVetID() + "): ");
            String vetIDStr = MainUIHandler.sc.nextLine();
            int vetID = existing.getVetID();
            if (!vetIDStr.trim().isEmpty()) {
                vetID = Integer.parseInt(vetIDStr);
            }
            
            System.out.print("Enter new date and time (dd.MM.yyyy HH:mm) (current: " + 
                           dateTimeFormat.format(existing.getAppDateTime()) + "): ");
            String dateTimeStr = MainUIHandler.sc.nextLine();
            Date appointmentDateTime = existing.getAppDateTime();
            
            if (!dateTimeStr.trim().isEmpty()) {
                try {
                    appointmentDateTime = dateTimeFormat.parse(dateTimeStr);
                } catch (ParseException e) {
                    System.err.println("Invalid date format. Keeping current date/time.");
                    appointmentDateTime = existing.getAppDateTime();
                }
            }
            
            System.out.print("Enter new reason (current: " + existing.getReason() + "): ");
            String reason = MainUIHandler.sc.nextLine();
            if (reason.trim().isEmpty()) {
                reason = existing.getReason();
            }
            
            Appointment updatedAppointment = new Appointment(appointmentID, petID, vetID, appointmentDateTime, reason);
            
            if (appointmentService.save(updatedAppointment)) {
                System.out.println("Appointment updated successfully!");
            } else {
                System.out.println("Failed to update appointment.");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter valid numbers.");
        } catch (SQLException e) {
            System.err.println("Error updating appointment: " + e.getMessage());
        }
    }

    private void deleteAppointment() {
        System.out.println("\n=== DELETE APPOINTMENT ===");
        
        try {
            System.out.print("Enter Appointment ID to delete: ");
            int appointmentID = Integer.parseInt(MainUIHandler.sc.nextLine());
            
            Appointment appointment = appointmentService.findById(appointmentID);
            if (appointment == null) {
                System.out.println("No appointment found with ID " + appointmentID);
                return;
            }
            
            System.out.println("Appointment to delete:");
            System.out.println(Appointment.getFormattedHeader());
            System.out.println(appointment);
            
            System.out.print("Are you sure you want to delete this appointment? (y/N): ");
            String confirmation = MainUIHandler.sc.nextLine();
            
            if (confirmation.equalsIgnoreCase("y") || confirmation.equalsIgnoreCase("yes")) {
                if (appointmentService.deleteById(appointmentID)) {
                    System.out.println("Appointment deleted successfully!");
                } else {
                    System.out.println("Failed to delete appointment. It may have related diagnoses.");
                }
            } else {
                System.out.println("Deletion cancelled.");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter a valid number.");
        } catch (SQLException e) {
            System.err.println("Error deleting appointment: " + e.getMessage());
        }
    }

    private void showByDateRange() {
        System.out.println("\n=== APPOINTMENTS BY DATE RANGE ===");
        
        try {
            System.out.print("Enter start date (dd.MM.yyyy): ");
            String startDateStr = MainUIHandler.sc.nextLine();
            
            System.out.print("Enter end date (dd.MM.yyyy): ");
            String endDateStr = MainUIHandler.sc.nextLine();
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);
            
            // Set end date to end of day
            endDate = new Date(endDate.getTime() + 24 * 60 * 60 * 1000 - 1);
            
            List<Appointment> appointments = appointmentService.findByDateRange(startDate, endDate);
            
            if (appointments.isEmpty()) {
                System.out.println("No appointments found in the specified date range.");
            } else {
                System.out.println("Found " + appointments.size() + " appointments:");
                System.out.println(Appointment.getFormattedHeader());
                System.out.println("─────────────────────────────────────────────────────────────────────────────────────────");
                for (Appointment appointment : appointments) {
                    System.out.println(appointment);
                }
            }
            
        } catch (ParseException e) {
            System.err.println("Invalid date format. Please use dd.MM.yyyy");
        } catch (SQLException e) {
            System.err.println("Error retrieving appointments: " + e.getMessage());
        }
    }

    private void searchByReason() {
        System.out.print("Enter reason to search for: ");
        String reason = MainUIHandler.sc.nextLine();
        
        try {
            List<Appointment> appointments = appointmentService.findByReason(reason);
            
            if (appointments.isEmpty()) {
                System.out.println("No appointments found with reason containing: " + reason);
            } else {
                System.out.println("\n=== APPOINTMENTS WITH REASON CONTAINING: " + reason + " ===");
                System.out.println(Appointment.getFormattedHeader());
                System.out.println("─────────────────────────────────────────────────────────────────────────────────────────");
                for (Appointment appointment : appointments) {
                    System.out.println(appointment);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching appointments: " + e.getMessage());
        }
    }

    private void showByPetAndVet() {
        System.out.println("\n=== APPOINTMENTS BETWEEN SPECIFIC PET AND VETERINARIAN ===");
        
        try {
            System.out.print("Enter Pet ID: ");
            int petID = Integer.parseInt(MainUIHandler.sc.nextLine());
            
            System.out.print("Enter Veterinarian ID: ");
            int vetID = Integer.parseInt(MainUIHandler.sc.nextLine());
            
            List<Appointment> appointments = appointmentService.findByPetAndVet(petID, vetID);
            
            if (appointments.isEmpty()) {
                System.out.println("No appointments found between Pet ID " + petID + " and Veterinarian ID " + vetID);
            } else {
                System.out.println("\n=== APPOINTMENTS BETWEEN PET " + petID + " AND VETERINARIAN " + vetID + " ===");
                System.out.println("Total appointments: " + appointments.size());
                System.out.println(Appointment.getFormattedHeader());
                System.out.println("─────────────────────────────────────────────────────────────────────────────────────────");
                for (Appointment appointment : appointments) {
                    System.out.println(appointment);
                }
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format. Please enter valid numbers.");
        } catch (SQLException e) {
            System.err.println("Error retrieving appointments: " + e.getMessage());
        }
    }

    private void showAppointmentsWithDiagnoses() {
        try {
            List<Appointment> appointments = appointmentService.findAppointmentsWithDiagnosis();
            
            if (appointments.isEmpty()) {
                System.out.println("No appointments found with diagnoses.");
            } else {
                System.out.println("\n=== APPOINTMENTS WITH DIAGNOSES ===");
                System.out.println(Appointment.getFormattedHeader());
                System.out.println("─────────────────────────────────────────────────────────────────────────────────────────");
                for (Appointment appointment : appointments) {
                    System.out.println(appointment);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving appointments with diagnoses: " + e.getMessage());
        }
    }

    private void showStatistics() {
        try {
            int totalAppointments = appointmentService.getAppointmentCount();
            List<Appointment> appointmentsWithDiagnoses = appointmentService.findAppointmentsWithDiagnosis();
            
            System.out.println("\n=== APPOINTMENT STATISTICS ===");
            System.out.println("Total appointments: " + totalAppointments);
            System.out.println("Appointments with diagnoses: " + appointmentsWithDiagnoses.size());
            System.out.println("Appointments without diagnoses: " + (totalAppointments - appointmentsWithDiagnoses.size()));
            
            if (totalAppointments > 0) {
                double diagnosisRate = (double) appointmentsWithDiagnoses.size() / totalAppointments * 100;
                System.out.printf("Diagnosis rate: %.1f%%%n", diagnosisRate);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving appointment statistics: " + e.getMessage());
        }
    }
}