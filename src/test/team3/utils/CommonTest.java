package team3.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import team3.main.Main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

public class CommonTest {
    @Rule
    public final ErrorCollector collector = new ErrorCollector();

    @Test
    public void writeToConfig() {
    }

//    @Test(expected = IOException.class)
//    public void getKeywordsFromFile() throws IOException {
//        //List<String> lines = Files.readAllLines(Paths.get(Main.SETTINGS_PATH));
//        assertNotNull(Files.readAllLines(Paths.get(Main.SETTINGS_PATH)));
//        //collector.addError(new Throwable("getKeywords Error"));
//    }

//    @Test(expected = IOException.class)
//    public void getExcludeWordsFromFile() throws IOException {
//        List<String> lines = Files.readAllLines(Paths.get(Main.SETTINGS_PATH));
//        collector.addError(new Throwable("getExcludeWords Error"));
//    }
//
//    @Test(expected = IOException.class)
//    public void getSettingsFromFile() throws IOException {
//        List<String> lines = Files.readAllLines(Paths.get(Main.SETTINGS_PATH));
//        collector.addError(new Throwable("getSettings Error"));
//    }
//
//    @Test(expected = IOException.class)
//    public void getColorsSettingsFromFile() throws IOException {
//        List<String> lines = Files.readAllLines(Paths.get(Main.SETTINGS_PATH));
//        collector.addError(new Throwable("getColorSettings Error"));
//    }

    @Test
    public void saveState() {
    }

    @Test
    public void getSmtp() {
    }

    @Test
    public void getEmailSettingsFromFile() {
    }

    @Test
    public void countLines() {
    }

    @Test
    public void delSettings() {
    }

    @Test
    public void console() {
    }

    @Test
    public void fill() {
    }

    @Test
    public void getInterval() {
    }

    @Test
    public void compareDatesOnly() {
    }

    @Test
    public void showDialog() {
    }

    @Test
    public void copyFiles() {
    }

    /**
     * Purpose: to test if the line that contains number can convert to that with only string.
     * input: delNoLetter(abcd7bef)
     * Expected:
     *  abcd7ef -> abcdef
     */
    @Test
    public void delNoLetterTest() {
        String s = "abcd7ef";
        String sb = Common.delNoLetter(s);
        System.out.println("abcd7ef to " + sb);
        assertEquals("abcdef", sb);
    }

    @Test
    public void sha256() {
    }

    @Test
    public void trayMessage() {
    }
}