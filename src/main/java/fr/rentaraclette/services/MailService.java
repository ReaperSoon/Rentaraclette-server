package fr.rentaraclette.services;

import java.util.HashMap;
import java.util.Map;

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
			String mailType = null;
			Map<String, String> vars = new HashMap<String, String>();

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
						else if (Emailer.SOAP_MAILTYPE.equals(node.getLocalName()))
							mailType = node.getTextContent();
						else if (Emailer.SOAP_VARIABLES.equals(node.getLocalName())) {
							nodeList = node.getChildNodes();
							for (int k = 0; k < nodeList.getLength(); k++) {
								node = nodeList.item(k);
								System.out.println(node.getLocalName() + ":" + node.getTextContent());
								vars.put(node.getLocalName(), node.getTextContent());
							}
						}
					}
					break;
				}
			}

			if (to != null && subject != null && mailType != null) {
				Emailer.getInstance().sendEmail(to, subject, mailType, vars);
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
