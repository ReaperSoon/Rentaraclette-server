package fr.rentaraclette.rpc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContextEvent;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import fr.rentaraclette.services.AbstractService;
import fr.rentaraclette.util.Logger;

public class ServicesLoader extends Thread implements javax.servlet.ServletContextListener {

	private HashMap<String, RpcObject> services;
	private static ServicesLoader instance;

	public ServicesLoader() throws Exception {
		if (instance != null)
			throw (new Exception("ServiceLoader is already instanciated, please use ServiceLoader.getInstance()"));
	}

	public static ServicesLoader getInstance() {
		if (instance == null)
			try {
				instance = new ServicesLoader();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return instance;
	}

	@Override
	public void run() {
		/* Load all service in Thread to allow Tomcat to start faster */
		loadServices();
	}

	/* Get the package where to find all services classes */
	public String getServicesPackage() {
		JSONTokener tokener;
		String mypackage = null;
		try {
			tokener = new JSONTokener(this.getClass().getClassLoader().getResourceAsStream("config.json"));
			JSONObject root = new JSONObject(tokener);
			JSONObject ProjectConf = root.getJSONObject("PROJECT");
			mypackage = ProjectConf.getString("SERVICES.PACKAGE");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mypackage;
	}

	public void loadServices() {
		/* Build the RpcObject map with key "className.functionName" */
		services = new HashMap<String, RpcObject>();
		
		/* Use reflection to get a Set of Class that extends of AbstractService class */
		List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
		classLoadersList.add(ClasspathHelper.contextClassLoader());
		classLoadersList.add(ClasspathHelper.staticClassLoader());
		Reflections reflections = new Reflections(new ConfigurationBuilder()
		.setScanners(new SubTypesScanner(false), new ResourcesScanner())
		.setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
		.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(getServicesPackage()))));

		Set<Class<? extends AbstractService>> allClasses = reflections.getSubTypesOf(AbstractService.class);
		Logger.log(Logger.INFO, "Loading services : ");

		/* Start looping in all classes to find all 'RemoteService' inside */
		Long start = System.currentTimeMillis();
		try {
			for (Class<? extends AbstractService> klass : allClasses) {
				/* For all classes extending AbstractService */
				Method[] methodes = klass.getMethods(); // Get all methods of the class
				AbstractService instance = klass.newInstance(); // Create a new instance of the class (cannot use 'new' because of using AbstractService to represent the service instead of the classname)
				
				/* For all methods of each class extending AbstractService */
				for (Method method : methodes) {
					/* Check if the method is annoted with the '@RemoteService' (if not the service will be use only localy, not by clients) */
					if (method.isAnnotationPresent(RemoteService.class)) {
						services.put((klass.getSimpleName() + "." + method.getName()).toLowerCase(), new RpcObject(instance, method));
						Logger.log(Logger.INFO, klass.getSimpleName() + "." + method.getName());
					}
				}
			}
			Long elapsed = System.currentTimeMillis() - start;
			Logger.log(Logger.INFO, "Loaded " + services.size() + " service in " + elapsed + "ms");
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public HashMap<String, RpcObject> getServices() {
		return services;
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		instance = this;
		this.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}
}
