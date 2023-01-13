import java.util.Properties;
import java.util.Scanner;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class App {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        //Query user with the needed information
        System.out.println("provide host");
        String hostName = sc.nextLine();
        System.out.println("Provide username:");
        String userName = sc.nextLine();
        System.out.println("Provide password");
        String password = sc.nextLine();

        Properties properties = new Properties();

                //Assign property values that will be used for this session
                properties.put("mail.imap.host", hostName);
                properties.put("mail.imap.port", 993);
                properties.put("mail.imap.sll.enable!", true);
                properties.put("mail.imap.ssl.trust", hostName);
                properties.put("mail.store.protocol", "imap");
                

                //Obtains a new Session based on the values in the properties object
                Session session = Session.getInstance(properties, new Authenticator() {
                    /*
                    Using gmails 2fa and a generated password to request access to gmails service and send the email
                    previous method of sending an email has been deprecated since 30th of may 2022
                     */
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userName, password);
                    }
                });
                
                //Obtain a connection to the host
                Store store = session.getStore("imaps");

                store.connect(hostName, userName, password);
                
                //Retrieve all the emails stored in the Host database
                Folder emailFolder = store.getFolder("INBOX");
                emailFolder.open(Folder.READ_ONLY);
                
                Message[] messages = emailFolder.getMessages();
                
                //Print out the contents of the database
                for (int i = 0; i < 20; i++){

                    Message currentMessage = messages[i];

                    System.out.println("Message: " + (i+1));
                    System.out.println("From : " + currentMessage.getFrom()[0]);
                    System.out.println("subject : " + currentMessage.getSubject());      
                    
                }
                
                emailFolder.close(true);
                store.close();
                
    }
}
