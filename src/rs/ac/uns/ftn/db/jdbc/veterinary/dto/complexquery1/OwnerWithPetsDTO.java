package rs.ac.uns.ftn.db.jdbc.veterinary.dto.complexquery1;

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.db.jdbc.veterinary.dto.complexquery1.PetInfoDTO;

public class OwnerWithPetsDTO {
    private int ownerID;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private java.util.Date birthDate;
    private List<PetInfoDTO> pets;
    
    public OwnerWithPetsDTO(int ownerID, String firstName, String lastName, String phoneNumber, java.util.Date birthDate) {
        this.ownerID = ownerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.pets = new ArrayList<>();
    }
    
    public void addPet(PetInfoDTO pet) {
        this.pets.add(pet);
    }
    
    public void display() {
        System.out.printf("Owner: %s %s (ID: %d) | Phone: %s%n", 
            firstName, lastName, ownerID, phoneNumber);
        
        if (pets.isEmpty()) {
            System.out.println("  Pets: NO PETS");
        } else {
            System.out.println("  Pets:");
            for (PetInfoDTO pet : pets) {
                System.out.printf("    • %s (%s, %s, born %d)%n", 
                    pet.getName(), pet.getSpecies(), pet.getBreedName(), pet.getBirthYear());
            }
            System.out.printf("  Total pets: %d%n", pets.size());
        }
    }
    
    // Getters
    public int getOwnerID() { return ownerID; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public List<PetInfoDTO> getPets() { return pets; }
}
