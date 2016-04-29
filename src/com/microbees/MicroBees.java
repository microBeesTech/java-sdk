package com.microbees;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;


public class MicroBees {
	MicroBees(Boolean debug){
		this.debug=debug;
	}
	private String token=null;
	private Boolean debug;
	public  String getAccessToken(String username,String password,String clientID,String clientSecret){
		String response ="";
		try {
			response = sendRequest("oauth/token?grant_type=password&username="+username+
					"&password="+password+"&client_id="+clientID+"&client_secret="+clientSecret,"");
			JSONObject responseObj = (JSONObject) JSONSerializer.toJSON(response);
			if(responseObj.has("access_token")){
				token = responseObj.getString("access_token");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = e.getMessage();
		}
		return response;
	}
	public String sendRequest(String url, String params) throws Exception {
		url = "https://dev.microbees.com/"+url;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setDoOutput(true);
        con.setDoInput(true);
		if(token!=null)
			con.setRequestProperty("Authorization", "Bearer "+token);
		if(params!=null){
			OutputStream os = con.getOutputStream();
			os.write(params.toString().getBytes("UTF-8"));
			os.close();
		}
		int responseCode = con.getResponseCode();
		if(debug){
			System.out.println("\nSending '"+"POST"+"' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
		}
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		if(debug){
			System.out.println("Response : " + response.toString());
		}
		return response.toString();
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Boolean getDebug() {
		return debug;
	}
	public void setDebug(Boolean debug) {
		this.debug = debug;
	}
}
