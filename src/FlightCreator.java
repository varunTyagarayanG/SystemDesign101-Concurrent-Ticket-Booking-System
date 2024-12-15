import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class FlightCreator {
    private static final String URL = "jdbc:mysql://localhost:3306/TicketBookingSystem";
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "YourPASS"; // Replace with your MySQL password

    public static void main(String[] args) {
        try {
            // Step 1: Connect to the MySQL database
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database!");

            // Step 2: Create the "trips" table if it doesn't already exist
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS trips (
                    flight_id INT AUTO_INCREMENT PRIMARY KEY,
                    flight_name VARCHAR(100) NOT NULL
                );
            """;
            Statement statement = connection.createStatement();
            statement.execute(createTableSQL);
            System.out.println("Table 'trips' created or already exists!");

            // Step 3: Insert a flight into the "trips" table
            String insertSQL = "INSERT INTO trips (flight_name) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, "Tyaggs"); // Insert "Flight A"
            preparedStatement.executeUpdate();
            System.out.println("Flight 'Tyaggs' inserted successfully!");

            // Step 4: Close resources
            preparedStatement.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
