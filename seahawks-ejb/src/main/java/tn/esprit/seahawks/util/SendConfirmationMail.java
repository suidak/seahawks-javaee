package tn.esprit.seahawks.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendConfirmationMail {
	public static boolean sendMail(String from, String password, String To,String subject, String mail_content) {
		String host = "smtp.gmail.com";
		Properties props = System.getProperties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", password);
		props.put("mail.smtp.port", 587);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		Session session = Session.getDefaultInstance(props, null);
		Message mimeMessage = new MimeMessage(session);
		String content =" <!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                    " \n" +
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                    "<head>\n" +
                    "    <title>Animal Caring</title>\n" +
                    "    <style media=\"screen\" type=\"text/css\">\n" +
                    "\n" +
                    " /* Global Styles */\n" +
                    " \n" +
                    "*{\n" +
                    "    padding: 0; /* Reset all padding to 0 */\n" +
                    "    margin: 0; /* Reset all margin to 0 */\n" +
                    "}\n" +
                    " \n" +
                    "body{\n" +
                    "    background: #F9F9F9; /* Set HTML background color */\n" +
                    "    font: 14px \"Lucida Grande\";  /* Set global font size & family */\n" +
                    "    color: #464646; /* Set global text color */\n" +
                    "}\n" +
                    " \n" +
                    "p{\n" +
                    "    margin: 10px 0px 10px 0px; /* Add some padding to the top and bottom of the <p> tags */\n" +
                    "}\n" +
                    " \n" +
                    "/* Header */\n" +
                    " \n" +
                    "#header{\n" +
                    "    height: 45px; /* Set header height */\n" +
                    "    background: #464646; /* Set header background color */\n" +
                    "}\n" +
                    " \n" +
                    "#header h3{\n" +
                    "    color: #FFFFF3; /* Set header heading(top left title ) color */\n" +
                    "    padding: 10px; /* Set padding, to center it within the header */\n" +
                    "    font-weight: normal; /* Set font weight to normal, default it was set to bold */\n" +
                    "}\n" +
                    " \n" +
                    "/* Wrap */\n" +
                    " \n" +
                    "#wrap{\n" +
                    "    background: #FFFFFF; /* Set content background to white */\n" +
                    "    width: 615px; /* Set the width of our content area */\n" +
                    "    margin: 0 auto; /* Center our content in our browser */\n" +
                    "    margin-top: 50px; /* Margin top to make some space between the header and the content */\n" +
                    "    padding: 10px; /* Padding to make some more space for our text */\n" +
                    "    border: 1px solid #DFDFDF; /* Small border for the finishing touch */\n" +
                    "    text-align: center; /* Center our content text */\n" +
                    "}\n" +
                    " \n" +
                    "#wrap h3{\n" +
                    "    font: italic 22px Georgia; /* Set font for our heading 2 that will be displayed in our wrap */\n" +
                    "}\n" +
                    " \n" +
                    "/* Form & Input field styles */\n" +
                    " \n" +
                    "form{\n" +
                    "    margin-top: 10px; /* Make some more distance away from the description text */\n" +
                    "}\n" +
                    " \n" +
                    "form .submit_button{\n" +
                    "    background: #F9F9F9; /* Set button background */\n" +
                    "    border: 1px solid #DFDFDF; /* Small border around our submit button */\n" +
                    "    padding: 8px; /* Add some more space around our button text */\n" +
                    "}\n" +
                    " \n" +
                    "input{\n" +
                    "    font: normal 16px Georgia; /* Set font for our input fields */\n" +
                    "    border: 1px solid #DFDFDF; /* Small border around our input field */\n" +
                    "    padding: 8px; /* Add some more space around our text */\n" +
                    "} \n" +
                    "\n" +
                    "</style> \n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <!-- start header div -->\n" +
                    "    <div id=\"header\">\n" +
                    "        <h3>NETTUTS > Sign up</h3>\n" +
                    "    </div>\n" +
                    "    <!-- end header div -->  \n" +
                    "     \n" +
                    "    <!-- start wrap div -->  \n" +
                    "    <div id=\"wrap\">\n" +
                    "         \n" +
                    "        <!-- start php code -->\n" +
                    "         \n" +
                    "        <!-- stop php code -->\n" +
                    "     \n" +
                    "        <!-- title and description -->   \n" +
                    "        <h3>Welcome Email : </h3>\n" +
                    "        <p>"+mail_content+"</p>\n" +
                    "         \n" +
                    "    </div>\n" +
                    "    <!-- end wrap div -->\n" +
                    "</body>\n" +
                    "</html>";
		try {
			mimeMessage.setFrom(new InternetAddress(from));
//			InternetAddress[] toAddresses = new InternetAddress[string.length];
//			for (int i = 0; i < string.length; i++) {
//				toAddresses[i] = new InternetAddress(string[i]);
//			}
//			for (int i = 0; i < toAddresses.length; i++) {
//				mimeMessage.addRecipient(javax.mail.Message.RecipientType.TO, toAddresses[i]);
//			}
			InternetAddress toAddress= new InternetAddress(To);
			mimeMessage.addRecipient(javax.mail.Message.RecipientType.TO, toAddress);
			mimeMessage.setSubject(subject);
			mimeMessage.setContent(content, "text/html; charset=utf-8");
			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, password);
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
			transport.close();
			return true;
		} catch (MessagingException me) {
			me.printStackTrace();
		}
		return false;

	}
}
