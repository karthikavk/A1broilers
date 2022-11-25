package in.kassapos.a1broilers.api;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class EmailSend extends Thread {
	public String HOST ="antonychicken.in";// "gourmeda.com";
	public String USER ="info@antonychicken.in";//"agasthyataxi@gmail.com";// "info@gourmeda.com";
	public String PASSWORD = "odOlisa34";//"agasthyateam1";
	public String PORT = "25";
	private String TO="palanibe91@gmail.com";
    private  String SUBJECT = "Error Report";
    private  String TEXT;
     public EmailSend(String host, String port,
            final String userName, final String password, String toAddress,
            String subject, String tEXT2) {
		HOST=host;
		PORT=port;
		USER=userName;
		PASSWORD=password;
		TO=toAddress;
		SUBJECT=subject;
		TEXT=tEXT2;
	}
    public EmailSend(String toAddress,
            String subject, String tEXT2) {
		TO=toAddress;
		SUBJECT=subject;
		TEXT=tEXT2;
	}
    public EmailSend(String tEXT2) {
		
		TEXT=tEXT2;
	}
	public  void SendEmail(String host, String port,
            final String userName, final String password, String toAddress,
            String subject, String tEXT2) throws AddressException,
            MessagingException {
 
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
      // properties.put("mail.smtp.starttls.enable", "true");
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };  
 
        Session session = Session.getInstance(properties, auth);
        try{
        Message msg = new MimeMessage(session);
        Multipart mp = new MimeMultipart();
        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        MimeBodyPart htmlPart = new MimeBodyPart();
       // String s=HTMLEncode.encode(tEXT2);
        htmlPart.setContent(tEXT2, "text/html");
        mp.addBodyPart(htmlPart); 
        msg.setContent(mp); 
        Transport.send(msg);
        }
        catch(Exception IO)
        {
        	
        	IO.printStackTrace();
        }

    }
	public void run(){
		try {
			SendEmail(HOST,PORT,USER,PASSWORD,TO,SUBJECT,TEXT);
		} catch (MessagingException e) {
			
			e.printStackTrace(); 
		} 
	}
	public static void main(String args[]){
		/*String s="<table style='border-collapse:collapse;border:2px solid red;' class='table table-bordered table-responsive col-lg-12 Shoppingtable' id='OrderPageTable'>      <thead>            <tr>                <th>Item List </th>                <th>Quantity</th>                <th>Amount</th>            </tr>            </thead>        </table>";
		String s2 = HTMLEncode.encode(s);
		s2=s2;*/
		new EmailSend("palanibe91@gmail.com", "palani", "fdf").start();
	}
	
}
