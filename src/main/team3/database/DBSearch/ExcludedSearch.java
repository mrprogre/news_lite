package team3.database.DBSearch;

import team3.database.SQLite;
import team3.database.Utilities;
import team3.utils.Common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExcludedSearch implements DBSearch {
    private final Utilities dbUtil = new Utilities();

    @Override
    public void dbSearch() {
        Common.EXCLUDED_WORDS.clear();
        try {
            Statement st = SQLite.connection.createStatement();
            ResultSet rs = st.executeQuery(dbUtil.getSQLQueryFromProp("exclQuery"));
            while (rs.next()) {
                String word = rs.getString("word");
                Common.EXCLUDED_WORDS.add(word);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
