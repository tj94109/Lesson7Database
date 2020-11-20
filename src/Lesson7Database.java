import java.io.IOException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Scanner;

public class Lesson7Database {

    private static String url = "jdbc:derby://localhost:1527/COREJAVA;create=true";

    public static void main(String[] args) throws IOException, SQLException {

        System.out.println("Database Connection info: " + url);
        Connection conn = DriverManager.getConnection(url);
        System.out.println("Connecting: done\n");
        Statement stat = conn.createStatement();
        System.out.println("Populating the database:\n");
        try (Scanner in = new Scanner(Paths.get(args[0]), "UTF-8")) {

                while (in.hasNext()) {
                    String line = in.nextLine().trim();
                    if (line.endsWith(";")) {
                        line = line.substring(0, line.length()-1);
                    }
                    try {
                        stat.execute(line);
                        int updateCount = stat.getUpdateCount();
                        System.out.println(updateCount + " rows updated");

                    } catch (SQLException ex) {
                        for (Throwable e : ex)
                            e.printStackTrace();
                    }
                }

        }catch(IOException ex){
            ex.printStackTrace();
        }
        System.out.println("Done");
        Statement selectStat = conn.createStatement();
        System.out.println("Query of Lessons table results:");
        ResultSet results = selectStat.executeQuery("SELECT * FROM Lessons");
        showResults(results);
        results.close();
        selectStat.close();
        conn.close();
    }


    public static void showResults(ResultSet result) throws SQLException {
        ResultSetMetaData metaData = result.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            if (i > 1) System.out.print(",\t\t");
            System.out.print(metaData.getColumnName(i));
        }
        System.out.println();
        while (result.next()) {
            for (int i = 1; i <= columnCount; i++) {
                if (i > 1) System.out.print(", ");
                System.out.print(result.getString(i));
            }
            System.out.println();
        }
    }

}