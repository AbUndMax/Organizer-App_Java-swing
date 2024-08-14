package Organizer.Tabs.AddressBookTab;

public record AddressBookEntry(Integer ID, String name, String street, int number, String city, int postalCode, String country) {
    public AddressBookEntry {
        if (ID == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }
}
