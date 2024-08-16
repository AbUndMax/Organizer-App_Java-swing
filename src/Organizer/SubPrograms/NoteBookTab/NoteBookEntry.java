package Organizer.SubPrograms.NoteBookTab;

public record NoteBookEntry(Integer id, String title, String content) {
    public NoteBookEntry {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
    }
}
