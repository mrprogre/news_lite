package database;

import gui.Gui;
import main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Common;

import javax.swing.*;
import java.sql.*;

public class SQLite {
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLite.class);
    public static Connection connection;
    public static boolean isConnectionToSQLite;
    private static final int WORD_FREQ_MATCHES = 2;

    // Открытие соединения с базой данных
    public void openSQLiteConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + Main.DIRECTORY_PATH + "news.db");
            isConnectionToSQLite = true;
            LOGGER.warn("Connected to SQLite");
            Thread.sleep(1000L);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn(e.getMessage());
        }
    }

    // Заполняем таблицу анализа
    public void selectSqlite() {
        try {
            Statement st = connection.createStatement();
            // TODO: Add SQL Query to properties
            String query = "SELECT SUM, TITLE FROM v_news_dual WHERE sum > " +
                    WORD_FREQ_MATCHES +
                    " AND title NOT IN (SELECT word FROM all_titles_to_exclude)" +
                    " ORDER BY SUM DESC ";
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
            String query = "DELETE FROM news_dual";
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
            String query = "DELETE FROM titles256";
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
                        String query = "SELECT id, source, link FROM rss_list WHERE is_active = 1  ORDER BY id";
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
                String max_id_query = "SELECT max(id) AS id FROM rss_list";
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
                    // TODO: Add SQL Query to properties
                    String query = "INSERT INTO rss_list(id, source, link, is_active) " + "VALUES (" + new_id + ", '" + source_name.getText() + "', '" + rss_link.getText() + "', " + 1 +")";
                    Statement st = connection.createStatement();
                    st.executeUpdate(query);
                    st.close();

                    Common.console("status: source added");
                    LOGGER.warn("New source added");
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
                //запись в БД
                // TODO: Add SQL Query to properties
                String query = "INSERT INTO exclude(word) " + "VALUES ('" + pWord + "')";
                Statement st = connection.createStatement();
                st.executeUpdate(query);
                st.close();

                Common.console("status: word \"" + pWord + "\" excluded from analysis");
                LOGGER.warn("New word excluded from analysis");
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
                // TODO: Add SQL Query to properties
                String query256 = "INSERT INTO titles256(title) VALUES ('" + pTitle + "')";
                Statement st256 = connection.createStatement();
                st256.executeUpdate(query256);
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
                // TODO: Add SQL Query to properties
                String q = "INSERT INTO all_news(title, news_date) VALUES ('" + pTitle + "', '" + pDate + "')";
                Statement st = connection.createStatement();
                st.executeUpdate(q);
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
                // TODO: Add SQL Query to properties
                String query = "SELECT max(1) FROM titles256 WHERE exists (SELECT title FROM titles256 t WHERE t.title = '" + pString256 + "')";
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
                // TODO: Add SQL Query to properties
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
                // TODO: Add SQL Query to properties
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
                // TODO: Add SQL Query to properties
                String query = "UPDATE rss_list SET is_active = " + pBoolean + " WHERE source = '" + pSource + "'";
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
                LOGGER.warn("Connection closed");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
