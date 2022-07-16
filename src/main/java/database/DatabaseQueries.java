package database;

import gui.Gui;
import lombok.extern.slf4j.Slf4j;
import model.ExcludeWord;
import model.RssInfoFromUi;
import model.RssList;
import model.VNewsDual;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import utils.Common;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class DatabaseQueries {
    ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
    private final JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
    private final Utilities dbUtil = new Utilities();
    private static final int WORD_FREQ_MATCHES = 2;

    // общая команда для транзакций TODO
    public void transactionCommand(String command) throws SQLException {
        SQLite sqlite = new SQLite();
        sqlite.openConnection();
        PreparedStatement statement = SQLite.connection.prepareStatement(command);
        statement.execute();
        statement.close();
    }

    // вставка кода по заголовку для отсеивания ранее обнаруженных новостей TODO
    public void insertTitleIn256(String pTitle) {
//        try {
//            String query256 = "INSERT INTO titles256(title) VALUES (?)";
//            PreparedStatement st256 = connection.prepareStatement(query256);
//            st256.setString(1, pTitle);
//            st256.executeUpdate();
//            st256.close();
//        } catch (SQLException t) {
//            t.printStackTrace();
//        }
        String query = "INSERT INTO titles256(title) VALUES (?)";
        jdbcTemplate.update(query, pTitle);
    }

    // сохранение всех заголовков  TODO
    public void insertAllTitles(String pTitle, String pDate) {
//        try {
//            String query = "INSERT INTO ALL_NEWS(TITLE, NEWS_DATE) VALUES (?, ?)";
//            PreparedStatement st = connection.prepareStatement(query);
//            st.setString(1, pTitle);
//            st.setString(2, pDate);
//            st.executeUpdate();
//            st.close();
//        } catch (SQLException ignored) {
//        }
        String query = "INSERT INTO ALL_NEWS(TITLE, NEWS_DATE) VALUES (?, ?)";
        jdbcTemplate.update(query, pTitle, pDate);
    }

    // отсеивание заголовков  TODO
    public boolean isTitleExists(String pString256) {
        int isExists = 0;

        String query = "SELECT MAX(1) FROM TITLES256 WHERE EXISTS (SELECT TITLE FROM TITLES256 T WHERE T.TITLE = ?)";
        Integer exists = jdbcTemplate.queryForObject(query, Integer.class, pString256);

        if (exists != null) {
            isExists = 1;
        }
        return isExists == 1;
    }

    // вставка разбитых заголовков по словам во временную таблицу
    public void insertIntoNewsDual(String title) throws SQLException {
        String query = "INSERT INTO NEWS_DUAL(TITLE) VALUES (?)";
        PreparedStatement st = SQLite.connection.prepareStatement(query);

        String[] subStr = title.split(" ");
        for (String s : subStr) {
            if (s.length() > 3) {
                assert st != null;
                st.setString(1, Common.delNoLetter(s).toLowerCase());
                st.executeUpdate();
            }
        }
        st.close();
    }

    // Получение id для нового источника
    public int getNextRssListId() {
        String query = "SELECT MAX(ID) AS ID FROM RSS_LIST";
        Integer integer = jdbcTemplate.queryForObject(query, Integer.class);

        if (integer != null) return ++integer;
        else return 0;
    }

    // Заполняем таблицу анализа
    public void selectSqlite() {
        String query = "SELECT SUM, TITLE FROM V_NEWS_DUAL WHERE SUM > ? " +
                "AND TITLE NOT IN (SELECT WORD FROM ALL_TITLES_TO_EXCLUDE) ORDER BY SUM DESC";
        List<VNewsDual> newsTitles = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(VNewsDual.class), WORD_FREQ_MATCHES);

        for (VNewsDual title : newsTitles) {
            Object[] row = new Object[]{title.getTitle(), title.getSum()};
            Gui.modelForAnalysis.addRow(row);
        }
        deleteTitles();
    }

    // запись данных по актуальным источникам из базы в массивы для поиска
    public void selectSources(String pDialog) {
        switch (pDialog) {
            case "smi":
                //sources
                Common.SMI_SOURCE.clear();
                Common.SMI_LINK.clear();

                String query = "SELECT ID, SOURCE, LINK, IS_ACTIVE FROM RSS_LIST WHERE IS_ACTIVE = 1  ORDER BY ID";
                List<RssList> rssItems = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(RssList.class));

                for (RssList rssItem : rssItems) {
                    Common.SMI_SOURCE.add(rssItem.getSource());
                    Common.SMI_LINK.add(rssItem.getLink());
                }
                break;
            case "excl":
                //excluded words
                Common.EXCLUDED_WORDS.clear();
                String excludedQuery = "SELECT WORD FROM EXCLUDE";
                List<ExcludeWord> excludedWords = jdbcTemplate.query(excludedQuery, new BeanPropertyRowMapper<>(ExcludeWord.class));

                for (ExcludeWord excludedWord : excludedWords) {
                    Common.EXCLUDED_WORDS.add(excludedWord.getWord());
                }
                break;
            case "active_smi":
                Common.SMI_SOURCE.clear();
                Common.SMI_LINK.clear();
                Common.SMI_IS_ACTIVE.clear();

                String queryRss = "SELECT ID, SOURCE, LINK, IS_ACTIVE FROM RSS_LIST ORDER BY ID";
                List<RssList> rssListItems = jdbcTemplate.query(queryRss, new BeanPropertyRowMapper<>(RssList.class));

                for (RssList rssItem : rssListItems) {
                    Common.SMI_SOURCE.add(rssItem.getSource());
                    Common.SMI_LINK.add(rssItem.getLink());
                    Common.SMI_IS_ACTIVE.add(rssItem.getIsActive());
                }
                break;
        }
    }

    // вставка нового источника
    public void insertNewSource() {
        // Диалоговое окно добавления источника новостей в базу данных
        RssInfoFromUi rssInfoFromUI = dbUtil.getRssInfoFromUi();

        if (rssInfoFromUI.getResult() == JOptionPane.YES_OPTION) {
            String query = "INSERT INTO RSS_LIST(ID, SOURCE, LINK, IS_ACTIVE) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(query, getNextRssListId(), rssInfoFromUI.getSourceName().getText(),
                    rssInfoFromUI.getRssLink().getText(), 1);

            Common.console("status: source added");
            log.info("New source added: " + rssInfoFromUI.getSourceName().getText());
        } else {
            Common.console("status: adding source canceled");
        }
    }

    // вставка нового слова для исключения из анализа частоты употребления слов
    public void insertNewExcludedWord(String pWord) {
        String query = "INSERT INTO exclude(word) VALUES (?)";
        jdbcTemplate.update(query, pWord);
        Common.console("status: word \"" + pWord + "\" excluded from analysis");
    }

    // новостей в архиве всего
    public Integer archiveNewsCount() {
        String query = "SELECT COUNT(*) FROM ALL_NEWS";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    // обновление статуса чекбокса is_active для ресурсов SELECT id, source, link FROM rss_list where is_active = 1  ORDER BY id
    public void updateIsActiveStatus(boolean pBoolean, String pSource) {
        String query = "UPDATE RSS_LIST SET IS_ACTIVE = ? WHERE SOURCE = ?";
        jdbcTemplate.update(query, pBoolean, pSource);
    }

    // удаление источника
    public void deleteSource(String source) {
        String query = "DELETE FROM rss_list WHERE source = ?";
        jdbcTemplate.update(query, source);
    }

    // удаление слова исключенного из поиска
    public void deleteExcluded(String source) {
        String query = "DELETE FROM EXCLUDE WHERE WORD = ?";
        jdbcTemplate.update(query, source);
    }

    // удаление дубликатов новостей
    public void deleteDuplicates() {
        String query = "DELETE FROM ALL_NEWS WHERE ROWID NOT IN (SELECT MIN(ROWID) " +
                "FROM ALL_NEWS GROUP BY TITLE, NEWS_DATE)";
        jdbcTemplate.update(query);
    }

    // удаляем все пустые строки
    public void deleteEmptyRows() {
        String query = "DELETE FROM NEWS_DUAL WHERE TITLE = ''";
        jdbcTemplate.update(query);
    }

    // Delete from news_dual
    public void deleteTitles() {
        String query = "DELETE FROM NEWS_DUAL";
        jdbcTemplate.update(query);
    }

    // Delete from titles256
    public void deleteFrom256() {
        String query = "DELETE FROM TITLES256";
        jdbcTemplate.update(query);
    }
}
