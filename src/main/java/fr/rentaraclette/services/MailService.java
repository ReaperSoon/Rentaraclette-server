package fr.rentaraclette.services;

import javax.xml.soap.SOAPMessage;

import fr.rentaraclette.rpc.RemoteService;
import fr.rentaraclette.util.Emailer;

public class MailService extends AbstractService {
	//private ExampleDao 				exampleDao = ExampleDao.getInstance();
	private static MailService 	instance = null;
	
	/* 
	 * Public constructor because of the Java Reflection 
	 * DO NOT USE!
	 */
	public MailService() {
		instance = this;
	}

	/* Singleton */
	public static MailService getInstance() {
		if (instance == null)
			instance = new MailService();
		return instance;
	}
	
	
	/* Creation of data in database example */
	@RemoteService //Anotation to allow service to be accessible by POST request
	public void sendMail(SOAPMessage mail) {
		Emailer.getInstance().sendEmail("", "", "", null);
		//ExampleDto example = new ExampleDto(array.getJSONObject(0)); // Get the first JSONObject in the array (representing the example object) and construct the DTO by JSON representation
		//ExampleDto newExample = exampleDao.create(example); // Call the dao "create" function that return the ExampleDto created in database with the 'id' set
	}
}
