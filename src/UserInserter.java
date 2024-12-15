import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Random;

public class UserInserter {

    // JDBC URL, username, and password
    private static final String URL = "jdbc:mysql://localhost:3306/TicketBookingSystem";
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "Tyagi#2004"; // Replace with your MySQL password

    public static void main(String[] args) {
        try {
            // Step 1: Connect to the MySQL database
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database!");

            // Step 2: Create the "users" table if it doesn't already exist
            String createTableSQL = """
                        CREATE TABLE IF NOT EXISTS users (
                            user_id INT AUTO_INCREMENT PRIMARY KEY,
                            user_name VARCHAR(100) NOT NULL
                        );
                    """;
            Statement statement = connection.createStatement();
            statement.execute(createTableSQL);
            System.out.println("Table 'users' created or already exists!");

            // Step 3: Prepare SQL for inserting users
            String insertSQL = "INSERT INTO users (user_name) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);

            // Step 4: Generate and insert 100 random users
            for (int i = 0; i < 100; i++) {
                String randomName = generateRandomName();
                preparedStatement.setString(1, randomName);
                preparedStatement.executeUpdate();
                System.out.println("Inserted user: " + randomName);
            }

            System.out.println("100 users inserted successfully!");

            // Step 5: Close resources
            preparedStatement.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to generate random names
    private static String generateRandomName() {
        String[] firstNames = {"John", "Alice", "Bob", "Eve", "Mike", "Lucy", "Tom", "Jane", "Rob", "Amy"};
        String[] lastNames = {"Smith", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor", "Anderson", "Thomas", "Jackson"};

        Random random = new Random();
        String firstName = firstNames[random.nextInt(firstNames.length)];
        String lastName = lastNames[random.nextInt(lastNames.length)];
        return firstName + " " + lastName;
    }
}
