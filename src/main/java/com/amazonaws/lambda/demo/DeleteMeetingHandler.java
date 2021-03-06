package com.amazonaws.lambda.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;

import com.amazonaws.lambda.db.DatabasePersistance;
import com.amazonaws.lambda.logic.CalendarList;
import com.amazonaws.lambda.model.CalendarModel;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class DeleteMeetingHandler implements RequestStreamHandler {
	JSONParser parser = new JSONParser();
	private final static JSONObject headerJson = new JSONObject();

	@Override
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

		LambdaLogger logger = context.getLogger();
		logger.log("Loading Java Lambda handler of ProxyWithStream\n");

		headerJson.put("Content-Type", "application/json");
		headerJson.put("Access-Control-Allow-Origin", "*");
		headerJson.put("Access-Control-Allow-Method", "GET,POST");

		JSONObject responseJson = new JSONObject();

		responseJson.put("isBase64Encoded", false);
		responseJson.put("headers", headerJson);

		JSONObject responseBody = new JSONObject();
		Integer responseCode = 200;

		String meetingID_String;
		String timeslotID_String;
		int meetingID = 0;
		int timeslotID = 0;
		Boolean deletionSuccess = false;

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		try {
			JSONObject event = (JSONObject) parser.parse(reader);
			logger.log("\nevent: " + event);
			
			if(event != null) {
				String body = (String) event.get("body");
				if (body != null) {
					event = (JSONObject) parser.parse(body);
				} else {
					// handle the empty input
					
				}
				meetingID_String = (String) event.get("meetingID");
				if(meetingID_String != null) {
					meetingID = Integer.parseInt(meetingID_String);
				}
				else {
					logger.log("the meetingID is empty");
				}

				logger.log("\nmeetingID: " + meetingID);
				
				timeslotID_String = (String) event.get("timeslotID");
				if(timeslotID_String != null) {
					timeslotID = Integer.parseInt(timeslotID_String);
				}
				else {
					logger.log("the timeslotID is empty");
				}
					
				try {
					
					DatabasePersistance dp = new DatabasePersistance();
					dp.setTimeslotStatus(timeslotID, 0);
					dp.removeMeeting(meetingID);
					responseBody.put("result", "Success");
					responseCode = 200;

//	                logger.log("Response Body: "+responseBody.toString()+"\n");
				} catch (Exception ex) {
					responseBody.put("result", "fail! no such meeting to delete");
					responseCode = 300;
					logger.log("deletion Failed");
				}

				logger.log("\n" + responseBody.toString() + "\n");
			}


		} catch (ParseException pex) {
			responseCode = 400;
		}
		
        responseJson.put("statusCode", responseCode);
        responseJson.put("body", responseBody.toString());

		logger.log(responseJson.toJSONString());
		OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
		writer.write(responseJson.toJSONString());
		writer.close();
	}
}
