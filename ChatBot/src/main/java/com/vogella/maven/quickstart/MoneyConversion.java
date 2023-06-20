package com.vogella.maven.quickstart;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.*;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import com.mashape.unirest.http.*;
import com.mashape.unirest.http.exceptions.UnirestException;

public class MoneyConversion 
{
    public static void main( String[] args ) throws UnirestException
    {
    	String from, to;
    	double amount;
    	Scanner scan = new Scanner(System.in);
    	System.out.print("What currency would you like to convert from: ");
    	from = scan.nextLine();
    	System.out.print("What currency would you like to convert to: ");
    	to = scan.nextLine();
    	System.out.print("Amount you would like to convert: ");
    	amount = scan.nextDouble();
    	JSONObject myJson = conversion(from, to, amount);
    	parse(myJson, amount);
    	scan.close();
    }
    
    public static JSONObject conversion(String from, String to, double amount) throws UnirestException {
    	com.mashape.unirest.http.HttpResponse<JsonNode> response = Unirest.get("https://currency-converter5.p.rapidapi.com/currency/convert?format=json&from=" + from + "&to=" + to +"&amount=" + amount)
    			.header("x-rapidapi-host", "currency-converter5.p.rapidapi.com")
    			.header("x-rapidapi-key", "2bd6ba9a8emshca6d5f735ab24e5p10f560jsnbfa32d62e701")
    			.asJson();
    	JSONObject myJson = response.getBody().getObject();
    	return myJson;
    }
    
    public static void parse(JSONObject myJson, double amount) {
    	int start = myJson.getJSONObject("rates").toString().indexOf("ount\":\"");
    	int end = myJson.getJSONObject("rates").toString().indexOf("\"}}");
    	double converted = Double.parseDouble(myJson.getJSONObject("rates").toString().substring(start+7,end-2));
    	start = myJson.getJSONObject("rates").toString().indexOf("ame");
    	end = myJson.getJSONObject("rates").toString().indexOf("\",\"rate");
    	String name = myJson.getJSONObject("rates").toString().substring(start+6,end);
    	System.out.printf("%.2f ", amount);
    	System.out.print(myJson.getString("base_currency_name") + "s converts to ");
    	System.out.printf("%.2f", converted);
    	System.out.println(" " + name + "s");
    }
}
