package Organizer.Database;

import Organizer.SubPrograms.SchedulerTab.Repetition;
import Organizer.SubPrograms.SchedulerTab.SchedulerEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;

public class SchedulerTable extends Database<SchedulerEntry> {

    public LinkedList<SchedulerEntry> searchInDB(String attribute, String searchQuery) {

        if (!isValidColumn("scheduler", attribute)) {
            throw new IllegalArgumentException("Invalid column name <" + attribute + "> for scheduler table.");
        }

        String sql = "SELECT * FROM scheduler WHERE " + attribute + " LIKE ?";
        LinkedList<SchedulerEntry> sEntries = new LinkedList<>();

        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setString(1, "%" + searchQuery + "%");

            ResultSet resultSet = prepStatement.executeQuery();

            while(resultSet.next()) {

                SchedulerEntry sEntry = new SchedulerEntry(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        LocalDate.parse(resultSet.getString("start_date")),
                        LocalDate.parse(resultSet.getString("end_date")),
                        LocalTime.parse(resultSet.getString("start_time")),
                        LocalTime.parse(resultSet.getString("end_time")),
                        Repetition.parse(resultSet.getString("repetition")),
                        resultSet.getInt("number_of_repetition"),
                        resultSet.getString("description")
                );
                sEntries.add(sEntry);
            }

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

        return sEntries;
    }

    public void updateDB(SchedulerEntry entry) {
        String sql = "UPDATE scheduler SET title = ?, start_date = ?, end_date = ?, start_time = ?, end_time = ?, repetition = ?, number_of_repetition = ?, description = ? WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setString(1, entry.title());
            prepStatement.setString(2, entry.startDate().toString());
            prepStatement.setString(3, entry.endDate().toString());
            prepStatement.setString(4, entry.startTime().toString());
            prepStatement.setString(5, entry.endTime().toString());
            prepStatement.setString(6, entry.repetition().toString());
            prepStatement.setInt(7, entry.numberOfRepetition());
            prepStatement.setString(8, entry.description());
            prepStatement.setInt(9, entry.id());

            prepStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void deleteDBTuple(int id) {
        String sql = "DELETE FROM scheduler WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setInt(1, id);
            prepStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

}
