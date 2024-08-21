package Organizer.SubPrograms.ContactBookTab;

public record ContactBookEntry (int id, String name, String surname, String phoneNumber, String street, String houseNumber, String city, String postalCode, String country) {
    public ContactBookEntry {
        if (id == 0) {
            throw new IllegalArgumentException("id cannot be 0");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }
}
