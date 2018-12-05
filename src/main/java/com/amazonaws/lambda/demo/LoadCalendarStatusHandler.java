package com.amazonaws.lambda.demo;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;

import com.amazonaws.lambda.logic.CalendarList;
import com.amazonaws.lambda.logic.timeSlotList;
import com.amazonaws.lambda.model.CalendarModel;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.google.gson.Gson;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class LoadCalendarStatusHandler implements RequestStreamHandler {
    JSONParser parser = new JSONParser();

	@Override
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

        LambdaLogger logger = context.getLogger();
        logger.log("Loading Java Lambda handler of ProxyWithStream");

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        JSONObject responseJson = new JSONObject();
        JSONObject responseBody = new JSONObject();
        String responseCode = "200";
        
        String val;
        Integer calendarID = null;
        CalendarModel calendar = null;
        JSONArray allTimeSlots;
        JSONArray meetings;

        try {
            JSONObject event = (JSONObject)parser.parse(reader);
            if (event.get("queryStringParameters") != null) {
                JSONObject qps = (JSONObject)event.get("queryStringParameters");
                if ( qps.get("calendarID") != null) {
                	val = (String) qps.get("calendarID");
                	calendarID = Integer.parseInt(val);
                } else {
                	// handle null calendarID
                	responseJson.put("statusCode", "300");
                }
            }
			try {
				calendar = CalendarList.getInstance().getCalendar(calendarID);
				Gson gson = new Gson();
				String calendar_asJson = gson.toJson(calendar);
				logger.log("Calendar iD is"+calendarID);
				logger.log("calendar as json"+calendar_asJson);
				timeSlotList.getInstance().createTimeSlot(calendarID, calendar.getStartDate(), calendar.getEndDate(), calendar.getStartHour(), calendar.getEndHouar(), calendar.getDuration());
				responseJson.put("statusCode", responseCode);
				
				responseBody.put("calendarID", String.valueOf(calendarID));
				responseBody.put("CalendarStatus", calendar_asJson);
				
			} catch(Exception ex) {
				responseJson.put("statusCode", 300);
				responseBody.put("exception", ex);
			}
			
            JSONObject headerJson = new JSONObject();
            headerJson.put("Content-Type", "application/json");
            headerJson.put("Access-Control-Allow-Origin", "*");
            headerJson.put("Access-Control-Allow-Method", "GET,POST");

            responseJson.put("isBase64Encoded", false);
            responseJson.put("headers", headerJson);
            responseJson.put("body", responseBody.toJSONString());

        } catch(ParseException pex) {
            responseJson.put("statusCode", "400");
            responseJson.put("exception", pex);
        }

        logger.log(responseJson.toJSONString());
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toJSONString());  
        writer.close();
    }
}