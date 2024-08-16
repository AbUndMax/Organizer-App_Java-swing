package Organizer.Database;

import Organizer.SubPrograms.ContactBookTab.ContactBookEntry;

import java.sql.*;
import java.util.LinkedList;

public class ContactBookTable extends Database<ContactBookEntry> {

    public LinkedList<ContactBookEntry> loadFullTable() {
        LinkedList<ContactBookEntry> abEntries = new LinkedList<>();

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM contact_book";
            ResultSet resultSet = statement.executeQuery(sql);

            abEntries = resultToObject(resultSet);

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

        return abEntries;
    }

    public LinkedList<ContactBookEntry> searchInDB(String attribute, String searchQuery) {

        switch (attribute) {
            case "Name":
                attribute = "name";
                break;
            case "Surname":
                attribute = "surname";
                break;
            case "Phone Number":
                attribute = "phone_number";
                break;
            case "Street":
                attribute = "street";
                break;
            case "House Number":
                attribute = "number";
                break;
            case "City":
                attribute = "city";
                break;
            case "Postal Code":
                attribute = "postal_code";
                break;
            case "Country":
                attribute = "country";
                break;
        }

        if (!isValidColumn("contact_book", attribute)) {
            throw new IllegalArgumentException("Invalid column name <" + attribute + "> for address_book table.");
        }

        String sql = "SELECT * FROM contact_book WHERE " + attribute + " LIKE ?";
        LinkedList<ContactBookEntry> abEntries = new LinkedList<>();

        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setString(1, "%" + searchQuery + "%");

            ResultSet resultSet = prepStatement.executeQuery();

            abEntries = resultToObject(resultSet);

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

        return abEntries;
    }

    private LinkedList<ContactBookEntry> resultToObject(ResultSet resultSet) throws SQLException {
        LinkedList<ContactBookEntry> abEntries = new LinkedList<>();
        while(resultSet.next()) {
            ContactBookEntry abEntry = new ContactBookEntry(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("surname"),
                    resultSet.getInt("phone_number"),
                    resultSet.getString("street"),
                    resultSet.getInt("number"),
                    resultSet.getString("city"),
                    resultSet.getInt("postal_code"),
                    resultSet.getString("country")
            );
            abEntries.add(abEntry);
        }

        return abEntries;
    }

    public ContactBookEntry newDBTuple(String name, String surname, int phoneNumber, String street, int houseNumber, String city, int postalCode, String country) {
        String sql = "INSERT INTO contact_book (name, surname, phone_number, street, number, city, postal_code, country) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int id = -1;
        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            prepStatement.setString(1, name);
            prepStatement.setString(2, surname);
            prepStatement.setInt(3, phoneNumber);
            prepStatement.setString(4, street);
            prepStatement.setInt(5, houseNumber);
            prepStatement.setString(6, city);
            prepStatement.setInt(7, postalCode);
            prepStatement.setString(8, country);

            // Ausführen des Insert-Statements
            int affectedRows = prepStatement.executeUpdate();

            // Überprüfen, ob ein Datensatz eingefügt wurde
            if (affectedRows > 0) {
                // Abrufen der generierten ID
                try (ResultSet rs = prepStatement.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

        return new ContactBookEntry(id, name, surname, phoneNumber, street, houseNumber, city, postalCode, country);
    }

    public void updateDB(ContactBookEntry entry) {
        String sql = "UPDATE contact_book SET name = ?, surname = ?, phone_number = ?, street = ?, number = ?, city = ?, postal_code = ?, country = ? WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setString(1, entry.name());
            prepStatement.setString(2, entry.surname());
            prepStatement.setInt(3, entry.phoneNumber());
            prepStatement.setString(4, entry.street());
            prepStatement.setInt(5, entry.houseNumber());
            prepStatement.setString(6, entry.city());
            prepStatement.setInt(7, entry.postalCode());
            prepStatement.setString(8, entry.country());
            prepStatement.setInt(9, entry.id());

            prepStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void deleteDBTuple(int id) {
        String sql = "DELETE FROM contact_book WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement prepStatement = connection.prepareStatement(sql)) {

            prepStatement.setInt(1, id);
            prepStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

}
