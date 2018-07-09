package tn.esprit.seahawks.persistence;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author DELL
 */
public class SendEmail {
     public static boolean sendMail(String from,String password, String message,String to[]){
     String host = "smtp.gmail.com";
     Properties props= System.getProperties();
     props.put("mail.smtp.starttls.enable", "true");
     props.put("mail.smtp.host", host);
     props.put("mail.smtp.user", from);
     props.put("mail.smtp.password", password);
     props.put("mail.smtp.port", 587);
     props.put("mail.smtp.auth", "true");
     props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
     Session session=Session.getDefaultInstance(props, null);
     MimeMessage mimeMessage =new MimeMessage(session);
    try{
     mimeMessage.setFrom(new InternetAddress(from));
     InternetAddress[] toAddresses= new InternetAddress[to.length];
     for(int i=0;i<to.length;i++){
     toAddresses[i]=new InternetAddress(to[i]);
     }
     for(int i=0;i<toAddresses.length;i++){
     mimeMessage.addRecipient(javax.mail.Message.RecipientType.TO, toAddresses[i]);
     }
     mimeMessage.setSubject("Let's walk");
     mimeMessage.setText(message);
     Transport transport = session.getTransport("smtp");
     transport.connect(host,from,password);
     transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
  transport.close();
  return true;
    }catch(MessagingException me){
    me.printStackTrace();
    }
     return false;
         
    }
    
}