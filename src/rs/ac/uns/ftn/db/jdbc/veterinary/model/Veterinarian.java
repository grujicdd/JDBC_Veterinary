package rs.ac.uns.ftn.db.jdbc.veterinary.model;

public class Veterinarian {
    private int vetID;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Integer supervisorID;

    public Veterinarian() {
        super();
    }

    public Veterinarian(int vetID, String firstName, String lastName, String phoneNumber, Integer supervisorID) {
        this.vetID = vetID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.supervisorID = supervisorID;
    }

    // Getters and Setters
    public int getVetID() {
        return vetID;
    }

    public void setVetID(int vetID) {
        this.vetID = vetID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getSupervisorID() {
        return supervisorID;
    }

    public void setSupervisorID(Integer supervisorID) {
        this.supervisorID = supervisorID;
    }

    @Override
    public String toString() {
        return String.format("%-6d %-15s %-15s %-15s %-12s", 
            vetID, firstName, lastName, phoneNumber, 
            supervisorID != null ? supervisorID.toString() : "N/A");
    }

    public static String getFormattedHeader() {
        return String.format("%-6s %-15s %-15s %-15s %-12s", 
            "ID", "FIRST NAME", "LAST NAME", "PHONE", "SUPERVISOR");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + vetID;
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
        result = prime * result + ((supervisorID == null) ? 0 : supervisorID.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Veterinarian other = (Veterinarian) obj;
        if (vetID != other.vetID)
            return false;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (phoneNumber == null) {
            if (other.phoneNumber != null)
                return false;
        } else if (!phoneNumber.equals(other.phoneNumber))
            return false;
        if (supervisorID == null) {
            if (other.supervisorID != null)
                return false;
        } else if (!supervisorID.equals(other.supervisorID))
            return false;
        return true;
    }
}
