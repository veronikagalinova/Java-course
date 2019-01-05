package bg.sofia.uni.fmi.mjt.meetup.dto;

public class Venue {
	private long id;
	private String name;
	private String address;
	private String city;
	private String country;
	
	@Override
	public String toString() {
		return "Venue [id=" + id + ", name=" + name + ", address=" + address + ", city=" + city + ", country=" + country
				+ "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
