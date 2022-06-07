import main.Main;
import org.junit.jupiter.api.Test;
import search.Search;
import utils.InternetAvailabilityChecker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SearchTest {
    @Test
    public void shouldToCheckInternetAvailableTest() throws IOException {
        try {
            String subject = ("News (" + new Search().today + ")");
            } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void shouldOpenSQLiteConnectionTest() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + Main.DIRECTORY_PATH + "news.db");
            assertFalse(connection.isClosed());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}

