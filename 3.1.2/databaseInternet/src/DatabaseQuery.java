import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.awt.Desktop;
import java.util.Scanner;

public class DatabaseQuery {
    public DatabaseQuery() {
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        String computer = "atlas.dsv.su.se";
        String db_name = "db_21114200";
        String url = "jdbc:mysql://" + computer + "/" + db_name;
        String db_user = "usr_21114200";
        String password = "114200";
        Scanner sc = new Scanner(System.in);

        try {
            Connection dbConnection = DriverManager.getConnection(url, db_user, password);
            Statement statement = dbConnection.createStatement();

            String name = sc.nextLine();
            String email = sc.nextLine();
            String webpage = sc.nextLine();
            String comment = sc.nextLine();
            statement.executeUpdate("INSERT INTO Logs (personName, email, webpage, comment) VALUES ('"+name+"','"+email+"','"+webpage+"','"+comment +"')");


            ResultSet resultSet = statement.executeQuery("SELECT * from Logs");

            String personLog = "";
            while (resultSet.next()) {
                personLog += resultSet.getString("personName") + "\n";
                personLog += resultSet.getString("email") + "\n";
                personLog += resultSet.getString("webpage") + "\n";
                personLog += resultSet.getString("comment") + "\n\n";

            }

            String htmlTemplate = "<!DOCTYPE html><html><body><textarea name=\"database-input\" id=\"logs\" cols=\"40\" rows=\"40\">$database-logs</textarea></body></html>";

            File htmlPage = new File(".\\logs.html");
            try{
                BufferedWriter bw = new BufferedWriter(new FileWriter(htmlPage));
                bw.write(htmlTemplate.replace("$database-logs", personLog));
                bw.close();
                sc.close();
                Desktop.getDesktop().open(htmlPage);
            }catch(IOException e){
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}