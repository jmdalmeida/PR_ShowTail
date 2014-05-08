package JDBC;

public class ConnectionFactory {
    
    private static final JDBCConnection jdbc;
    
    static {
        jdbc = new JDBCConnection("localhost", "shows", "Andre", "a12345");
    }
    
    private ConnectionFactory(){}
    
    public static JDBCConnection getInstance() {
        return jdbc;
    }
}
