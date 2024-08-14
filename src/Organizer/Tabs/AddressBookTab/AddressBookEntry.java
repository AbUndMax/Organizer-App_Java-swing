package Organizer.Tabs.AddressBookTab;

public record AddressBookEntry(Integer id, String name, String street, int number, String city, int postalCode, String country) {
    public AddressBookEntry {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }
}
