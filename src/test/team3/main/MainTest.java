package team3.main;

import org.junit.Test;
import team3.utils.Common;

import java.io.File;

import static org.junit.Assert.*;

public class MainTest {
    /**
     * Purpose: File directory existing test
     * input: mainDirectory.exists()
     * Expected:
     *  no error with testcase
     */
    @Test
    public void initialFileSettingTest() {
        File mainDirectory = new File(Main.DIRECTORY_PATH);
        assertTrue(mainDirectory.exists());

        File sqliteExeIsExists = new File(Main.DIRECTORY_PATH + "sqlite3.exe");
        assertTrue(sqliteExeIsExists.exists());

        File sqliteDllIsExists = new File(Main.DIRECTORY_PATH + "sqlite3.dll");
        assertTrue(sqliteDllIsExists.exists());

        File sqliteDefIsExists = new File(Main.DIRECTORY_PATH + "sqlite3.def");
        assertTrue(sqliteDefIsExists.exists());

        File dbIsExists = new File(Main.DIRECTORY_PATH + "news.db");
        assertTrue(dbIsExists.exists());

        File configIsExists = new File(Main.DIRECTORY_PATH + "config.txt");
        assertTrue(configIsExists.exists());
    }


}