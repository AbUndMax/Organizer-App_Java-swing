package Organizer.Database;

import java.sql.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class Database<T> {

    protected static final String DATABASE_URL = "jdbc:sqlite:database/organizer.sqlite";
    protected static final Logger LOGGER = LoggerFactory.getLogger(Database.class);

    protected static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
        return connection;
    }

    protected static boolean isValidColumn(String table, String column) {
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

            String sqlContactBook = "CREATE TABLE IF NOT EXISTS contact_book (\n"
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + " name TEXT,\n"
                    + " surname TEXT,\n"
                    + " phone_number TEXT,\n"
                    + " street TEXT,\n"
                    + " number TEXT,\n"
                    + " city TEXT,\n"
                    + " postal_code TEXT,\n"
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

            statement.execute(sqlContactBook);
            statement.execute(sqlNotebook);
            statement.execute(sqlScheduler);

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

}
