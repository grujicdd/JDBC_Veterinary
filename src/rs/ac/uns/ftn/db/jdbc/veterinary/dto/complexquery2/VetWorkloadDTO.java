package rs.ac.uns.ftn.db.jdbc.veterinary.dto.complexquery2;

public class VetWorkloadDTO {
    private int vetID;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String supervisorName;
    private int appointmentCount;
    private int subordinateCount;
    private String workloadLevel;
    
    public VetWorkloadDTO(int vetID, String firstName, String lastName, String phoneNumber,
                         String supervisorName, int appointmentCount, int subordinateCount, String workloadLevel) {
        this.vetID = vetID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.supervisorName = supervisorName;
        this.appointmentCount = appointmentCount;
        this.subordinateCount = subordinateCount;
        this.workloadLevel = workloadLevel;
    }
    
    public void display() {
        System.out.printf("%-4d %-15s %-15s %-15s %-15s %-12d %-12d %-10s%n",
            vetID, firstName, lastName, phoneNumber,
            supervisorName != null ? supervisorName : "N/A",
            appointmentCount, subordinateCount, workloadLevel);
    }
    
    // Getters
    public int getVetID() { return vetID; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getAppointmentCount() { return appointmentCount; }
    public int getSubordinateCount() { return subordinateCount; }
    public String getWorkloadLevel() { return workloadLevel; }
}
