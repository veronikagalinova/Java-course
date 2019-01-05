package bg.sofia.uni.fmi.mjt.meetup;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import bg.sofia.uni.fmi.mjt.meetup.dto.Event;
import bg.sofia.uni.fmi.mjt.meetup.dto.Photo;
import java.lang.reflect.Type;

public class MeetupClient {
	private HttpClient client;
	private String apiKey;

	public MeetupClient(HttpClient client, String apiKey) {
		this.client = client;
		this.apiKey = apiKey;
	}

	/**
	 * Fetches the nearby events.
	 * 
	 * @return
	 */
	public List<Event> getEventsNearby() {
		return findNearbyEvents();
	}

	/**
	 * Fetches the nearby events with the given venue name. The comparison is case
	 * insensitive.
	 * 
	 * @param venueName
	 *            - the event venue name
	 * @return
	 */
	public List<Event> getEventsWithVenueName(String venueName) {
		List<Event> result = findNearbyEvents();
		return result.stream()
				.filter(event -> event.getVenue() != null && event.getVenue().getName() != null
						&& event.getVenue().getName().toLowerCase().equals(venueName.toLowerCase()))
				.collect(Collectors.toList());

	}

	/**
	 * Fetches the nearby events whose descriptions contains all of the given
	 * keywords. The comparison is case insensitive.
	 * 
	 * @param keywords
	 *            - the keywords to search in the event description
	 * @return
	 */
	public List<Event> getEventsWithKeywords(Collection<String> keywords) {
		List<Event> result = findNearbyEvents();
		return result.stream().filter(event -> event.getDescription() != null)
				.filter(event -> containsAllKeywords(event, keywords))
				.collect(Collectors.toList());

	}

	/**
	 * Fetches the nearby event with max duration. Returns null when no events are
	 * found.
	 * 
	 * @return
	 */
	public Event getEventWithMaxDuration() {
		return findNearbyEvents().stream().max(Comparator.comparing(Event::getDuration)).orElse(null);
	}

	/**
	 * Fetches an event by group urlname and event id. Returns null in case of a
	 * missing event.
	 * 
	 * @param urlname
	 *            - the event group urlname
	 * @param id
	 *            - the event id
	 * @return
	 */
	public Event getEvent(String urlname, String id) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Fetches photos for the photo album with the given id. Returns null in case of
	 * a missing photo album.
	 * 
	 * @param urlname
	 *            - the photo album group urlname
	 * @param id
	 *            - the photo album id
	 * @return
	 */
	public List<Photo> getAlbumPhotos(String urlname, String id) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Performs a parallel download of the photos from the given photo album to the
	 * given target directory. A folder with the album id is being created in the
	 * target directory. The photos are downloaded in the newly created album
	 * directory. The file name of each photo is its id.
	 * 
	 * @param urlname
	 *            - the photo album group urlname
	 * @param albumId
	 *            - the photo album id
	 * @param target
	 *            - the target directory to save the photo album
	 * @throws IOException
	 */
	public void downloadPhotoAlbum(String urlname, String albumId, Path target) throws IOException {
		throw new UnsupportedOperationException();
	}

	private boolean containsAllKeywords(Event e, Collection<String> keywords) {
		return keywords.parallelStream().anyMatch(e.getDescription()::contains);
	}

	private List<Event> findNearbyEvents() {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.meetup.com/find/events?key=" + apiKey)).build();

		String nearbyEvents = null;
		try {
			nearbyEvents = client.send(request, BodyHandlers.ofString()).body();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Gson gson = new Gson();

		Type type = new TypeToken<List<Event>>() {
		}.getType();
		List<Event> result = gson.fromJson(nearbyEvents, type);
		return result;
	}
	
	public static void main(String[] args) {
		HttpClient client = HttpClient.newHttpClient();
		MeetupClient meetup = new MeetupClient(client, "5532537f4ba5879325f483e404d3b1d");
		List<Event> result = meetup.getEventsWithKeywords(List.of("Hi all"));
		for (Event e : result) {
			System.out.println(e);
		}
		System.out.println("Max duration: " + meetup.getEventWithMaxDuration().getDuration());
	}
}
