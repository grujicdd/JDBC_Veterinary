package rs.ac.uns.ftn.db.jdbc.veterinary.dto.complexquery1;

public class PetInfoDTO {
    private int petID;
    private String name;
    private int birthYear;
    private String species;
    private String breedName;
    
    public PetInfoDTO(int petID, String name, int birthYear, String species, String breedName) {
        this.petID = petID;
        this.name = name;
        this.birthYear = birthYear;
        this.species = species;
        this.breedName = breedName;
    }
    
    // Getters
    public int getPetID() { return petID; }
    public String getName() { return name; }
    public int getBirthYear() { return birthYear; }
    public String getSpecies() { return species; }
    public String getBreedName() { return breedName; }
}
