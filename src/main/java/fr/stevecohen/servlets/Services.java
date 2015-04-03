package fr.stevecohen.servlets;

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

import fr.stevecohen.rpc.RpcObject;
import fr.stevecohen.rpc.ServiceException;
import fr.stevecohen.rpc.ServicesLoader;
import fr.stevecohen.services.AbstractService;
import fr.stevecohen.util.Logger;

/**
 * Servlet implementation class Services
 */
@WebServlet("/services/*")
public class Services extends HttpServlet implements javax.servlet.ServletContextListener {
	private static final long 	serialVersionUID = 1L;
	private ServicesLoader 		servicesLoader = ServicesLoader.getInstance();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Services() {
		super();
	}
	
	public void initialize() {
		Logger.info("ServiceAPI is ready!");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String[] args = request.getRequestURI().substring(request.getContextPath().length()+1).split("/");
		
		if (args.length >= 3) {
			String className = args[1];
			String methodeName = args[2];
			
			Logger.log(Logger.LOG, getClientIpAddr(request) + " ask for service " + className + "." + methodeName);
			
			try {
				/* Lecture JSON */
				StringBuffer jb = new StringBuffer();
				String line = null;
				BufferedReader reader = request.getReader();
				while ((line = reader.readLine()) != null)
					jb.append(line);
				
				/* Creation argument avec JSON */
				JSONArray obj = new JSONArray(jb.toString());
				Object[] serviceArgs = new Object[1];
				serviceArgs[0] = obj;
				
				RpcObject serviceRpc = servicesLoader.getServices().get((className + "." + methodeName).toLowerCase());
				if (serviceRpc == null)
					throw new ServiceException("Service not found : " + className + "." + methodeName);
				AbstractService service = serviceRpc.getService();
				Method method = service.getClass().getMethod(methodeName, JSONArray.class);
				Object invokeResult = method.invoke(service, serviceArgs);
				JSONObject ret = new JSONObject();
				if (invokeResult != null) {
					ret.put("success", invokeResult);
					out.print(ret.toString());
				}else {
					ret.put("success", "success");
					out.print(ret.toString());
				}
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
