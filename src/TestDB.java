import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class TestDB {

    public static String url = "jdbc:derby://localhost:1527/COREJAVA;create=true";

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        System.out.println(new String(new char[40]).replace("\0", "-"));

        try{
            runTest();
        }catch (SQLException ex)
        {
            for (Throwable t : ex){
                t.printStackTrace();
            }
        }

    }

    public static void runTest() throws SQLException, IOException, ClassNotFoundException {

//        try{
//            Class.forName("org.apache.derby.jdbc.ClientDriver");// or may be it is "org.apache.derby.jdbc.EmbeddedDriver"? Not sure. Check the correct name and put it here.
//        } catch(ClassNotFoundException e){
//            e.printStackTrace();
//        }

        try(Connection conn = DriverManager.getConnection(url);
            Statement stat = conn.createStatement())
        {
            //stat.executeUpdate("DROP TABLE Greetings");
            stat.executeUpdate("CREATE TABLE Greetings (Message CHAR(20))");
            stat.executeUpdate("INSERT INTO Greetings VALUES ('Hello, World!')");
            try(ResultSet result = stat.executeQuery("SELECT * FROM Greetings"))
            {
                if(result.next())
                    System.out.println(result.getString(1));
            }
            //stat.executeUpdate("DROP TABLE Greetings");
        }


    }

    public static Connection getConnection() throws IOException, SQLException {

        Properties props = new Properties();
        try(InputStream in = Files.newInputStream(Paths.get("database.properties")) ){
            props.load(in);
        }
        String drivers = props.getProperty("jdbc.drivers");
        if(drivers != null) System.setProperty("jdbc.drivers", drivers);
        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        return DriverManager.getConnection(url, username, password);
    }


}
