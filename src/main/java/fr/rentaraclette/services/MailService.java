package fr.rentaraclette.services;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	public void sendMail(SOAPMessage request) {
		try {
			String to = null;
			String subject = null;
			String content = null;

			SOAPBody body = request.getSOAPBody();
			NodeList nodeList = body.getChildNodes();
			Node node;

			for (int i = 0; i < nodeList.getLength(); i++) {
				node = nodeList.item(i);
				if (Emailer.SOAP_MAIL.equals(node.getLocalName())) {
					nodeList = node.getChildNodes();
					for (int j = 0; j < nodeList.getLength(); j++) {
						node = nodeList.item(j);
						System.out.println(node.getLocalName() + ":" + node.getTextContent());
						if (Emailer.SOAP_TO.equals(node.getLocalName()))
							to = node.getTextContent();
						else if (Emailer.SOAP_SUBJECT.equals(node.getLocalName()))
							subject = node.getTextContent();
						else if (Emailer.SOAP_CONTENT.equals(node.getLocalName()))
							content = node.getTextContent();
					}
					break;
				}
			}

			if (to != null && subject != null && content != null) {
				Emailer.getInstance().sendEmail(to, subject, content, null);
			} else {
				// Error SOAP
			}

		} catch (SOAPException e) {
			e.printStackTrace();
		}

		//ExampleDto example = new ExampleDto(array.getJSONObject(0)); // Get the first JSONObject in the array (representing the example object) and construct the DTO by JSON representation
		//ExampleDto newExample = exampleDao.create(example); // Call the dao "create" function that return the ExampleDto created in database with the 'id' set
	}
}
