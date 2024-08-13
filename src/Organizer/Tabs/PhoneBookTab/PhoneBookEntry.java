package Organizer.Tabs.PhoneBookTab;

public record PhoneBookEntry(Integer ID, String name, String surName, int phoneNumber) {
    public PhoneBookEntry {
        if (ID == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
    }
}
