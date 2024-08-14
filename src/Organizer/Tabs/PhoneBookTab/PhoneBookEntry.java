package Organizer.Tabs.PhoneBookTab;

public record PhoneBookEntry(Integer id, String name, String surName, int phoneNumber) {
    public PhoneBookEntry {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
    }
}
