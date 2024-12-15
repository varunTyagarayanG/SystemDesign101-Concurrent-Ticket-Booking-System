import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TicketBookingSystem {

    // Database connection details
    static final String URL = "jdbc:mysql://localhost:3306/TicketBookingSystem";
    static final String USER = "root";
    static final String PASSWORD = "Tyagi#2004"; // Update with your MySQL password

    // Book a seat for a user
    public static boolean bookSeat(int userId) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            connection.setAutoCommit(false); // Start transaction

            // Skip locked rows using the `FOR UPDATE SKIP LOCKED` query
            String query = "SELECT seat_id, seat_name FROM seats WHERE user_id IS NULL LIMIT 1 FOR UPDATE SKIP LOCKED";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                ResultSet resultSet = stmt.executeQuery();
                if (resultSet.next()) {
                    int seatId = resultSet.getInt("seat_id");
                    String seatName = resultSet.getString("seat_name");

                    // Book the seat by updating the user_id
                    String updateSQL = "UPDATE seats SET user_id = ? WHERE seat_id = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateSQL)) {
                        updateStmt.setInt(1, userId);
                        updateStmt.setInt(2, seatId);
                        int rowsUpdated = updateStmt.executeUpdate();

                        if (rowsUpdated > 0) {
                            connection.commit(); // Commit the transaction
                            System.out.println("User " + userId + " was assigned the seat " + seatName); // Print assignment
                            return true; // Seat booked successfully
                        } else {
                            connection.rollback(); // Rollback if update failed
                            System.out.println("Seat " + seatName + " is locked, skipping to the next available seat for User " + userId);
                            return false; // Failed to book seat
                        }
                    }
                } else {
                    connection.rollback();
                    return false; // No available seats
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // No available seats found or error
    }

    // Print the final seat assignments in a grid format
    public static void printSeatGrid() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT seat_name, user_id FROM seats ORDER BY seat_id";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                ResultSet resultSet = stmt.executeQuery();

                int bookedCount = 0;
                String[][] grid = new String[10][10]; // 10x10 grid for seats
                String[][] bookedBy = new String[10][10]; // To store user names for booked seats

                while (resultSet.next()) {
                    String seatName = resultSet.getString("seat_name");
                    int userId = resultSet.getInt("user_id");

                    // If user_id is not null, it means the seat is booked
                    String status = (userId == 0) ? "-" : "X";

                    // Extract row (numeric part) and column (letter part) from seat_name
                    int row = Integer.parseInt(seatName.replaceAll("[^0-9]", "")) - 1; // Remove non-numeric part and convert
                    int col = seatName.replaceAll("[^A-Za-z]", "").charAt(0) - 'A'; // Get the alphabet part (A=0, B=1, etc.)

                    grid[row][col] = status; // Mark the seat as booked (X) or available (-)
                    if (userId != 0) {
                        bookedCount++; // Count how many seats are booked
                        // Store the user's seat name in the grid
                        bookedBy[row][col] = "User " + userId + " (" + seatName + ")";
                    } else {
                        bookedBy[row][col] = null; // If available, no user booked it
                    }
                }

                // Print the grid with booking status and users
                System.out.println("\nSeat Availability Grid (X = Booked, - = Available):");
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        if (grid[i][j] == null) {
                            grid[i][j] = "-"; // Empty spots
                        }
                        System.out.print(grid[i][j] + " ");
                    }
                    System.out.println();
                }

                // Print users who booked each seat at the end
                System.out.println("\nSeats Booked by Users:");
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        if (bookedBy[i][j] != null) {
                            System.out.println(bookedBy[i][j]);
                        }
                    }
                }

                System.out.println("\nTotal Booked Seats: " + bookedCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Reset all seats (user_id) to NULL after booking is complete
    public static void resetSeats() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String resetSQL = "UPDATE seats SET user_id = NULL";
            try (PreparedStatement stmt = connection.prepareStatement(resetSQL)) {
                int rowsUpdated = stmt.executeUpdate();
                System.out.println("\nAll seats have been reset to available (user_id = NULL). Total rows updated: " + rowsUpdated);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();  // Start measuring time

        // ExecutorService to handle concurrency for 100 users (threads)
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        // Submit tasks for 100 users to book seats concurrently
        for (int i = 1; i <= 100; i++) {
            final int userId = i;
            executorService.submit(() -> {
                bookSeat(userId); // Booking a seat for each user
            });
        }

        executorService.shutdown(); // Shutdown the executor

        // Wait until all tasks are finished
        while (!executorService.isTerminated()) {
            // Wait for all threads to finish
        }

        // Print the final seat assignments in grid format after all bookings
        printSeatGrid();

        // Reset the user_id field for all seats
        resetSeats();

        long endTime = System.currentTimeMillis();  // End measuring time
        long executionTime = endTime - startTime; // Calculate the execution time
        System.out.println("\nExecution Time: " + executionTime + " milliseconds");
    }
}
