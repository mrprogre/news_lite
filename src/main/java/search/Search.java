package search;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import database.SQLite;
import email.EmailSender;
import gui.Gui;
import main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Common;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class Search {
    private static final Logger log = LoggerFactory.getLogger(Search.class);
    public static List<String> excludeFromSearch;
    public static AtomicBoolean isStop;
    public static AtomicBoolean isSearchNow;
    public static AtomicBoolean isSearchFinished;

    public Search() {
        excludeFromSearch = Common.getExcludeWordsFromFile();
        isStop = new AtomicBoolean(false);
        isSearchNow = new AtomicBoolean(false);
        isSearchFinished = new AtomicBoolean(false);
    }

    public static int j = 1;
    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    final LocalDateTime now = LocalDateTime.now();
    public final String today = dtf.format(now);
    final SimpleDateFormat date_format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    final SimpleDateFormat dateFormatHoursFirst = new SimpleDateFormat("dd.MMM HH:mm", Locale.ENGLISH);
    public static final ArrayList<String> dataForEmail = new ArrayList<>();
    int newsCount = 0;
    final Date minDate = Main.MIN_PUB_DATE.getTime();
    int checkDate;
    LocalTime timeStart;
    LocalTime timeEnd;
    Duration searchTime;

    //Main search
    public void mainSearch(String pSearchType) {
        SQLite sqlite = new SQLite();
        if (!isSearchNow.get()) {
            int modelRowCount = Gui.model.getRowCount();
            dataForEmail.clear();
            //выборка актуальных источников перед поиском из БД
            sqlite.selectSources("smi");
            isSearchNow.set(true);
            timeStart = LocalTime.now();
            Search.j = 1;
            if (!Gui.GUI_IN_TRAY.get()) Gui.model.setRowCount(0);
            if (!Gui.WAS_CLICK_IN_TABLE_FOR_ANALYSIS.get()) Gui.modelForAnalysis.setRowCount(0);
            newsCount = 0;
            Gui.labelSum.setText("" + newsCount);
            Search.isStop.set(false);
            Gui.findWord = Gui.topKeyword.getText().toLowerCase();

            if (pSearchType.equals("word")) {
                Gui.searchBtnTop.setVisible(false);
                Gui.stopBtnTop.setVisible(true);
            } else if (pSearchType.equals("words")) {
                Gui.searchBtnBottom.setVisible(false);
                Gui.stopBtnBottom.setVisible(true);
            }

            isSearchFinished = new AtomicBoolean(false);
            Gui.sendEmailBtn.setIcon(Gui.SEND);
            new Thread(Common::fill).start();
            try {
                // начало транзакции
                PreparedStatement st = SQLite.connection.prepareStatement("insert into news_dual(title) values (?)");
                String q_begin = "BEGIN TRANSACTION";
                Statement st_begin = SQLite.connection.createStatement();
                st_begin.execute(q_begin);
                st_begin.close();

                Parser parser = new Parser();
                for (Common.SMI_ID = 0; Common.SMI_ID < Common.SMI_LINK.size(); Common.SMI_ID++) {
                    try {
                        try {
                            if (isStop.get()) return;
                            SyndFeed feed = parser.parseFeed(Common.SMI_LINK.get(Common.SMI_ID));
                            for (Object message : feed.getEntries()) {
                                j++;
                                SyndEntry entry = (SyndEntry) message;
                                SyndContent content = entry.getDescription();
                                String smi_source = Common.SMI_SOURCE.get(Common.SMI_ID);
                                String title = entry.getTitle();
                                assert content != null;
                                String newsDescribe = content.getValue()
                                        .trim()
                                        .replace("<p>", "")
                                        .replace("</p>", "")
                                        .replace("<br />", "");
                                if (newsDescribe.contains("<img")
                                        || newsDescribe.contains("href")
                                        || newsDescribe.contains("<div")
                                        || newsDescribe.contains("&#34")
                                        || newsDescribe.contains("<p lang")
                                        || newsDescribe.contains("&quot")
                                        || newsDescribe.contains("<span")
                                        || newsDescribe.contains("<ol")
                                        || newsDescribe.equals("")
                                ) newsDescribe = title;
                                Date pubDate = entry.getPublishedDate();
                                String dateToEmail = date_format.format(pubDate);
                                String link = entry.getLink();

                                // отсеиваем новости ранее 01.01.2021
                                if (pubDate.after(minDate)) checkDate = 1;
                                else checkDate = 0;

                                if (pSearchType.equals("word")) {
                                    if (title.toLowerCase().contains(Gui.findWord.toLowerCase())
                                            && title.length() > 15 && checkDate == 1
                                            && !title.toLowerCase().contains(excludeFromSearch.get(0))
                                            && !title.toLowerCase().contains(excludeFromSearch.get(1))
                                            && !title.toLowerCase().contains(excludeFromSearch.get(2))
                                    ) {
                                        //отсеиваем новости, которые уже были найдены ранее
                                        if (sqlite.isTitleExists(Common.sha256(title + pubDate))
                                                && SQLite.isConnectionToSQLite) {
                                            continue;
                                        }

                                        //Data for a table
                                        Date currentDate = new Date();
                                        int date_diff = Common.compareDatesOnly(currentDate, pubDate);

                                        // вставка всех новостей в архив (ощутимо замедляет общий поиск)
                                        sqlite.insertAllTitles(title, pubDate.toString());

                                        if (Gui.todayOrNotCbx.getState() && (date_diff != 0)) {
                                            newsCount++;
                                            Gui.labelSum.setText(String.valueOf(newsCount));
                                            dataForEmail.add(newsCount + ") " + title + "\n" + link + "\n" + newsDescribe + "\n" +
                                                    smi_source + " - " + dateToEmail);

                                            Object[] row = new Object[]{
                                                    newsCount,
                                                    smi_source,
                                                    title,
                                                    dateFormatHoursFirst.format(pubDate),
                                                    link
                                            };
                                            Gui.model.addRow(row);

                                            //SQLite
                                            String[] subStr = title.split(" ");
                                            for (String s : subStr) {
                                                if (s.length() > 3) {
                                                    assert st != null;
                                                    st.setString(1, Common.delNoLetter(s).toLowerCase());
                                                    st.executeUpdate();
                                                }
                                            }
                                            sqlite.insertTitleIn256(Common.sha256(title + pubDate));

                                        } else if (!Gui.todayOrNotCbx.getState()) {
                                            newsCount++;
                                            Gui.labelSum.setText(String.valueOf(newsCount));
                                            dataForEmail.add(newsCount + ") " + title + "\n" + link + "\n" + newsDescribe + "\n" +
                                                    smi_source + " - " + dateToEmail);

                                            Object[] row = new Object[]{
                                                    newsCount,
                                                    smi_source,
                                                    title,
                                                    dateFormatHoursFirst.format(pubDate),
                                                    link
                                            };
                                            Gui.model.addRow(row);

                                            // SQLite
                                            String[] subStr = title.split(" ");
                                            for (String s : subStr) {
                                                if (s.length() > 3) {
                                                    assert st != null;
                                                    st.setString(1, Common.delNoLetter(s).toLowerCase());
                                                    st.executeUpdate();
                                                }
                                            }
                                            sqlite.insertTitleIn256(Common.sha256(title + pubDate));
                                        }
                                    }
                                } else if (pSearchType.equals("words")) {
                                    for (String it : Common.getKeywordsFromFile()) {
                                        if (title.toLowerCase().contains(it.toLowerCase()) && title.length() > 15 && checkDate == 1) {

                                            // отсеиваем новости которые были обнаружены ранее
                                            if (sqlite.isTitleExists(Common.sha256(title + pubDate)) && SQLite.isConnectionToSQLite) {
                                                continue;
                                            }

                                            //Data for a table
                                            Date currentDate = new Date();
                                            int date_diff = Common.compareDatesOnly(currentDate, pubDate);

                                            if (Gui.todayOrNotCbx.getState() && (date_diff != 0)) {
                                                newsCount++;
                                                Gui.labelSum.setText(String.valueOf(newsCount));
                                                dataForEmail.add(newsCount + ") " + title + "\n" + link + "\n" + newsDescribe + "\n" +
                                                        smi_source + " - " + dateToEmail);

                                                Object[] row = new Object[]{
                                                        newsCount,
                                                        smi_source,
                                                        title,
                                                        dateFormatHoursFirst.format(pubDate),
                                                        link
                                                };
                                                Gui.model.addRow(row);

                                                //SQLite
                                                String[] subStr = title.split(" ");
                                                for (String s : subStr) {
                                                    if (s.length() > 3) {
                                                        assert st != null;
                                                        st.setString(1, Common.delNoLetter(s).toLowerCase());
                                                        st.executeUpdate();
                                                    }
                                                }
                                                sqlite.insertTitleIn256(Common.sha256(title + pubDate));
                                            } else if (!Gui.todayOrNotCbx.getState()) {
                                                newsCount++;
                                                Gui.labelSum.setText(String.valueOf(newsCount));
                                                dataForEmail.add(newsCount + ") " + title + "\n" + link + "\n" + newsDescribe + "\n" +
                                                        smi_source + " - " + dateToEmail);

                                                Object[] row = new Object[]{
                                                        newsCount,
                                                        smi_source,
                                                        title,
                                                        dateFormatHoursFirst.format(pubDate),
                                                        link
                                                };
                                                Gui.model.addRow(row);

                                                //SQLite
                                                String[] subStr = title.split(" ");
                                                for (String s : subStr) {
                                                    if (s.length() > 3) {
                                                        assert st != null;
                                                        st.setString(1, Common.delNoLetter(s).toLowerCase());
                                                        st.executeUpdate();
                                                    }
                                                }
                                                sqlite.insertTitleIn256(Common.sha256(title + pubDate));
                                            }
                                        }
                                    }
                                }
                                if (isStop.get()) return;
                            }
                            if (!Gui.isOnlyLastNews && SQLite.isConnectionToSQLite) sqlite.deleteFrom256();
                        } catch (Exception no_rss) {
                            String smi = Common.SMI_LINK.get(Common.SMI_ID)
                                    .replaceAll(("https://|http://|www."), "");
                            smi = smi.substring(0, smi.indexOf("/"));
                            Common.console("rss is not available: " + smi);
                            log.warn("rss is not available: " + smi);
                        }
                    } catch (Exception e) {
                        Common.console("status: to many news.. please restart the application!");
                        log.warn("status: restart the application please!");
                        isStop.set(true);
                    }
                }
                st.close();
                //Время поиска
                timeEnd = LocalTime.now();
                searchTime = Duration.between(timeStart, timeEnd);
                if (!Gui.GUI_IN_TRAY.get()) Common.console("status: search completed in " +
                        searchTime.getSeconds() + " s.");
                isSearchNow.set(false);

                Gui.labelSum.setText("total: " + newsCount);

                isSearchFinished.set(true);
                Gui.progressBar.setValue(100);
                Gui.table.setAutoCreateRowSorter(true);
                Gui.tableForAnalysis.setAutoCreateRowSorter(true);

                // итоги в трей
                if (newsCount != 0 && newsCount != modelRowCount && Gui.GUI_IN_TRAY.get())
                    Common.trayMessage("News found: " + newsCount);
                log.info("News found: " + newsCount);

                if (pSearchType.equals("word")) {
                    Gui.searchBtnTop.setVisible(true);
                    Gui.stopBtnTop.setVisible(false);
                } else if (pSearchType.equals("words")) {
                    Gui.searchBtnBottom.setVisible(true);
                    Gui.stopBtnBottom.setVisible(false);
                }

                // коммит транзакции
                String q_commit = "COMMIT";
                Statement st_commit = SQLite.connection.createStatement();
                st_commit.execute(q_commit);
                st_commit.close();

                // удаляем все пустые строки
                String q_del = "delete from news_dual where title = ''";
                Statement st_del = SQLite.connection.createStatement();
                st_del.executeUpdate(q_del);
                st_del.close();

                // Заполняем таблицу анализа
                if (!Gui.WAS_CLICK_IN_TABLE_FOR_ANALYSIS.get()) sqlite.selectSqlite();

                // Автоматическая отправка результатов
                if (Gui.autoSendMessage.getState() && (Gui.model.getRowCount() > 0)) {
                    Gui.sendEmailBtn.doClick();
                }

                sqlite.deleteDuplicates();
                Gui.WAS_CLICK_IN_TABLE_FOR_ANALYSIS.set(false);
                if (pSearchType.equals("word"))
                    Common.console("info: number of news items in the archive = " + sqlite.archiveNewsCount());
                log.info("number of news items in the archive = " + sqlite.archiveNewsCount());
            } catch (Exception e) {
                log.warn(e.getMessage());
                try {
                    String q_begin = "ROLLBACK";
                    Statement st_begin = SQLite.connection.createStatement();
                    st_begin.execute(q_begin);
                    st_begin.close();
                } catch (SQLException i) {
                    log.warn(i.getMessage());
                }
                isStop.set(true);
            }
        }
    }

    //Console search
    public void searchByConsole() {
        SQLite sqlite = new SQLite();
        if (!isSearchNow.get()) {
            dataForEmail.clear();
            sqlite.selectSources("smi");
            isSearchNow.set(true);
            Search.j = 1;
            newsCount = 0;

            try {
                // начало транзакции
                String q_begin = "BEGIN TRANSACTION";
                Statement st_begin = SQLite.connection.createStatement();
                st_begin.executeUpdate(q_begin);

                Parser parser = new Parser();
                for (Common.SMI_ID = 0; Common.SMI_ID < Common.SMI_LINK.size(); Common.SMI_ID++) {
                    try {
                        try {
                            if (isStop.get()) return;
                            SyndFeed feed = parser.parseFeed(Common.SMI_LINK.get(Common.SMI_ID));
                            for (Object message : feed.getEntries()) {
                                j++;
                                SyndEntry entry = (SyndEntry) message;
                                SyndContent content = entry.getDescription();
                                String smi_source = Common.SMI_SOURCE.get(Common.SMI_ID);
                                String title = entry.getTitle();
                                assert content != null;
                                String newsDescribe = content.getValue()
                                        .trim()
                                        .replace("<p>", "")
                                        .replace("</p>", "")
                                        .replace("<br />", "");
                                if (newsDescribe.contains("<img")
                                        || newsDescribe.contains("href")
                                        || newsDescribe.contains("<div")
                                        || newsDescribe.contains("&#34")
                                        || newsDescribe.contains("<p lang")
                                        || newsDescribe.contains("&quot")
                                        || newsDescribe.contains("<span")
                                        || newsDescribe.contains("<ol")
                                        || newsDescribe.equals("")
                                ) newsDescribe = title;
                                Date pubDate = entry.getPublishedDate();
                                String dateToEmail = date_format.format(pubDate);
                                String link = entry.getLink();

                                // отсеиваем новости ранее 01.01.2021
                                if (pubDate.after(minDate)) checkDate = 1;
                                else checkDate = 0;

                                for (String it : Main.keywordsFromConsole) {
                                    if (it.equals(Main.keywordsFromConsole[0]) || it.equals(Main.keywordsFromConsole[1]))
                                        continue;

                                    if (title.toLowerCase().contains(it.toLowerCase()) && title.length() > 15 && checkDate == 1) {
                                        // отсеиваем новости которые были обнаружены ранее
                                        if (sqlite.isTitleExists(Common.sha256(title + pubDate)) && SQLite.isConnectionToSQLite) {
                                            continue;
                                        }

                                        //Data for a table
                                        Date currentDate = new Date();
                                        int date_diff = Common.compareDatesOnly(currentDate, pubDate);

                                        if (date_diff != 0) { // если новость between Main.minutesIntervalForConsoleSearch and currentDate
                                            newsCount++;
                                            dataForEmail.add(newsCount + ") " + title + "\n" + link + "\n" + newsDescribe + "\n" +
                                                    smi_source + " - " + dateToEmail);
                                            /**/
                                            System.out.println(newsCount + ") " + title);
                                            /**/
                                            sqlite.insertTitleIn256(Common.sha256(title + pubDate));
                                        }
                                    }
                                }
                            }
                            // удалять новости, чтобы были вообще все, даже те, которые уже были обнаружены
                            //sqlite.deleteFrom256();
                        } catch (Exception ignored) {
                        }
                    } catch (Exception ignored) {
                    }
                }
                isSearchNow.set(false);

                // коммит транзакции
                String q_commit = "COMMIT";
                Statement st_commit = SQLite.connection.createStatement();
                st_commit.execute(q_commit);

                // удаляем все пустые строки
                String q_del = "delete from news_dual where title = ''";
                Statement st_del = SQLite.connection.createStatement();
                st_del.executeUpdate(q_del);

                // Автоматическая отправка результатов
                if (dataForEmail.size() > 0) {
                    Common.IS_SENDING.set(false);
                    EmailSender email = new EmailSender();
                    email.sendMessage();
                }
                sqlite.deleteDuplicates();
                Gui.WAS_CLICK_IN_TABLE_FOR_ANALYSIS.set(false);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    String q_begin = "ROLLBACK";
                    Statement st_begin = SQLite.connection.createStatement();
                    st_begin.execute(q_begin);
                } catch (SQLException sql) {
                    sql.printStackTrace();
                }
            }
        }
    }
}
