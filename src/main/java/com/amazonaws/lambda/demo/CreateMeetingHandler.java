package com.amazonaws.lambda.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.amazonaws.lambda.db.DatabasePersistance;
import com.amazonaws.lambda.model.MeetingModel;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

public class CreateMeetingHandler implements RequestStreamHandler {
	private static LambdaLogger logger;
	private final static JSONParser parser = new JSONParser();
	private final static JSONObject handerJson = new JSONObject();
    
    public void handleRequest(InputStream inputStream,OutputStream outputStream,Context context) throws IOException{
    	
    	logger = context.getLogger();
    	JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type","application/json");
		headerJson.put("Access-Control-Allow-Origin","*");
		headerJson.put("Access-Control-Allow-Methods","GET,POST");
		JSONObject responseBody = new JSONObject();
		
		JSONObject responseJson = new JSONObject();
		responseJson.put("isBase64Encoded", false);
		responseJson.put("headers", headerJson);
		try {
			
			String meetingName = null;
			String meetingPerticipent = null;
			String meetingLocaion = null;
			String meetingDate = null;
			//String startTime = null;
			int calendarID = -1;
			int timeSlotID = -1;
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			JSONObject input_asJson = (JSONObject) parser.parse(reader);
			
			String body = (String) input_asJson.get("body");
			if (body != null) {
				input_asJson = (JSONObject) parser.parse(body);
			} else {
				// handle the empty input
				responseJson = responseCreator("unsuccesful null input", 301, responseJson, responseBody);
			}
			
			if (input_asJson != null) {
				meetingName = (String) input_asJson.get("meetingName");
				meetingDate = (String) input_asJson.get("meetingDate");
				meetingPerticipent = (String) input_asJson.get("meetingPerticipent");
				calendarID = Integer.parseInt((String) input_asJson.get("calendarID"));
				timeSlotID = Integer.parseInt((String) input_asJson.get("timeSlotID"));
				meetingLocaion = (String) input_asJson.get("meetingLocaion");
			} else {
				responseJson = responseCreator("unsuccesful null value", 306, responseJson, responseBody);
			}
			
			try {
				
				MeetingModel meetingModel = new MeetingModel(meetingName, meetingLocaion, meetingPerticipent, meetingDate ,timeSlotID,calendarID);
				DatabasePersistance persistance = new DatabasePersistance();
				persistance.addMeeting(meetingModel);
				persistance.setTimeslotStatus(timeSlotID, 1);
				String insertCal_asJson = new Gson().toJson(meetingModel);
				responseBody.put("validation", insertCal_asJson);
				responseJson = responseCreator("succesful！！", 200, responseJson, responseBody);
			} catch (Exception ex) {
				responseJson = responseCreator("unsuccesful unable to add to model", 300, responseJson, responseBody);
			}
		}catch(Exception e) {
			logger.log(e.toString());
		}
		OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
		writer.write(responseJson.toJSONString());  
        writer.close();
    }
    
    private JSONObject responseCreator(String result, int statusCode, JSONObject responseJson,
			JSONObject responseBody) {
		responseBody.put("result", result);
		responseJson.put("statusCode", statusCode);
		responseJson.put("body", responseBody.toString());
		return responseJson;
	}

}
