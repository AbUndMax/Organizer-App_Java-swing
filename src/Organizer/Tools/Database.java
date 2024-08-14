package Organizer.Tools;

import Organizer.Tabs.AddressBookTab.AddressBookEntry;
import Organizer.Tabs.NoteBookTab.NoteBookEntry;
import Organizer.Tabs.PhoneBookTab.PhoneBookEntry;
import Organizer.Tabs.SchedulerTab.Repetition;
import Organizer.Tabs.SchedulerTab.SchedulerEntry;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class Database {

    private static final String DATABASE_URL = "jdbc:sqlite:database/organizer.sqlite";
    private static final Logger LOGGER = LoggerFactory.getLogger(Database.class);

    private static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
        return connection;
    }

    private static boolean isValidColumn(String table, String column) {
        String sql = "PRAGMA table_info(" + table + ");";
        boolean isValid = false;

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()) {
                if (resultSet.getString("name").equals(column)) {
                    isValid = true;
                    break;
                }
            }

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

        return isValid;
    }

    public static void createDatabase() {
        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {

            String sqlPhoneBook = "CREATE TABLE IF NOT EXISTS phone_book (\n"
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + " name TEXT,\n"
                    + " surname TEXT,\n"
                    + " phone_number INTEGER\n"
                    + ");";

            String sqlAddressBook = "CREATE TABLE IF NOT EXISTS address_book (\n"
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + " name TEXT,\n"
                    + " street TEXT,\n"
                    + " number INTEGER,\n"
                    + " city TEXT,\n"
                    + " postal_code INTEGER,\n"
                    + " country TEXT\n"
                    + ");";

            String sqlNotebook = "CREATE TABLE IF NOT EXISTS notebook (\n"
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + " title TEXT,\n"
                    + " note TEXT\n"
                    + ");";

            String sqlScheduler = "CREATE TABLE IF NOT EXISTS scheduler (\n"
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + " title TEXT,\n"
                    + " start_date TEXT,\n"
                    + " end_date TEXT,\n"
                    + " start_time TEXT,\n"
                    + " end_time TEXT,\n"
                    + " repetition TEXT,\n"
                    + " number_of_repetition INTEGER,\n"
                    + " description TEXT\n"
                    + ");";

            statement.execute(sqlPhoneBook);
            statement.execute(sqlAddressBook);
            statement.execute(sqlNotebook);
            statement.execute(sqlScheduler);

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static LinkedList<PhoneBookEntry> searchInPhoneBook(String attribute, String searchQuery) {

        if (!isValidColumn("phone_book", attribute)) {
            throw new IllegalArgumentException("Invalid column name <" + attribute + "> for phone_book table.");
        }

        String sql = "SELECT * FROM scheduler WHERE " + attribute + " LIKE ?";
        LinkedList<PhoneBookEntry> pbEntries = new LinkedList<>();

        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setString(1, attribute);
            prepStatement.setString(2, "%" + searchQuery + "%");

            ResultSet resultSet = prepStatement.executeQuery();

            while(resultSet.next()) {
                PhoneBookEntry pbEntry = new PhoneBookEntry(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getInt("phone_number")
                );
                pbEntries.add(pbEntry);
            }

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

        return pbEntries;
    }

    public static LinkedList<AddressBookEntry> searchInAddressBook(String attribute, String searchQuery) {

        if (!isValidColumn("address_book", attribute)) {
            throw new IllegalArgumentException("Invalid column name <" + attribute + "> for address_book table.");
        }

        String sql = "SELECT * FROM scheduler WHERE " + attribute + " LIKE ?";
        LinkedList<AddressBookEntry> abEntries = new LinkedList<>();

        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setString(1, attribute);
            prepStatement.setString(2, "%" + searchQuery + "%");

            ResultSet resultSet = prepStatement.executeQuery();

            while(resultSet.next()) {
                AddressBookEntry abEntry = new AddressBookEntry(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("street"),
                        resultSet.getInt("number"),
                        resultSet.getString("city"),
                        resultSet.getInt("postal_code"),
                        resultSet.getString("country")
                );
                abEntries.add(abEntry);
            }

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

        return abEntries;
    }

    public static LinkedList<NoteBookEntry> searchInNoteBook(String attribute, String searchQuery) {

        if (!isValidColumn("notebook", attribute)) {
            throw new IllegalArgumentException("Invalid column name <" + attribute + "> for notebook table.");
        }

        String sql = "SELECT * FROM scheduler WHERE " + attribute + " LIKE ?";
        LinkedList<NoteBookEntry> nbEntries = new LinkedList<>();

        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setString(1, attribute);
            prepStatement.setString(2, "%" + searchQuery + "%");

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

    public static LinkedList<SchedulerEntry> searchInScheduler(String attribute, String searchQuery) {

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

}
