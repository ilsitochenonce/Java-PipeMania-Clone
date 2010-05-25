package ros.game.database;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JTable;
import ros.game.util.ColoredJTable;

public class DatabaseManager {

    private Connection conn = null;

    public DatabaseManager() {
        //delete();
        create();

        //getConnection();
        //insertRecord("Ross", 15, 2, 3, 4, 5, 6, 7, 8);
        //insertRecord("Nick", 8, 2, 3, 4, 5, 6, 7, 8);
        //disconnect();
    }

    /**
     * @return Connection or null if connection fail
     */
    public Connection getConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection("jdbc:derby:top15;create=true");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
            conn = null;
        }
        return conn;
    }

    public void disconnect() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void insertRecord(String playername, int points, int level) {

        conn = getConnection();

        if (conn != null) {
            try {
                String query = "INSERT INTO top15(playername, points, level) VALUES (?,?,?)";

                PreparedStatement st = conn.prepareStatement(query);

                if(playername == null)
                    st.setString(1, "playername");
                else
                    st.setString(1, playername);
                st.setInt(2, points);      // total Points
                st.setInt(3, level);       // level

                st.executeUpdate();

                st.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        disconnect();
    }

    public void printTop15() {
        conn = getConnection();

        try {
            String query = "SELECT * FROM top15 ORDER BY points DESC";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            System.out.println("Posizione - Nome - Punti - Livello");
            int pos = 1;
            while(rs.next()){
                System.out.println(pos + " - " + rs.getString(2) + " - " + rs.getInt(3) + " - "+ rs.getInt(4));
                pos++;
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        disconnect();
    }

    public JTable getTop15Table() {

        Color[] colors = {
            new Color(255, 224, 224),   // Light red
            new Color(255, 255, 255),   // White
            new Color(208, 255, 255),   // Light cyan
            new Color(255, 255, 255),   // White
        };


        conn = getConnection();

        JTable table = new ColoredJTable(new HighScoresTableModel(conn),colors);

        return table;
    }

    /**
     * Utility metod
     *
     * delete table highscores
     */
    private void delete() {
        conn = getConnection();

        if (conn != null) {
            try {
                Statement db_statement = conn.createStatement();

                db_statement.executeUpdate("DROP TABLE top15 ");

                System.out.println("tabella top15 cancellata dal database");
                db_statement.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            disconnect();
        }

    }

    /**
     * If no exist create DB.
     *
     * If no exist table highscores create table.
     *
     */
    private void create() {

        conn = getConnection();

        if (conn != null) {
            try {
                // if the table not exist => create
                DatabaseMetaData dbmd;

                dbmd = conn.getMetaData();

                String[] myTables = {"TABLE"};
                ResultSet tabelle = dbmd.getTables(null, null, "%", myTables);
                String tableName = null;

                if (tabelle.next()) {
                    tableName = tabelle.getString("TABLE_NAME");
                    System.out.println(tableName + " already in the db.");
                } else {
                    tabelle.close();
                    System.out.println("nessuna tabella nel database");

                    // crea tabella
                    Statement db_statement = conn.createStatement();

                    db_statement.executeUpdate("CREATE TABLE top15 (id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT top15_pk PRIMARY KEY, playername VARCHAR(50), points INTEGER, level INTEGER)");

                    System.out.println("tabella top15 creata nel database");

                    for (int i = 0; i < 15; i++) {
                        insertRecord(tableName, 0, 0);
                        System.out.println("inserito record " + i);
                    }

                    db_statement.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            disconnect();
        }
    }
}
