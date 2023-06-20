package com.vogella.maven.quickstart;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;
import org.json.*;


public class weatherAPI {
	
	public static HttpURLConnection connection;
	//public static String holder;
	
	public static void main(String[] args) throws MalformedURLException, JSONException{
		String line;
		System.out.print("Please enter city or zip code: ");
		Scanner scan = new Scanner(System.in);
		String holder = scan.nextLine();
		line = getInfo(holder);
		System.out.println(line);
		scan.close();
	}
	
	public static String getInfo(String location) {
		BufferedReader reader;
		StringBuffer Content = new StringBuffer();
		String line;
		URL url;
		try {
			url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + location + "&units=imperial&appid=39848748755f3ccef6d6d78b8cf84619");
			
			connection = (HttpURLConnection) url.openConnection();
			
			//Request Setup
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			
			int status = connection.getResponseCode();
			
			
			if(status > 299) {
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				while((line = reader.readLine()) != null) {
					Content.append(line);
				}
				reader.close();
			}
			else {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while((line = reader.readLine()) != null) {
					Content.append(line);
			}
				reader.close();
			}
			JSONObject weatherObj = new JSONObject(Content.toString());
			String temp = parse(weatherObj, location);
			return temp;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		finally {
			connection.disconnect();
		}
		return "";
	}
	
	public static String parse(JSONObject weatherObj, String location) throws JSONException {
		int begin = weatherObj.getJSONArray("weather").toString().indexOf("ption\":\"");
		int end = weatherObj.getJSONArray("weather").toString().indexOf("\",\"main");
		String temp = (weatherObj.getJSONArray("weather").toString().substring(begin + 8, end) + ", and " + weatherObj.getJSONObject("main").get("temp") + " today in " + location 
				+ " with a high of " + weatherObj.getJSONObject("main").get("temp_max") + " and a low of " + weatherObj.getJSONObject("main").get("temp_min"));
		return temp;
	}
}
