package database.DBSearch;

import database.Utilities;
import utils.Common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static database.SQLite.connection;

public class ActiveSmiSearch implements DBSearch {
    private final Utilities dbUtil = new Utilities();

    @Override
    public void dbSearch() {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
