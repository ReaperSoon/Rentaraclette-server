package fr.rentaraclette.services;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.rentaraclette.dao.ExampleDao;
import fr.rentaraclette.dto.ExampleDto;
import fr.rentaraclette.rpc.RemoteService;

public class ExampleService extends AbstractService {
	
	private ExampleDao 				exampleDao = ExampleDao.getInstance();
	private static ExampleService 	instance = null;
	
	/* 
	 * Public constructor because of the Java Reflection 
	 * DO NOT USE!
	 */
	public ExampleService() {
		instance = this;
	}

	/* Singleton */
	public static ExampleService getInstance() {
		if (instance == null)
			instance = new ExampleService();
		return instance;
	}
	
	
	/* Creation of data in database example */
	@RemoteService //Anotation to allow service to be accessible by POST request
	public JSONObject createExample(JSONArray array) throws JSONException {
		ExampleDto example = new ExampleDto(array.getJSONObject(0)); // Get the first JSONObject in the array (representing the example object) and construct the DTO by JSON representation
		ExampleDto newExample = exampleDao.create(example); // Call the dao "create" function that return the ExampleDto created in database with the 'id' set
		return new JSONObject(newExample); // Return JSON representation of the new ExampleDTO to the client
	}
	
	/* Update Example */
	@RemoteService
	public JSONObject updateExample(JSONArray array) throws JSONException {
		ExampleDto example = new ExampleDto(array.getJSONObject(0));
		ExampleDto updatedExample = exampleDao.update(example);
		return new JSONObject(updatedExample);
	}
	
	/* Select all Example objects from the database */
	@RemoteService
	public JSONObject getAllExample(JSONArray array) throws JSONException {
		List<ExampleDto> examples = exampleDao.selectAll();
		JSONArray profilsArray = new JSONArray(); // Create the JSONArray that will contains all ExampleDTO
		for (ExampleDto example : examples)
			profilsArray.put(example); // Put each ExampleDto in the array (that will automatically be converted in JSONObject)
		JSONObject container = new JSONObject();
		container.put("examples", examples); // Put the array in the JSONObject for the response
		return container;
	}
	
	/* Select the Example by id */
	@RemoteService
	public JSONObject getExampleById(JSONArray array) throws JSONException {
		ExampleDto example = exampleDao.selectById(array.optInt(0));
		return new JSONObject(example);
	}
	
	/* Select with filter ( SELECT * FROM examples WHERE 'name' = ... )*/
	@RemoteService
	public JSONObject searchExamplesByName(JSONArray array) throws JSONException {
		List<ExampleDto> examples = exampleDao.selectByName(array.optString(0)); // Get all matching results for the filter
		JSONArray ExamplesArray = new JSONArray();
		for (ExampleDto example : examples)
			ExamplesArray.put(example);
		JSONObject container = new JSONObject();
		container.put("examples", examples);
		return container;
	}
	
	/* Select with multiple filters */
	@RemoteService
	public JSONObject searchExamplesByMultipleFilter(JSONArray array) throws JSONException {
		String[] filters = new String[array.length()];
		for (int i = 0; i < array.length(); i++) { // Put all filters in a String array
			filters[i] = array.optString(i);
		}
		List<ExampleDto> examples = exampleDao.selectByMultipleFilter(filters); //Get all matching results for these filters
		JSONArray examplesArray = new JSONArray();
		for (ExampleDto example : examples)
			examplesArray.put(example);
		JSONObject container = new JSONObject();
		container.put("examples", examples);
		return container;
	}
	
	/* Delete 1 example object in database */
	@RemoteService
	public void removeExample(JSONArray array) throws JSONException {
		ExampleDto exampleDto = new ExampleDto(array.getJSONObject(0));
		exampleDao.delete(exampleDto);
	}
}
