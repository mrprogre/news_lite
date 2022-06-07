package team3.search;

import team3.main.Main;
import org.junit.jupiter.api.Test;
import team3.search.Search;
import team3.utils.InternetAvailabilityChecker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SearchTest {
    //SearchObject Test
    @Test
    public void objectCreateTest() throws IOException {
        try {
            Search search = new Search();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //emailSenderTest
    @Test
    public void emailSenderTest() throws IOException {
        try {
            String subject = ("News (" + new Search().today + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // clearButtonTopTest.getX,getY,getIMG error


    //searchByConsole test
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

