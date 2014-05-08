package JDBC;

public class ConnectionFactory {
    
    private static final JDBCConnection jdbc;
    
    static {
        jdbc = new JDBCConnection("localhost", "db_pr", "user", "user");
    }
    
    private ConnectionFactory(){}
    
    public static JDBCConnection getInstance() {
        return jdbc;
    }
}
