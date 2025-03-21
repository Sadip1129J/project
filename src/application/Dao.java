package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Dao {

    final String DRIVER = "com.mysql.cj.jdbc.Driver";
    final String HOST = "localhost";
     final int PORT = 3306;
    final String DBNAME = "traffic_fine";
    final String USER = "root";
    final String PASS = "Sadip123@";

    final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DBNAME;

    public boolean connect() {
        Connection conn = null;
        try {
//            Class.forName(DRIVER); // Loading driver class
//            conn = DriverManager.getConnection(URL, USER, PASS);
//            System.out.println("Connection to the database was successful.");
//            return true; // Connection successful
//            
        	Class.forName("com.mysql.cj.jdbc.Driver");
        	conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/traffic_fine","root","Sadip123@");
        	return  true;
        } catch (Exception ex) {
            System.out.println("Error connecting to the database: " + ex.getMessage());
            return false; // Connection failed
        } finally {
            // Close the connection if it was established
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Error closing the connection: " + e.getMessage());
                }
            }
        }
    }

    public boolean insertAmenity(String name, String description, String type, String location) {
        String sql = "INSERT INTO test (name, description, type, location) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setString(3, type);
            pstmt.setString(4, location);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if the insert was successful
        } catch (SQLException ex) {
            System.out.println("Error inserting amenity: " + ex.getMessage());
            return false;
        }
    }
 
}