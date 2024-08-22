package Organizer.Database;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.*;

import Organizer.Main;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class Database {

    protected static final Logger LOGGER = LoggerFactory.getLogger(Database.class);
    protected static final File dataBase = getDataBase();
    protected static final String DATABASE_URL = "jdbc:sqlite:" + dataBase.getAbsolutePath();

    protected static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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

    public static File getDataBase() {

        File dataBase = null;

        try {
            // Ermitteln des Verzeichnisses, in dem die Anwendung ausgeführt wird
            File jarFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            String jarDir = jarFile.getParentFile().getPath();
            dataBase = new File(jarDir + File.separator + "data.sqlite");

        } catch (URISyntaxException e) {
            LOGGER.error(e.getMessage());
        }

        System.out.println(dataBase.getAbsolutePath());
        return dataBase;
    }

    public static void createDatabase() {

        try {
            if (dataBase.createNewFile()) {

                System.out.println("New file created");

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

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

    }

}
