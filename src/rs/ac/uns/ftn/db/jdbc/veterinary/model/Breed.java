package rs.ac.uns.ftn.db.jdbc.veterinary.model;

public class Breed {
    private int breedID;
    private String breedName;

    public Breed() {
        super();
    }

    public Breed(int breedID, String breedName) {
        this.breedID = breedID;
        this.breedName = breedName;
    }

    // Getters and Setters
    public int getBreedID() {
        return breedID;
    }

    public void setBreedID(int breedID) {
        this.breedID = breedID;
    }

    public String getBreedName() {
        return breedName;
    }

    public void setBreedName(String breedName) {
        this.breedName = breedName;
    }

    @Override
    public String toString() {
        return String.format("%-6d %-20s", breedID, breedName);
    }

    public static String getFormattedHeader() {
        return String.format("%-6s %-20s", "ID", "BREED NAME");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + breedID;
        result = prime * result + ((breedName == null) ? 0 : breedName.hashCode());
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
        Breed other = (Breed) obj;
        if (breedID != other.breedID)
            return false;
        if (breedName == null) {
            if (other.breedName != null)
                return false;
        } else if (!breedName.equals(other.breedName))
            return false;
        return true;
    }
}
