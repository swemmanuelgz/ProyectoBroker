package database;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    private Connection connection;

    @BeforeEach
    public void setUp() {
        connection = new ConnectMysql().conectar();
        assertNotNull(connection, "La conexion no tiene que ser nula");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testInsertAndQuery() throws SQLException {
        String insertQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
            ps.setString(1, "testuser");
            ps.setString(2, "testpassword");
            int rowsInserted = ps.executeUpdate();
            assertEquals(1, rowsInserted, "One row should be inserted");
        }

        String selectQuery = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(selectQuery)) {
            ps.setString(1, "testuser");
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "Resultset tiene que tener al menos una fila");
                assertEquals("testuser", rs.getString("username"), "Username should match");
                assertEquals("testpassword", rs.getString("password"), "Password should match");
            }
        }
    }
}