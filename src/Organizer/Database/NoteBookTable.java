package Organizer.Database;

import Organizer.SubPrograms.NoteBookTab.NoteBookEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class NoteBookTable extends Database<NoteBookEntry> {

    public static LinkedList<NoteBookEntry> loadFullTable() {
        LinkedList<NoteBookEntry> nbEntries = new LinkedList<>();

        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement("SELECT * FROM notebook");
             ResultSet resultSet = prepStatement.executeQuery()) {

            nbEntries = resultToObject(resultSet);

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

        return nbEntries;
    }

    public static String getNoteContent(int id) {
        String sql = "SELECT note FROM notebook WHERE id = ?";
        String content = "";

        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setInt(1, id);

            ResultSet resultSet = prepStatement.executeQuery();

            if (resultSet.next()) {
                content = resultSet.getString("note");
            }

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

        return content;
    }

    private static LinkedList<NoteBookEntry> resultToObject(ResultSet resultSet) throws SQLException {
        LinkedList<NoteBookEntry> nbEntries = new LinkedList<>();
        while(resultSet.next()) {
            NoteBookEntry nbEntry = new NoteBookEntry(
                    resultSet.getInt("id"),
                    resultSet.getString("title")
            );
            nbEntries.add(nbEntry);
        }

        return nbEntries;
    }

    public static NoteBookEntry newDBTuple(String title, String content) {
        String sql = "INSERT INTO notebook (title, note) VALUES (?, ?)";
        int id = -1;

        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            prepStatement.setString(1, title);
            prepStatement.setString(2, content);

            int affectedRows = prepStatement.executeUpdate();

            // check for valid change
            if (affectedRows > 0) {
                // call the ID
                try (ResultSet rs = prepStatement.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

        return new NoteBookEntry(id, title);
    }

    public static void updateDB(NoteBookEntry entry, String content) {
        String sql = "UPDATE notebook SET title = ?, note = ? WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setString(1, entry.title());
            prepStatement.setString(2, content);
            prepStatement.setInt(3, entry.id());

            prepStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static void deleteDBTuple(int id) {
        String sql = "DELETE FROM notebook WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setInt(1, id);
            prepStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

}
