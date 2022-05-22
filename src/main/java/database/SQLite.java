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
    private final Utilities dbUtil = new Utilities();

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
            PreparedStatement preparedStatement =
                    connection.prepareStatement(dbUtil.getSQLQueryFromProp("selectSQLite"));
            preparedStatement.setInt(1, WORD_FREQ_MATCHES);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String word = rs.getString("TITLE");
                int sum = rs.getInt("SUM");
                Object[] row2 = new Object[]{word, sum};
                Gui.modelForAnalysis.addRow(row2);
            }
            deleteTitles();
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Delete from news_dual
    public void deleteTitles() {
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(dbUtil.getSQLQueryFromProp("deleteTitles"));
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Delete from titles256
    public void deleteFrom256() {
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(dbUtil.getSQLQueryFromProp("deleteFrom256"));
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
                    smiSearch();
                    break;
                case "excl":
                    //excluded words
                    excludedSearch();
                    break;
                case "active_smi":
                    activeSMISearch();
                    break;
            }

        }
    }

    private void activeSMISearch() {
        Common.SMI_SOURCE.clear();
        Common.SMI_LINK.clear();
        Common.SMI_IS_ACTIVE.clear();
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(dbUtil.getSQLQueryFromProp("activeSmiQuery"));
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
    }

    private void excludedSearch() {
        Common.EXCLUDED_WORDS.clear();
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(dbUtil.getSQLQueryFromProp("exclQuery"));
            while (rs.next()) {
                String word = rs.getString("word");
                Common.EXCLUDED_WORDS.add(word);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void smiSearch() {
        //sources
        Common.SMI_SOURCE.clear();
        Common.SMI_LINK.clear();
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(dbUtil.getSQLQueryFromProp("smiQuery"));
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
    }

    // вставка нового источника
    public void insertNewSource() {
        if (isConnectionToSQLite) {
            int max_id_in_source = 0;
            int new_id;
            try {
                Statement max_id_st = connection.createStatement();
                ResultSet rs = max_id_st.executeQuery(dbUtil.getSQLQueryFromProp("maxIdQuery"));
                while (rs.next()) {
                    max_id_in_source = rs.getInt("ID");
                }
                rs.close();
                max_id_st.close();
                new_id = max_id_in_source + 1;

                // Диалоговое окно добавления источника новостей в базу данных
                JTextField source_name = new JTextField();
                JTextField rss_link = new JTextField();
                Object[] new_source = {"Source:", source_name, "Link to rss:", rss_link};

                int result = JOptionPane.showConfirmDialog(Gui.scrollPane, new_source, "New source", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.YES_OPTION) {

                    //запись в БД
                    PreparedStatement preparedStatement = connection.prepareStatement(Main.prop.getProperty("insertNewSource"));
                    preparedStatement.setInt(1, new_id);
                    preparedStatement.setString(2, source_name.getText());
                    preparedStatement.setString(3, rss_link.getText());
                    preparedStatement.executeUpdate();
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
                PreparedStatement preparedStatement =
                        connection.prepareStatement(dbUtil.getSQLQueryFromProp("insertExcludeWord"));
                preparedStatement.setString(1, pWord);
                preparedStatement.executeUpdate();

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
                PreparedStatement preparedStatement =
                        connection.prepareStatement(dbUtil.getSQLQueryFromProp("insertTitle256"));
                preparedStatement.setString(1, pTitle);
                preparedStatement.executeUpdate();
            } catch (SQLException t) {
                t.printStackTrace();
            }
        }
    }

    // сохранение всех заголовков
    public void insertAllTitles(String pTitle, String pDate) {
        if (isConnectionToSQLite) {
            try {
                PreparedStatement preparedStatement =
                        connection.prepareStatement(dbUtil.getSQLQueryFromProp("insertAllTitles"));
                preparedStatement.setString(1, pTitle);
                preparedStatement.setString(2, pDate);
                preparedStatement.executeUpdate();
            } catch (SQLException ignored) {
            }
        }
    }

    // отсеивание заголовков
    public boolean isTitleExists(String pString256) {
        int isExists = 0;
        if (isConnectionToSQLite) {
            try {
                PreparedStatement preparedStatement =
                        connection.prepareStatement(dbUtil.getSQLQueryFromProp("titleExists"));
                preparedStatement.setString(1, pString256);
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    isExists = rs.getInt(1);
                }
                rs.close();
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
                ResultSet rs = st.executeQuery(dbUtil.getSQLQueryFromProp("archiveNewsCount"));

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
                PreparedStatement preparedStatement =
                        connection.prepareStatement(dbUtil.getSQLQueryFromProp("deleteSource"));
                preparedStatement.setString(1, p_source);
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                Common.console("status: " + e.getMessage());
            }
        }
    }

    // удаление слова исключенного из поиска
    public void deleteExcluded(String p_source) {
        if (isConnectionToSQLite) {
            try {
                PreparedStatement preparedStatement =
                        connection.prepareStatement(dbUtil.getSQLQueryFromProp("deleteExcluded"));
                preparedStatement.setString(1, p_source);
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                Common.console("status: " + e.getMessage());
            }
        }
    }

    // обновление статуса чекбокса is_active для ресурсов SELECT id, source, link FROM rss_list where is_active = 1  ORDER BY id
    public void updateIsActiveStatus(boolean pBoolean, String pSource) {
        if (isConnectionToSQLite) {
            try {
                PreparedStatement preparedStatement =
                        connection.prepareStatement(dbUtil.getSQLQueryFromProp("updateActiveStatus"));
                preparedStatement.setBoolean(1, pBoolean);
                preparedStatement.setString(2, pSource);
                preparedStatement.executeUpdate();
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
                st.executeUpdate(dbUtil.getSQLQueryFromProp("deleteAllDuplicates"));
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
