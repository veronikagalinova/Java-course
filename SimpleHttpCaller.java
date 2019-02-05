import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class SimpleHttpCaller {

    public static void main(String[] args) {
        
    	HttpClient client = HttpClient.newHttpClient();
    	
    	String apiKey = "8A0wDyAusd3yNtqfdRqfTg7ZpNLj01BuLsx7NEyZ";
    	String URL = "https://api.nal.usda.gov/ndb/search/?q=nutella&api_key=" + apiKey;
    	
    	HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL)).build();
    	
    	try {
			String jsonResponse = client.send(request, BodyHandlers.ofString()).body();
			Gson gson = new Gson();
			Result result = gson.fromJson(jsonResponse, new TypeToken<Result>() {
			}.getType());
			
			result.display();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    	
    }
}
