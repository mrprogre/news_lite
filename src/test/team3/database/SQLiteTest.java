package team3.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class SQLiteTest {
    private SQLite sqLite;

    @Before
    public void setUp() {
        sqLite = new SQLite();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void openSQLiteConnection() throws SQLException {
        sqLite.openSQLiteConnection();
        assertTrue(SQLite.connection.isValid(3));
    }

    @Test
    public void closeSQLiteConnection() throws SQLException {
        sqLite.openSQLiteConnection();
        sqLite.closeSQLiteConnection();
        assertFalse(SQLite.connection.isValid(3));
    }
}