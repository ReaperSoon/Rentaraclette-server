package fr.rentaraclette.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SOAPMessage;

import fr.rentaraclette.rpc.RpcObject;
import fr.rentaraclette.rpc.ServiceException;
import fr.rentaraclette.rpc.ServicesLoader;
import fr.rentaraclette.services.AbstractService;
import fr.rentaraclette.util.Logger;
import fr.rentaraclette.util.Util;

/**
 * Servlet implementation class Services
 */
@WebServlet("/messages/*")
public class Messages extends HttpServlet {
	private static final long 	serialVersionUID = 1L;
	private ServicesLoader 		servicesLoader = ServicesLoader.getInstance();

	public Messages() {
		super();
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().println("<p>Salut tout le monde!</p>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Setting response context */
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		/* Get the request arguments (http://mywebsite.com/services/<className>/<functionName>) */
		String[] args = request.getRequestURI().substring(request.getContextPath().length()+1).split("/");

		/* Check if there is almost 3 arguments (services/<className>/<functionName>) */
		if (args.length >= 3) {
			String className = args[1];
			String methodeName = args[2];
			Logger.log(Logger.LOG, getClientIpAddr(request) + " ask for service " + className + "." + methodeName);

			try {
				/* Reading the received text from request */
				String received = getReceivedText(request);

				/* Create Object[] to give to the invoked method as argument (Object[] class is mandatoty by the fucking invoke method) */
				/*
				<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
				  <soapenv:Header>
				  </soapenv:Header>
				  <soapenv:Body>
				    <mail>
				      <to>julien.beguier@epitech.eu</to>
				      <subject>Mail subject</subject>
				      <mailType>rent-confirmation</mailType>
				      <variables>
				        <announce.title></announce.title>
				        <announce.url></announce.url>
				        <constumer.gender></constumer.gender>
				        <constumer.name></constumer.name>
				        <renter.address></renter.address>
				        <renter.gender></renter.gender>
				        <renter.message></renter.message>
				        <renter.name></renter.name>
				        <reservation.date></reservation.date>
				        <reservation.duration></reservation.duration>
				        <reservation.handle></reservation.handle>
				        <reservation.time></reservation.time>
				      </variables>
				    </mail>
				  </soapenv:Body>
				</soapenv:Envelope>
				 */

				SOAPMessage obj = Util.getSoapMessageFromString(received);
				Object[] serviceArgs = new Object[1];
				serviceArgs[0] = obj;

				/* 
				 * Get the RpcObject representing the service in the serviceLoader (get it with the key "className.methodName") 
				 * The RpcObject contains the service class as AbstractService and the Function of the specific service
				 */
				RpcObject serviceRpc = servicesLoader.getServices().get((className + "." + methodeName).toLowerCase());

				/* If the service does not exist */
				if (serviceRpc == null)
					throw new ServiceException("Service not found : " + className + "." + methodeName);

				/* Extract the AbstractService and the Method from the RpcObject */
				AbstractService service = serviceRpc.getService();
				Method method = service.getClass().getMethod(methodeName, SOAPMessage.class);

				/* Invoke the service and get the result (Usualy a JSONObject or null) */
				method.invoke(service, serviceArgs);

				out.println("{\"info\":{\"message\":\"email sent successfully\"}}");

			} catch (ServiceException e) {
				out.println("{\"error\":{\"message\":\"" + e.getMessage() + "\"}}");
			} catch (Exception e) {
				//out.println("{\"error\":{\"message\":\"" + "Internal error occured. Please contact website administrator." + "\"}}");
				out.println("{\"error\":{\"message\":\"" + e.getCause() + "\"}}");
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param request
	 * @return The text given by the client witch call the servlet
	 * @throws IOException
	 */
	private String getReceivedText(HttpServletRequest request) throws IOException {
		StringBuffer jb = new StringBuffer();
		String line = null;
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null)
			jb.append(line);

		return jb.toString();
	}

	/* Get the client IP address in the request header */
	public String getClientIpAddr(HttpServletRequest request) {  
		String ip = request.getHeader("X-Forwarded-For");  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("Proxy-Client-IP");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("WL-Proxy-Client-IP");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("HTTP_CLIENT_IP");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getRemoteAddr();  
		}  
		return ip;  
	}
}
