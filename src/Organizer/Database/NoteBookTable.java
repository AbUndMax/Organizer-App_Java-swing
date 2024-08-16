package Organizer.Database;

import Organizer.SubPrograms.NoteBookTab.NoteBookEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class NoteBookTable extends Database<NoteBookEntry> {

    public LinkedList<NoteBookEntry> searchInDB(String attribute, String searchQuery) {

        if (!isValidColumn("notebook", attribute)) {
            throw new IllegalArgumentException("Invalid column name <" + attribute + "> for notebook table.");
        }

        String sql = "SELECT * FROM scheduler WHERE " + attribute + " LIKE ?";
        LinkedList<NoteBookEntry> nbEntries = new LinkedList<>();

        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setString(1, "%" + searchQuery + "%");

            ResultSet resultSet = prepStatement.executeQuery();

            while(resultSet.next()) {
                NoteBookEntry nbEntry = new NoteBookEntry(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("note")
                );
                nbEntries.add(nbEntry);
            }

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

        return nbEntries;
    }

    public void updateDB(NoteBookEntry entry) {
        String sql = "UPDATE notebook SET title = ?, note = ? WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setString(1, entry.title());
            prepStatement.setString(2, entry.content());
            prepStatement.setInt(3, entry.id());

            prepStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void deleteDBTuple(int id) {
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
