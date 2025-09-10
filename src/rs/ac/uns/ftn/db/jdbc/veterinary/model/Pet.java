package rs.ac.uns.ftn.db.jdbc.veterinary.model;

public class Pet {
    private int petID;
    private String name;
    private int birthYear;
    private String species;
    private Integer ownerID;
    private int breedID;

    public Pet() {
        super();
    }

    public Pet(int petID, String name, int birthYear, String species, Integer ownerID, int breedID) {
        this.petID = petID;
        this.name = name;
        this.birthYear = birthYear;
        this.species = species;
        this.ownerID = ownerID;
        this.breedID = breedID;
    }

    // Getters and Setters
    public int getPetID() {
        return petID;
    }

    public void setPetID(int petID) {
        this.petID = petID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public Integer getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Integer ownerID) {
        this.ownerID = ownerID;
    }

    public int getBreedID() {
        return breedID;
    }

    public void setBreedID(int breedID) {
        this.breedID = breedID;
    }

    @Override
    public String toString() {
        return String.format("%-6d %-15s %-10d %-8s %-8s %-8d", 
            petID, name, birthYear, species, 
            ownerID != null ? ownerID.toString() : "N/A", breedID);
    }

    public static String getFormattedHeader() {
        return String.format("%-6s %-15s %-10s %-8s %-8s %-8s", 
            "ID", "NAME", "BIRTH YEAR", "SPECIES", "OWNER ID", "BREED ID");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + petID;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + birthYear;
        result = prime * result + ((species == null) ? 0 : species.hashCode());
        result = prime * result + ((ownerID == null) ? 0 : ownerID.hashCode());
        result = prime * result + breedID;
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
        Pet other = (Pet) obj;
        if (petID != other.petID)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (birthYear != other.birthYear)
            return false;
        if (species == null) {
            if (other.species != null)
                return false;
        } else if (!species.equals(other.species))
            return false;
        if (ownerID == null) {
            if (other.ownerID != null)
                return false;
        } else if (!ownerID.equals(other.ownerID))
            return false;
        if (breedID != other.breedID)
            return false;
        return true;
    }
}
