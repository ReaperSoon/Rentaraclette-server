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
@WebServlet("/messages")
public class Messages extends HttpServlet {
	private static final long 	serialVersionUID = 1L;

	public Messages() {
		super();
	}
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().println("<p>Salut tout le monde!</p>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = getReceivedText(request);
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
