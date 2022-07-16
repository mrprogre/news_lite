package main;

import com.formdev.flatlaf.intellijthemes.FlatHiberbeeDarkIJTheme;
import database.SQLite;
import gui.Gui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import search.ConsoleSearch;
import utils.Common;
import utils.InternetAvailabilityChecker;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    public static final String DIRECTORY_PATH = System.getProperty("user.home") + File.separator + "News" + File.separator;
    public static final Calendar MIN_PUB_DATE = Calendar.getInstance();
    public static final int [] GUI_FONT = new int[3];
    public static final int [] GUI_BACKGROUND = new int[3];
    // Console search
    public static final AtomicBoolean IS_CONSOLE_SEARCH = new AtomicBoolean(false);
    public static String emailToFromConsole;
    public static int minutesIntervalForConsoleSearch;
    public static String[] keywordsFromConsole;

    // создание директорий и файлов
    static {
        // Минимальная дата публикации новости 01.01.2021
        MIN_PUB_DATE.set(Calendar.YEAR, 2022);
        MIN_PUB_DATE.set(Calendar.DAY_OF_YEAR, 1);

        // main directory create
        File mainDirectory = new File(DIRECTORY_PATH);
        if (!mainDirectory.exists()) mainDirectory.mkdirs();

        // log file create
//        File logIsExists = new File(DIRECTORY_PATH + "app.log"); // TODO logback.xml: property "LOG" = DIRECTORY_PATH + "app.log"
//        if (!logIsExists.exists()) {
//            try {
//                logIsExists.createNewFile();
//            } catch (IOException e) {
//                log.error("log create failed");
//            }
//        }

        // создание файлов программы
        File sqliteExeIsExists = new File(DIRECTORY_PATH + "sqlite3.exe");
        if (!sqliteExeIsExists.exists()) {
            Common.copyFiles(Main.class.getResource("/sqlite3.exe"), DIRECTORY_PATH + "sqlite3.exe");
        }
        File sqliteDllIsExists = new File(DIRECTORY_PATH + "sqlite3.dll");
        if (!sqliteDllIsExists.exists()) {
            Common.copyFiles(Main.class.getResource("/sqlite3.dll"), DIRECTORY_PATH + "sqlite3.dll");
        }
        File sqliteDefIsExists = new File(DIRECTORY_PATH + "sqlite3.def");
        if (!sqliteDefIsExists.exists()) {
            Common.copyFiles(Main.class.getResource("/sqlite3.def"), DIRECTORY_PATH + "sqlite3.def");
        }
        File dbIsExists = new File(DIRECTORY_PATH + "news.db");
        if (!dbIsExists.exists()) {
            Common.copyFiles(Main.class.getResource("/news.db"), DIRECTORY_PATH + "news.db");
        }
        File configIsExists = new File(DIRECTORY_PATH + "config.txt");
        if (!configIsExists.exists()) {
            Common.copyFiles(Main.class.getResource("/config.txt"), DIRECTORY_PATH + "config.txt");
        }
    }

    /**
     * Main arguments for console search:
     * args1 = email
     * args2 = interval in minutes
     * args3 = keyword1, keyword2 ... argsN = search keywords
     */
    public static void main(String[] args) throws IOException {
        keywordsFromConsole = new String[args.length];
        if (args.length == 0) {
            log.info("Application started");

            // FlatLaf theme
            // https://github.com/JFormDesigner/FlatLaf
            // https://docs.oracle.com/javase/tutorial/uiswing/lookandfeel/_nimbusDefaults.html
            UIManager.put("Component.arc", 10);
            UIManager.put("ProgressBar.arc", 6);
            UIManager.put("Button.arc", 8);
            Common.getColorsSettingsFromFile();
            UIManager.put("Table.background", new Color(GUI_BACKGROUND[0], GUI_BACKGROUND[1], GUI_BACKGROUND[2]));
            UIManager.put("Table.alternateRowColor", new Color(59, 59, 59));
            UIManager.put("Table.foreground", new Color(GUI_FONT[0], GUI_FONT[1], GUI_FONT[2]));
            UIManager.put("TextField.background", Color.GRAY);
            UIManager.put("TextField.foreground", Color.BLACK);
            FlatHiberbeeDarkIJTheme.setup();

            new Gui();
            // load config.txt
            Common.getSettingsFromFile();
            Gui.isOnlyLastNews = Gui.onlyNewNews.getState();
        } else {
            // Console search
            IS_CONSOLE_SEARCH.set(true);
            emailToFromConsole = args[0];
            minutesIntervalForConsoleSearch = Integer.parseInt(args[1]);
            System.arraycopy(args, 0, keywordsFromConsole, 0, args.length);
            System.out.println(Arrays.toString(keywordsFromConsole)); //***
            ConsoleSearch consoleSearch = new ConsoleSearch();
            consoleSearch.searchByConsole();
        }

        // check internet
        if (!InternetAvailabilityChecker.isInternetAvailable()) {
            Common.console("status: no internet connection");
        }
    }
}
