package fr.rentaraclette.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.json.JSONException;
import org.json.JSONObject;

@Entity
@Table(name="profils")
public class ProfileDto extends AbstractDto {

	private int				id;
	
	private String 			name;
	private String 			birthday;
	private String 			city;
	private String 			country;
	private String 			skills;
	private String 			cv_url;
	private String 			photo_url;
	private String			phone;
	
	public ProfileDto() {
		
	}
	
	public ProfileDto(JSONObject obj) throws JSONException {
		if (obj.has("id"))
			setId(obj.getInt("id"));
		setName(obj.getString("name"));
		setBirthday(obj.getString("birthday"));
		setCity(obj.getString("city"));
		setCountry(obj.getString("country"));
		setSkills(obj.getString("skills"));
		setCv_url(obj.getString("cv_url"));
		setPhoto_url(obj.getString("photo_url"));
		setPhone(obj.getString("phone"));
	}

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="birthday")
	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	@Column(name="city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name="country")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name="skills")
	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	@Column(name="cv_url")
	public String getCv_url() {
		return cv_url;
	}

	public void setCv_url(String cv_url) {
		this.cv_url = cv_url;
	}

	@Column(name="photo_url")
	public String getPhoto_url() {
		return photo_url;
	}

	public void setPhoto_url(String photo_url) {
		this.photo_url = photo_url;
	}

	@Column(name="phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}