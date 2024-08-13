package Organizer.Tabs.NoteBookTab;

public record NoteBookEntry(Integer ID, String title, String content) {
    public NoteBookEntry {
        if (ID == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
    }
}
