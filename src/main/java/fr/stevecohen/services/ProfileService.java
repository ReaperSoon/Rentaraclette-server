package fr.stevecohen.services;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.stevecohen.dao.ProfileDao;
import fr.stevecohen.dto.ProfileDto;
import fr.stevecohen.rpc.RemoteService;

public class ProfileService extends AbstractService {
	
	private ProfileDao 				profileDao = ProfileDao.getInstance();
	private static ProfileService 	instance = null;
	
	public ProfileService() {
		instance = this;
	}

	public static ProfileService getInstance() {
		if (instance == null)
			instance = new ProfileService();
		return instance;
	}
	
	@RemoteService
	public JSONObject createProfile(JSONArray array) throws JSONException {
		ProfileDto profile = new ProfileDto(array.getJSONObject(0));
		ProfileDto newProfile = profileDao.create(profile);
		return new JSONObject(newProfile);
	}
	
	@RemoteService
	public JSONObject updateProfile(JSONArray array) throws JSONException {
		ProfileDto profile = new ProfileDto(array.getJSONObject(0));
		ProfileDto updatedProfile = profileDao.update(profile);
		return new JSONObject(updatedProfile);
	}
	
	@RemoteService
	public JSONObject getAllProfiles(JSONArray array) throws JSONException {
		List<ProfileDto> profils = profileDao.selectAll();
		JSONArray profilsArray = new JSONArray();
		for (ProfileDto profil : profils)
			profilsArray.put(profil);
		JSONObject container = new JSONObject();
		container.put("profils", profils);
		return container;
	}
	
	@RemoteService
	public JSONObject getProfileById(JSONArray array) throws JSONException {
		ProfileDto profile = profileDao.selectById(array.optInt(0));
		return new JSONObject(profile);
	}
	
	@RemoteService
	public JSONObject searchProfilesByName(JSONArray array) throws JSONException {
		List<ProfileDto> profils = profileDao.selectByName(array.optString(0));
		JSONArray profilsArray = new JSONArray();
		for (ProfileDto profil : profils)
			profilsArray.put(profil);
		JSONObject container = new JSONObject();
		container.put("profils", profils);
		return container;
	}
	
	@RemoteService
	public JSONObject searchProfilesByCity(JSONArray array) throws JSONException {
		List<ProfileDto> profils = profileDao.selectByCity(array.optString(0));
		JSONArray profilsArray = new JSONArray();
		for (ProfileDto profil : profils)
			profilsArray.put(profil);
		JSONObject container = new JSONObject();
		container.put("profils", profils);
		return container;
	}
	
	@RemoteService
	public JSONObject searchProfilesByCountry(JSONArray array) throws JSONException {
		List<ProfileDto> profils = profileDao.selectByCountry(array.optString(0));
		JSONArray profilsArray = new JSONArray();
		for (ProfileDto profil : profils)
			profilsArray.put(profil);
		JSONObject container = new JSONObject();
		container.put("profils", profils);
		return container;
	}
	
	@RemoteService
	public JSONObject searchProfilesByPhone(JSONArray array) throws JSONException {
		List<ProfileDto> profils = profileDao.selectByPhone(array.optString(0));
		JSONArray profilsArray = new JSONArray();
		for (ProfileDto profil : profils)
			profilsArray.put(profil);
		JSONObject container = new JSONObject();
		container.put("profils", profils);
		return container;
	}
	
	@RemoteService
	public JSONObject searchProfilesBySkills(JSONArray array) throws JSONException {
		String[] skills = new String[array.length()];
		for (int i = 0; i < array.length(); i++) {
			skills[i] = array.optString(i);
		}
		List<ProfileDto> profils = profileDao.selectBySkills(skills);
		JSONArray profilsArray = new JSONArray();
		for (ProfileDto profil : profils)
			profilsArray.put(profil);
		JSONObject container = new JSONObject();
		container.put("profils", profils);
		return container;
	}
	
	@RemoteService
	public void removeProfile(JSONArray array) throws JSONException {
		ProfileDto profileDto = new ProfileDto(array.getJSONObject(0));
		profileDao.delete(profileDto);
	}
}
