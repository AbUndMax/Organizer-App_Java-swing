package Organizer.Tools;

import Organizer.Tabs.PhoneBookTab.PhoneBookEntry;

import java.sql.*;
import java.util.LinkedList;

public class Database {

    private static final String DATABASE_URL = "jdbc:sqlite:database/organizer.sqlite";

    private static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
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
            System.out.println(e.getMessage());
        }
    }

}
