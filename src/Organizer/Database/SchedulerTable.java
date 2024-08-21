package Organizer.Database;

import Organizer.SubPrograms.SchedulerTab.Repetition;
import Organizer.SubPrograms.SchedulerTab.SchedulerEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.TreeSet;

public class SchedulerTable extends Database<SchedulerEntry> {

    public static LinkedList<SchedulerEntry> loadFullTable() {
        LinkedList<SchedulerEntry> sEntries = new LinkedList<>();

        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement("SELECT id, title, start_date, start_time FROM scheduler");
             ResultSet resultSet = prepStatement.executeQuery()) {

            sEntries = resultToObject(resultSet);

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

        return sEntries;
    }

    public static TreeSet<SchedulerEntry>[] loadTuplesOfMonth(Year year, Month month) {

        String sql = "SELECT * FROM scheduler WHERE start_date LIKE ?";
        String formattedMonth = String.format("%02d", month.getValue());
        TreeSet<SchedulerEntry>[] sEntries = new TreeSet[31];
        for (int i = 0; i < sEntries.length; i++) {
            sEntries[i] = new TreeSet<>(Comparator.comparing(SchedulerEntry::getStartDate));
        }

        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setString(1, "%" + year.toString() + "-" + formattedMonth + "%");

            ResultSet resultSet = prepStatement.executeQuery();

            while(resultSet.next()) {
                SchedulerEntry sEntry = new SchedulerEntry(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        LocalDate.parse(resultSet.getString("start_date")),
                        LocalTime.parse(resultSet.getString("start_time"))
                );
                int start_day = sEntry.getStartDate().getDayOfMonth();
                sEntries[start_day - 1].add(sEntry);
            }

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

        return sEntries;
    }

    private static LinkedList<SchedulerEntry> resultToObject(ResultSet resultSet) throws SQLException {
        LinkedList<SchedulerEntry> sEntries = new LinkedList<>();
        while(resultSet.next()) {
            SchedulerEntry sEntry = new SchedulerEntry(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    LocalDate.parse(resultSet.getString("start_date")),
                    LocalTime.parse(resultSet.getString("start_time"))
            );
            sEntries.add(sEntry);
        }
        return sEntries;
    }

    public static void loadFullEntry(SchedulerEntry entry) {
        String sql = "SELECT end_date, end_time, repetition, number_of_repetition, description FROM scheduler WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setInt(1, entry.getId());
            ResultSet resultSet = prepStatement.executeQuery();

            while (resultSet.next()) {
                entry.setEndDate(LocalDate.parse(resultSet.getString("end_date")));
                entry.setEndTime(LocalTime.parse(resultSet.getString("end_time")));
                entry.setRepetition(Repetition.valueOf(resultSet.getString("repetition")));
                entry.setNumberOfRepetition(Integer.parseInt(resultSet.getString("number_of_repetition")));
                entry.setDescription(resultSet.getString("description"));
            }

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static void updateDB(SchedulerEntry entry) {
        String sql = "UPDATE scheduler SET title = ?, start_date = ?, end_date = ?, start_time = ?, end_time = ?, repetition = ?, number_of_repetition = ?, description = ? WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setString(1, entry.getTitle());
            prepStatement.setString(2, entry.getStartDate().toString());
            prepStatement.setString(3, entry.getEndDate().toString());
            prepStatement.setString(4, entry.getStartTime().toString());
            prepStatement.setString(5, entry.getEndTime().toString());
            prepStatement.setString(6, entry.getRepetition().toString());
            prepStatement.setInt(7, entry.getNumberOfRepetition());
            prepStatement.setString(8, entry.getDescription());
            prepStatement.setInt(9, entry.getId());

            prepStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static void deleteDBTuple(int id) {
        String sql = "DELETE FROM scheduler WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setInt(1, id);
            prepStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static SchedulerEntry newTuple(String title, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, Repetition repetition, int numberOfRepetition, String description) {
        String sql = "INSERT INTO scheduler (title, start_date, end_date, start_time, end_time, repetition, number_of_repetition, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int id = -1;

        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            prepStatement.setString(1, title);
            prepStatement.setString(2, startDate.toString());
            prepStatement.setString(3, endDate.toString());
            prepStatement.setString(4, startTime.toString());
            prepStatement.setString(5, endTime.toString());
            prepStatement.setString(6, repetition.toString());
            prepStatement.setInt(7, numberOfRepetition);
            prepStatement.setString(8, description);

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

        return new SchedulerEntry(id, title, startDate, startTime);
    }

}
