package ros.game.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class HighScoresTableModel extends AbstractTableModel {

    /**
     *
     */
    private static final long serialVersionUID = -1542007495150352216L;
    private String query = "SELECT * FROM top15 ORDER BY points DESC";
    private ArrayList<Object[]> top15Cache;

    public HighScoresTableModel(Connection conn) {

        top15Cache = new ArrayList<Object[]>();

        if (conn != null) {
            try {
                Statement db_statement = conn.createStatement();
                ResultSet resultTop15 = db_statement.executeQuery(query);

                while (resultTop15.next()) {
                    Object[] row = new Object[getColumnCount()];
                    for (int j = 0; j < row.length; j++) {
                        //position
                        if (j == 0) {
                            row[j] = new Integer(top15Cache.size() + 1);
                        } else { //other info
                            row[j] = resultTop15.getObject(j+1);
                        }
                    }
                    top15Cache.add(row);
                }

                resultTop15.close();
                db_statement.close();

                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int c) {
        switch (c) {
            case 0:
                return "Position";
            case 1:
                return "Player Name";
            case 2:
                return "Points";
            case 3:
                return "Level";
            default:
                return null;
        }
    }

    @Override
    public int getRowCount() {
        return 15;
        //return top15Cache.size();
    }

    @Override
    public Object getValueAt(int r, int c) {
        if (r < top15Cache.size()) {
            return ((Object[]) top15Cache.get(r))[c];
        } else {
            return null;
        }
    }

    @Override
    public boolean isCellEditable(int arg0, int arg1) {
        return false;
    }
}
