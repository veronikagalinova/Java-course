package bg.sofia.uni.fmi.mjt.meetup.dto;

public class Event {
	private String id;
	private String name;
	private String status;
	private long duration;
	private Venue venue;
	private String description;
	
	@Override
	public String toString() {
		return "Event [id=" + id + ", name=" + name + ", status=" + status + ", duration=" + duration + ", venue="
				+ venue + ", description=" + description + "]";
	}

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public Venue getVenue() {
		return venue;
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
