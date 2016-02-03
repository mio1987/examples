/**
 * Represents merchant 
 * @author Michal Bosiak 
 */
package sk.mio1987.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Document(indexName = "merchant", type = "merchant", shards = 1, replicas = 0)
public class Merchant {
	
	// fields
	@Id
	private String id;
	private String name;
	private String description;
	private String address;
	@GeoPointField
	private GeoPoint location;
	
	
	// constructor
	public Merchant(){}
	
	public Merchant(String name, String description, String address, double latitude, double longitude){
		this.name = name;
		this.description = description;
		this.address = address;		
		this.location = new GeoPoint(latitude, longitude);
	}
	
	// getters and setters
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public double getLatitude() {
		return location.getLat();
	}	
	
	public double getLongitude() {
		return location.getLon();
	}
	

	public GeoPoint getLocation() {
		return location;
	}

	public void setLocation(GeoPoint location) {
		this.location = location;
	}
	
	
	
}
