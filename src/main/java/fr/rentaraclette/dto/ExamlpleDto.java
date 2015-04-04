package fr.rentaraclette.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * @Entity is used to say hibernate this class can be transpose in DatabaseObject
 * @Table is use to make the link between the DTO and the table name in database
 */
@Entity
@Table(name="examples")
public class ExamlpleDto extends AbstractDto {

	private int				id;
	
	private String 			name;
	
	/* Public constructor is used by Hibernate to build empty instance and fill it with setters */
	public ExamlpleDto() {
		
	}
	
	/* This constructor is used to build a DTO from it JSON representation (e.g: received by a the client calling 'create' service */
	public ExamlpleDto(JSONObject obj) throws JSONException {
		if (obj.has("id"))
			setId(obj.getInt("id"));
		setName(obj.getString("name"));
	}

	/* 
	 * Set the is as id and AUTO_INCREMENT in database 
	 * The getters are called by Hibernate to tranpose the DTO as database object
	 * It also used by org.json to build JSONObject from an instance of class
	 */
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	public int getId() {
		return id;
	}

	/* The getters are called by Hibernate to build a DTO from a database object */
	public void setId(int id) {
		this.id = id;
	}

	/* The column annotation represents the corresponding column name of this variables in database */
	@Column(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}