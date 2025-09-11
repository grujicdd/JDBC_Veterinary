package rs.ac.uns.ftn.db.jdbc.veterinary.dto.complexquery2;

public class ComplexVetAnalysisDTO {
    private int vetID;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String supervisorName;
    private int totalAppointments;
    private int uniquePetsServed;
    private int uniqueOwnersServed;
    private int breedVariety;
    private double avgPetAge;
    private java.sql.Timestamp lastAppointmentDate;
    private String workloadCategory;
    
    public ComplexVetAnalysisDTO(int vetID, String firstName, String lastName, String phoneNumber,
                               String supervisorName, int totalAppointments, int uniquePetsServed, 
                               int uniqueOwnersServed, int breedVariety, double avgPetAge,
                               java.sql.Timestamp lastAppointmentDate, String workloadCategory) {
        this.vetID = vetID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.supervisorName = supervisorName;
        this.totalAppointments = totalAppointments;
        this.uniquePetsServed = uniquePetsServed;
        this.uniqueOwnersServed = uniqueOwnersServed;
        this.breedVariety = breedVariety;
        this.avgPetAge = avgPetAge;
        this.lastAppointmentDate = lastAppointmentDate;
        this.workloadCategory = workloadCategory;
    }
    
    public void display() {
        System.out.printf("%-4d %-12s %-12s %-15s %-15s %-8d %-8d %-8d %-8d %-8.1f %-12s%n",
            vetID, firstName, lastName,
            supervisorName != null ? supervisorName : "N/A",
            phoneNumber.length() > 15 ? phoneNumber.substring(0, 12) + "..." : phoneNumber,
            totalAppointments, uniquePetsServed, uniqueOwnersServed, breedVariety, 
            avgPetAge, workloadCategory);
    }
    
    // Getters
    public int getVetID() { return vetID; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getTotalAppointments() { return totalAppointments; }
    public double getAvgPetAge() { return avgPetAge; }
    public String getWorkloadCategory() { return workloadCategory; }
}