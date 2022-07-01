package database;

import gui.Gui;
import main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Common;

import javax.swing.*;
import java.sql.*;

public class SQLite {
    private static final Logger log = LoggerFactory.getLogger(SQLite.class);
    public static Connection connection;
    public static boolean isConnectionToSQLite;
    private static final int WORD_FREQ_MATCHES = 2;

    // Открытие соединения с базой данных
    public void openSQLiteConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + Main.DIRECTORY_PATH + "news.db");
            isConnectionToSQLite = true;
            log.info("Connected to SQLite");
            Thread.sleep(1000L);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getMessage());
        }
    }

    // Заполняем таблицу анализа
    public void selectSqlite() {
        try {
            Statement st = connection.createStatement();
            String query = "select SUM, TITLE from v_news_dual where sum > " +
                    WORD_FREQ_MATCHES +
                    " and title not in (select word from all_titles_to_exclude)" +
                    " order by SUM desc";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String word = rs.getString("TITLE");
                int sum = rs.getInt("SUM");
                Object[] row2 = new Object[]{word, sum};
                Gui.modelForAnalysis.addRow(row2);
            }
            deleteTitles();
            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Delete from news_dual
    public void deleteTitles() {
        try {
            Statement st = connection.createStatement();
            String query = "DELETE FROM NEWS_DUAL";
            st.executeUpdate(query);
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Delete from titles256
    public void deleteFrom256() {
        try {
            Statement st = connection.createStatement();
            String query = "DELETE FROM TITLES256";
            st.executeUpdate(query);
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // запись данных по актуальным источникам из базы в массивы для поиска
    public void selectSources(String pDialog) {
        if (isConnectionToSQLite) {
            switch (pDialog) {
                case "smi":
                    //sources
                    Common.SMI_SOURCE.clear();
                    Common.SMI_LINK.clear();
                    try {
                        Statement st = connection.createStatement();
                        String query = "SELECT id, source, link FROM rss_list where is_active = 1  ORDER BY id";
                        ResultSet rs = st.executeQuery(query);
                        while (rs.next()) {
                            //int id = rs.getInt("id");
                            String source = rs.getString("source");
                            String link = rs.getString("link");
                            Common.SMI_SOURCE.add(source);
                            Common.SMI_LINK.add(link);
                        }
                        rs.close();
                        st.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "excl":
                    //excluded words
                    Common.EXCLUDED_WORDS.clear();
                    try {
                        Statement st = connection.createStatement();
                        String query = "SELECT word FROM exclude";
                        ResultSet rs = st.executeQuery(query);
                        while (rs.next()) {
                            String word = rs.getString("word");
                            Common.EXCLUDED_WORDS.add(word);
                        }
                        rs.close();
                        st.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "active_smi":
                    Common.SMI_SOURCE.clear();
                    Common.SMI_LINK.clear();
                    Common.SMI_IS_ACTIVE.clear();
                    try {
                        Statement st = connection.createStatement();
                        String query = "SELECT id, source, link, is_active FROM rss_list ORDER BY id";
                        ResultSet rs = st.executeQuery(query);
                        while (rs.next()) {
                            //int id = rs.getInt("id");
                            String source = rs.getString("source");
                            String link = rs.getString("link");
                            boolean isActive = rs.getBoolean("is_active");

                            Common.SMI_SOURCE.add(source);
                            Common.SMI_LINK.add(link);
                            Common.SMI_IS_ACTIVE.add(isActive);
                        }
                        rs.close();
                        st.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }

        }
    }

    // вставка нового источника
    public void insertNewSource() {
        if (isConnectionToSQLite) {
            int max_id_in_source = 0;
            int new_id;
            try {
                String max_id_query = "SELECT max(id) as id FROM rss_list";
                Statement max_id_st = connection.createStatement();
                ResultSet rs = max_id_st.executeQuery(max_id_query);
                while (rs.next()) {
                    max_id_in_source = rs.getInt("ID");
                }
                rs.close();
                max_id_st.close();
                new_id = max_id_in_source + 1;

                // Диалоговое окно добавления источника новостей в базу данных
                JTextField source_name = new JTextField();
                JTextField rss_link = new JTextField();
                Object[] new_source = {
                        "Source:", source_name,
                        "Link to rss:", rss_link
                };

                int result = JOptionPane.showConfirmDialog(Gui.scrollPane, new_source, "New source", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.YES_OPTION) {

                    //запись в БД
                    String query = "INSERT INTO RSS_LIST(ID, SOURCE, LINK, IS_ACTIVE) VALUES (?, ?, ?, ?)";
                    PreparedStatement st = connection.prepareStatement(query);
                    st.setInt(1, new_id);
                    st.setString(2, source_name.getText());
                    st.setString(3, rss_link.getText());
                    st.setInt(4, 1);

                    //Statement st = connection.createStatement();
                    st.executeUpdate();
                    st.close();

                    Common.console("status: source added");
                    log.info("New source added");
                } else {
                    Common.console("status: adding source canceled");
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // вставка нового слова для исключения из анализа частоты употребления слов
    public void insertNewExcludedWord(String pWord) {
        if (isConnectionToSQLite) {
            try {
                String query = "INSERT INTO exclude(word) " + "VALUES (?)";
                PreparedStatement st = connection.prepareStatement(query);
                st.setString(1, pWord);
                st.executeUpdate();
                st.close();

                Common.console("status: word \"" + pWord + "\" excluded from analysis");
                log.info("New word excluded from analysis");
            } catch (Exception e) {
                e.printStackTrace();
                Common.console("status: " + e.getMessage());
            }
        }
    }

    // вставка кода по заголовку для отсеивания ранее обнаруженных новостей
    public void insertTitleIn256(String pTitle) {
        if (isConnectionToSQLite) {
            try {
                String query256 = "INSERT INTO titles256(title) VALUES (?)";
                PreparedStatement st256 = connection.prepareStatement(query256);
                st256.setString(1, pTitle);
                st256.executeUpdate();
                st256.close();
            } catch (SQLException t) {
                t.printStackTrace();
            }
        }
    }

    // сохранение всех заголовков
    public void insertAllTitles(String pTitle, String pDate) {
        if (isConnectionToSQLite) {
            try {
                String query = "INSERT INTO all_news(title, news_date) VALUES (?, ?)";
                PreparedStatement st = connection.prepareStatement(query);
                st.setString(1, pTitle);
                st.setString(2, pDate);
                st.executeUpdate();
                st.close();
            } catch (SQLException ignored) {
            }
        }
    }

    // отсеивание заголовков
    public boolean isTitleExists(String pString256) {
        int isExists = 0;
        if (isConnectionToSQLite) {
            try {
                Statement st = connection.createStatement();
                String query = "SELECT max(1) FROM titles256 where exists (select title from titles256 t where t.title = '" + pString256 + "')";
                ResultSet rs = st.executeQuery(query);

                while (rs.next()) {
                    isExists = rs.getInt(1);
                }
                rs.close();
                st.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isExists == 1;
    }

    // новостей в архиве всего
    public int archiveNewsCount() {
        int countNews = 0;
        if (isConnectionToSQLite) {
            try {
                Statement st = connection.createStatement();
                String query = "SELECT count(*) FROM all_news";
                ResultSet rs = st.executeQuery(query);

                while (rs.next()) {
                    countNews = rs.getInt(1);
                }
                rs.close();
                st.close();
            } catch (Exception ignored) {
            }
        }
        return countNews;
    }

    // удаление источника
    public void deleteSource(String p_source) {
        if (isConnectionToSQLite) {
            try {
                String query = "DELETE FROM rss_list WHERE source = '" + p_source + "'";
                Statement del_st = connection.createStatement();
                del_st.executeUpdate(query);
                del_st.close();
            } catch (Exception e) {
                Common.console("status: " + e.getMessage());
            }
        }
    }

    // удаление слова исключенного из поиска
    public void deleteExcluded(String p_source) {
        if (isConnectionToSQLite) {
            try {
                String query = "DELETE FROM exclude WHERE word = '" + p_source + "'";
                Statement del_st = connection.createStatement();
                del_st.executeUpdate(query);
                del_st.close();
            } catch (Exception e) {
                Common.console("status: " + e.getMessage());
            }
        }
    }

    // обновление статуса чекбокса is_active для ресурсов SELECT id, source, link FROM rss_list where is_active = 1  ORDER BY id
    public void updateIsActiveStatus(boolean pBoolean, String pSource) {
        if (isConnectionToSQLite) {
            try {
                Statement st = connection.createStatement();
                String query = "UPDATE rss_list SET is_active = " + pBoolean + " where source = '" + pSource + "'";
                st.executeUpdate(query);
                st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // удаление дубликатов новостей
    public void deleteDuplicates() {
        if (isConnectionToSQLite) {
            try {
                Statement st = connection.createStatement();
                String query = "DELETE FROM all_news WHERE rowid NOT IN (SELECT min(rowid) FROM all_news GROUP BY title, news_date)";
                st.executeUpdate(query);
                st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // закрытие соединения SQLite
    public void closeSQLiteConnection() {
        try {
            if (isConnectionToSQLite) {
                connection.close();
                log.info("Connection closed");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
