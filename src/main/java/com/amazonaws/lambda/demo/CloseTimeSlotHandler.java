package com.amazonaws.lambda.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.lambda.db.DatabasePersistance;
import com.amazonaws.lambda.logic.timeSlotList;
import com.amazonaws.lambda.model.DailyModel;
import com.amazonaws.lambda.model.MeetingModel;
import com.amazonaws.lambda.model.aviliableTimeSlotModel;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

public class CloseTimeSlotHandler implements RequestStreamHandler {

	JSONParser parser = new JSONParser();
	private static LambdaLogger logger;
	@Override	
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
    	
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
			
			String date = null;
			String type = null;
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
				calendarID = Integer.parseInt((String) input_asJson.get("calendarID"));
				timeSlotID = Integer.parseInt((String) input_asJson.get("timeSlotID"));
				date = (String) input_asJson.get("date");
				type = (String) input_asJson.get("type");
			} else {
				responseJson = responseCreator("unsuccesful null value", 306, responseJson, responseBody);
			}
			
			try {
				timeSlotList.getInstance().closeTimeslot(calendarID, timeSlotID, date, type);

				responseBody.put("validation", "success");
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
