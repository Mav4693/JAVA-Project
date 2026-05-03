package airlinemanagementsystem;
import java.sql.*;
import java.io.InputStream;
import java.util.Properties;

public class Conn {

    Connection c;
    Statement s;

    public Conn(){
        try{

            Properties props = new Properties();
            InputStream io = new java.io.FileInputStream("config.properties");
            props.load(io);

            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");

            // Class.forName("com.mysql.cj.jdbc.Driver");
            //com.mysql.cj.jdbc.Driver is the MySQL JDBC Driver Class
            //This class acts like a translator between: Java program ↔ MySQL database
c = DriverManager.getConnection( url,username,password);
            s = c.createStatement();
    }
catch(Exception e){
            e.printStackTrace();
        }
    }
}
