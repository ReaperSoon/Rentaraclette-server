package fr.rentaraclette.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import fr.rentaraclette.rpc.RpcObject;
import fr.rentaraclette.rpc.ServiceException;
import fr.rentaraclette.rpc.ServicesLoader;
import fr.rentaraclette.services.AbstractService;
import fr.rentaraclette.util.Logger;

/**
 * Servlet implementation class Services
 */
@WebServlet("/services/*")
public class Services extends HttpServlet implements javax.servlet.ServletContextListener {
	private static final long 	serialVersionUID = 1L;
	private ServicesLoader 		servicesLoader = ServicesLoader.getInstance();

	public Services() {
		super();
	}
	
	public void initialize() {
		Logger.info("ServiceAPI is ready!");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//For page direct access, write on response.getWriter();
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
				JSONArray obj = new JSONArray(received);
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
				Method method = service.getClass().getMethod(methodeName, JSONArray.class);
				
				/* Invoke the service and get the result (Usualy a JSONObject or null) */
				Object invokeResult = method.invoke(service, serviceArgs);
				
				/* Build the JSONObject for the response */
				JSONObject ret = new JSONObject();
				
				/* If the result if not null put it on the JSONObject for the response with the key "success" */
				if (invokeResult != null) {
					ret.put("success", invokeResult);
					out.print(ret.toString());
				/* If response is null, just put "success" at the key "success" */
				}else {
					ret.put("success", "success");
					out.print(ret.toString());
				}
				
				/* Flush the response */
				out.flush();
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
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		initialize();
	}
}
