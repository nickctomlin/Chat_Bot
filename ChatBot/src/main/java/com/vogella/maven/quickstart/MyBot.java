package com.vogella.maven.quickstart;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.jibble.pircbot.*;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


public class MyBot extends PircBot{
	
	public static HttpURLConnection connection;
	
	public MyBot() {
		this.setName("chatbot");
	}
	
	
	
	public void onMessage(String channel, String sender, String login, String hostname, String message){
		if(message.equalsIgnoreCase("hi") || message.equalsIgnoreCase("Hey") || message.equalsIgnoreCase("Hello")) {
			sendMessage(channel, "Hi, welcome to pircbot! here you will be able to request weather in most cities around the world and convert currencies. \n");
			sendMessage(channel, "To find the weather simply type \"weather in *city/zip*\".\n");
			sendMessage(channel, "To convert currency the message must be in the form \"convert *amount* *currency* to *other currency*.\" Ex. \"convert 10 USD to MXN\"");
		}
		else if (message.equalsIgnoreCase("time")) {
			String time = new java.util.Date().toString();
			sendMessage(channel, sender + ": The time is now " + time);
		}
		else if(message.contains("weather")) {
			String sub = message.substring(message.lastIndexOf(" ") + 1);
			String weather = getInfo(sub);
			sendMessage(channel, sender + ": " + weather);
		}
		else if(message.contains("convert")) {
			String to = message.substring(message.lastIndexOf(" ") + 1);
			String from = message.substring(message.lastIndexOf(" ") - 6, message.lastIndexOf(" ") - 3);
			double amount;
			if(message.contains("from")) {
				amount = Double.parseDouble(message.substring(message.indexOf("from") + 5, message.indexOf(from)));
			}
			else {
				amount = Double.parseDouble(message.substring(message.indexOf("convert") + 8, message.indexOf(from) - 1));
			}
				try {
					sendMessage(channel, sender + ": " + conversion(from, to, amount));
				} catch (UnirestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		else if(message.equalsIgnoreCase("help")) {
			sendMessage(channel, "Try saying hi or hey.");
		}
	}
	
	public static String conversion(String from, String to, double amount) throws UnirestException {
    	com.mashape.unirest.http.HttpResponse<JsonNode> response = Unirest.get("https://currency-converter5.p.rapidapi.com/currency/convert?format=json&from=" + from + "&to=" + to +"&amount=" + amount)
    			.header("x-rapidapi-host", "currency-converter5.p.rapidapi.com")
    			.header("x-rapidapi-key", "2bd6ba9a8emshca6d5f735ab24e5p10f560jsnbfa32d62e701")
    			.asJson();
    	JSONObject myJson = response.getBody().getObject();
    	String last = parse(myJson, amount);
    	return last;
    }
    
    public static String parse(JSONObject myJson, double amount) {
    	int start = myJson.getJSONObject("rates").toString().indexOf("ount\":\"");
    	int end = myJson.getJSONObject("rates").toString().indexOf("\"}}");
    	double converted = Double.parseDouble(myJson.getJSONObject("rates").toString().substring(start+7,end-2));
    	start = myJson.getJSONObject("rates").toString().indexOf("ame");
    	end = myJson.getJSONObject("rates").toString().indexOf("\",\"rate");
    	String name = myJson.getJSONObject("rates").toString().substring(start+6,end);
    	String last = amount + " " + myJson.getString("base_currency_name") + "s converts to " + converted + " " + name + "s";
    	return last;
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
	
