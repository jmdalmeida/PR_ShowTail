package JDBC;

public class ConnectionFactory {
    
    private static final JDBCConnections jdbc;
    
    static {
        jdbc = new JDBCConnections("localhost", "db_pr", "user", "user");
    }
    
    private ConnectionFactory(){}
    
    public static JDBCConnections getInstance() {
        return jdbc;
    }
}
