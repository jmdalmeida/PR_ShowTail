package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCConnection {

    private final String host, database, user, password;
    private Connection conn = null;
    private PreparedStatement stmt = null;

    public JDBCConnection(String host, String database, String user,
                           String password) {
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    private boolean loadDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            return true;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    private boolean setupConnection() {
        try {
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://"
                                                            + host + "/" + database + "?user=" + user + "&password="
                                                            + password);
            return true;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }
    }

    public void setAutoCommit(boolean value) {
        try {
            conn.setAutoCommit(value);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void commit() throws SQLException {
            conn.commit();
    }

    public void rollback() {
        try {
            conn.rollback();
        } catch (SQLException ex) {
        }
    }

    public long insertAndReturnId(String sql, Object[] params) throws SQLException {
        ResultSet generatedKeys = null;
        stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        int len = params != null ? params.length : 0;
        for (int i = 1; i <= len; i++) {
            stmt.setObject(i, params[i - 1]);
        }
        stmt.executeUpdate();

        generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getLong(1);
        }
        return -1;
    }

    public void update(String sql, Object[] params) throws SQLException {
        stmt = conn.prepareStatement(sql);
        int len = params != null ? params.length : 0;
        for (int i = 1; i <= len; i++) {
            stmt.setObject(i, params[i - 1]);
        }
        stmt.executeUpdate();
    }

    public ResultSet select(String sql, Object[] params) throws SQLException {
        stmt = conn.prepareStatement(sql);
        int len = params != null ? params.length : 0;
        for (int i = 1; i <= len; i++) {
            stmt.setObject(i, params[i - 1]);
        }
        return stmt.executeQuery();
    }

    public boolean init() {
        return loadDriver() && setupConnection();
    }

    public void close() {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
            stmt = null;
            conn = null;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
    }
}
