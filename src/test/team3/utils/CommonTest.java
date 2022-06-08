package team3.utils;

import org.junit.Test;
import org.junit.rules.ErrorCollector;
import team3.main.Main;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.Assert.*;

public class CommonTest {

    /**
     * Purpose: to cover case branch in getKeywordsFromFile()
     * input: p_word("hihi") and p_types(for enter each case)
     * Expected: branch coverage
     * @throws NullPointerException
     */
    @Test(expected = NullPointerException.class)
    public void Config_ToWriteTest() throws IOException {
        String p_word = "hihi", p_word2 = "todayOrNotChbx", p_word3 = "filterNewsChbx", p_word4 = "autoSendChbx";
        String p_type1 = "keyword", p_type2 = "fontColorRed", p_type3 = "fontColorGreen", p_type4 = "fontColorBlue",
                p_type5 = "backgroundColorRed", p_type6 = "backgroundColorGreen", p_type7 = "backgroundColorBlue",
                p_type8 = "email", p_type9 = "interval", p_type10 = "checkbox";
        Common.writeToConfig(p_word, p_type1);
        Common.writeToConfig(p_word, p_type2);
        Common.writeToConfig(p_word, p_type3);
        Common.writeToConfig(p_word, p_type4);
        Common.writeToConfig(p_word, p_type5);
        Common.writeToConfig(p_word, p_type6);
        Common.writeToConfig(p_word, p_type7);
        Common.writeToConfig(p_word, p_type8);
        Common.writeToConfig(p_word, p_type9);
        Common.writeToConfig(p_word, p_type10);
        Common.writeToConfig(p_word2, p_type10);
        Common.writeToConfig(p_word3, p_type10);
        Common.writeToConfig(p_word4, p_type10);
        Common.writeToConfig("s", "t");
    }

    /**
     * Purpose: to see coverage
     * input: showDialog("smi", "log", "excl")
     * Expected:
     *  high line coverage
     */
    @Test
    public void showDialog() {
        String p_file1 = "smi", p_file2 = "log", p_file3 = "excl";
        Common.showDialog(p_file1);
        Common.showDialog(p_file2);
        Common.showDialog(p_file3);
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

    /**
     * Purpose: to see coverage
     * input: sha256("base")
     * Expected:
     *  high line coverage
     */
    @Test
    public void sha256() {
        String base = "base";
        Common.sha256(base);
    }
}