import java.util.Properties;
import java.util.Scanner;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class App {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.println("Provide username:");
        String userName = sc.nextLine();
        System.out.println("Provide password");
        String password = sc.nextLine();

        Properties properties = new Properties();

                //Assign property values that will be used for this session
                properties.put("mail.imap.host", "imap.gmail.com");
                properties.put("mail.imap.port", 993);
                properties.put("mail.imap.starttls.enable", true);
                properties.put("mail.imap.ssl.trust", "imap.gmail.com");
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

                store.connect("imap.gmail.com",userName, password);
                
                //Retrieve all the emails stored in the Host database
                Folder emailFolder = store.getFolder("INBOX");
                emailFolder.open(Folder.READ_ONLY);
                
                Message[] messages = emailFolder.getMessages();
                
                for (int i = 0; i < messages.length; i++){

                    Message currentMessage = messages[i];

                    System.out.println("Email Number : " + (i+1));
                    System.out.println("Message : " + currentMessage.getSubject());
                    System.out.println("Message : " + currentMessage.getFrom()[0]);
                        
                    
                }
                
                emailFolder.close(true);
                store.close();
                
    }
}
