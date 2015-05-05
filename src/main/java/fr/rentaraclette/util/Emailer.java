package fr.rentaraclette.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Emailer extends Thread {

	private static Emailer 			instance = null;
	
	private Properties 				props;
	private SmtpAuthenticator 		authentication;
	private Session 				session;

	private String 					from = "RentaRaclette";
	private String 					to;
	private String 					subject;
	private String 					body;

	public static void main(String... aArguments ){
		Emailer emailer = Emailer.getInstance();
		//the domains of these email addresses should be valid,
		//or the example will fail:
		emailer.sendEmail(
				"cohensteve@hotmail.fr",
				"Testing 1-2-3",
				"blah blah blah"
				);
	}

	private Emailer() {
		props = new Properties();
        props.put("mail.smtp.host" , "smtp.mail.yahoo.com");

        //To use TLS
        props.put("mail.smtp.auth", "true"); 
//        props.put("mail.smtp.starttls.enable", "true");
        //To use SSL
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        authentication = new SmtpAuthenticator();
        session  = Session.getDefaultInstance(props, authentication);
	}

	public static Emailer getInstance() {
		if (instance == null)
			instance = new Emailer();
		return instance;
	}
	
	@Override
	public void run() {
        Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(from));
            msg.setRecipient(Message.RecipientType.TO, 
                new InternetAddress(to));
            msg.setSubject(subject);
            msg.setText(body);
            Transport.send(msg);
            Logger.info("Email was sent!");
        }
        catch(Exception exc) {
//            Logger.error(exc.getMessage());
        	exc.printStackTrace();
        }
	}

	/**
	 * Send a single email.
	 */
	public void sendEmail(String aToEmailAddr, String aSubject, String aBody){
		to = aToEmailAddr;
        subject = aSubject;
        body = aBody;
		this.start();
	}

	public class SmtpAuthenticator extends Authenticator {
		public SmtpAuthenticator() {

		    super();
		}

		@Override
		public PasswordAuthentication getPasswordAuthentication() {
		 String username = "rentaraclette@yahoo.com";
		 String password = "azertyuiop";
		    if ((username != null) && (username.length() > 0) && (password != null) 
		      && (password.length   () > 0)) {

		        return new PasswordAuthentication(username, password);
		    }

		    return null;
		}
	}
} 