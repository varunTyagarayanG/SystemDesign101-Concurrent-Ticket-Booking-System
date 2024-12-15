import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SeatsCreator {
    private static final String URL = "jdbc:mysql://localhost:3306/TicketBookingSystem"; // Database URL
    private static final String USER = "root"; // MySQL username
    private static final String PASSWORD = "YourPASS"; // MySQL password

    /*
    CREATE TABLE IF NOT EXISTS seats (
        seat_id INT AUTO_INCREMENT PRIMARY KEY,
        seat_name VARCHAR(10) NOT NULL,
        trip_id INT NOT NULL,
        user_id INT DEFAULT NULL,
        FOREIGN KEY (trip_id) REFERENCES trips(flight_id) ON DELETE CASCADE,
        FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
    );
*/
    public static void main(String[] args) {
        try {
            // Step 1: Connect to the MySQL database
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database!");

            // Step 2: Prepare the SQL query to insert seats into the "seats" table
            String insertSQL = "INSERT INTO seats (seat_name, trip_id) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);

            // Step 3: Insert 100 seats for trip_id = 1
            for (int row = 1; row <= 10; row++) { // 10 rows
                for (char col = 'A'; col <= 'J'; col++) { // 10 seats per row (A-J)
                    String seatName = row + String.valueOf(col); // Seat name like "1A", "1B", ..., "10J"
                    preparedStatement.setString(1, seatName); // Set the seat name
                    preparedStatement.setInt(2, 1); // Set trip_id = 1 (assuming one flight)
                    preparedStatement.executeUpdate(); // Execute the insert statement
                    System.out.println("Inserted seat: " + seatName);
                }
            }

            System.out.println("100 seats inserted successfully!");

            // Step 4: Close the prepared statement and the connection
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            // Handle any exceptions (like database connection failure)
            e.printStackTrace();
        }
    }
}
