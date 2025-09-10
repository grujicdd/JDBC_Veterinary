package rs.ac.uns.ftn.db.jdbc.veterinary.model;

import java.util.Date;

public class Appointment {
    private int petID;
    private int vetID;
    private Date appDateTime;
    private String reason;

    public Appointment() {
        super();
    }

    public Appointment(int petID, int vetID, Date appDateTime, String reason) {
        this.petID = petID;
        this.vetID = vetID;
        this.appDateTime = appDateTime;
        this.reason = reason;
    }

    // Getters and Setters
    public int getPetID() {
        return petID;
    }

    public void setPetID(int petID) {
        this.petID = petID;
    }

    public int getVetID() {
        return vetID;
    }

    public void setVetID(int vetID) {
        this.vetID = vetID;
    }

    public Date getAppDateTime() {
        return appDateTime;
    }

    public void setAppDateTime(Date appDateTime) {
        this.appDateTime = appDateTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return String.format("%-8d %-8d %-20s %-30s", 
            petID, vetID, 
            appDateTime != null ? appDateTime.toString() : "N/A", 
            reason != null ? reason : "N/A");
    }

    public static String getFormattedHeader() {
        return String.format("%-8s %-8s %-20s %-30s", 
            "PET ID", "VET ID", "DATE & TIME", "REASON");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + petID;
        result = prime * result + vetID;
        result = prime * result + ((appDateTime == null) ? 0 : appDateTime.hashCode());
        result = prime * result + ((reason == null) ? 0 : reason.hashCode());
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
        Appointment other = (Appointment) obj;
        if (petID != other.petID)
            return false;
        if (vetID != other.vetID)
            return false;
        if (appDateTime == null) {
            if (other.appDateTime != null)
                return false;
        } else if (!appDateTime.equals(other.appDateTime))
            return false;
        if (reason == null) {
            if (other.reason != null)
                return false;
        } else if (!reason.equals(other.reason))
            return false;
        return true;
    }
}