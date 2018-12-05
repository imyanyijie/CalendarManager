package com.amazonaws.lambda.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.io.BufferedReader;

import com.amazonaws.lambda.db.DatabasePersistance;
import com.amazonaws.lambda.logic.timeSlotList;
import com.amazonaws.lambda.model.CalendarModel;
import com.amazonaws.lambda.model.DailyModel;
import com.amazonaws.lambda.model.MeetingModel;
import com.amazonaws.lambda.model.aviliableTimeSlotModel;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class LPCHandler implements RequestStreamHandler {
	JSONParser parser = new JSONParser();

	@SuppressWarnings("unchecked")
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

		LambdaLogger logger = context.getLogger();
		logger.log("Loading Java Lambda handler of ProxyWithStream\n");

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		JSONObject responseJson = new JSONObject();
		JSONObject responseBody = new JSONObject();
		String responseCode = "200";

		String val;
		Integer calendarID = null;
		String date = null;
		JSONArray meetings = null;
		JSONArray timeSlots = null;
		JSONArray timeSlotsWithMeetings = null;

		try {
			JSONObject event = (JSONObject) parser.parse(reader);
			if (event.get("queryStringParameters") != null) {
				JSONObject qps = (JSONObject) event.get("queryStringParameters");
				if (qps.get("calendarID") != null) {
					val = (String) qps.get("calendarID");
					calendarID = Integer.parseInt(val);
				} else {
					// handle null calendarID
				}
				if (qps.get("date") != null) {
					date = (String) qps.get("date");
				} else {
					// handle null time
				}
			}

			String year = date.substring(0, 4);
			String month = date.substring(4, 6);
			String day = date.substring(6, 8);

			// day 00 means get info of a month, otherwise get info of a day.
			if (day.equals("00")) {
				meetings = getMeetings(calendarID, year, month, day);
				List<String> dayList = timeSlotList.getInstance().getTimeslotByDay(calendarID);
				responseBody.put("meetings", meetings);
				responseBody.put("day", dayList);

			} else {
				timeSlotsWithMeetings = getTimeSlotsWithMeetings(calendarID, year, month, day);
				responseBody.put("timeSlotsWithMeetings", timeSlotsWithMeetings);
			}

			JSONObject headerJson = new JSONObject();
			headerJson.put("Content-Type", "application/json");
			headerJson.put("Access-Control-Allow-Origin", "*");
			headerJson.put("Access-Control-Allow-Method", "GET,POST");

			responseJson.put("isBase64Encoded", false);
			responseJson.put("statusCode", responseCode);
			responseJson.put("headers", headerJson);
			responseJson.put("body", responseBody.toJSONString());

		} catch (ParseException pex) {
			responseJson.put("statusCode", "500");
			responseJson.put("exception", pex);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			responseJson.put("statusCode", "500");
			responseJson.put("exception", e);
		}

		logger.log(responseJson.toJSONString());
		OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
		writer.write(responseJson.toJSONString());
		writer.close();
	}

	private JSONArray getMeetings(int calendarID, String year, String month, String day) {
		DatabasePersistance dbp = new DatabasePersistance();
		List<MeetingModel> meetings = dbp.getMeetings(calendarID, year, month, day);

		JSONArray JSONMeetings = new JSONArray();
		for (int i = 0; i < meetings.size(); ++i) {
			MeetingModel meeting = meetings.get(i);
			JSONObject mt = new JSONObject();
			mt.put("meetingID", meeting.getTimeSlotID());
			mt.put("meetingName", meeting.getMeetingName());
			mt.put("meetingLocaion", meeting.getMeetingLocaion());
			mt.put("meetingPerticipent", meeting.getMeetingPerticipent());
			mt.put("meetingDate", meeting.getMeetingDate());
			mt.put("timeSlotID", meeting.getTimeSlotID());
			mt.put("calendarID", meeting.getCalendarID());

			JSONMeetings.add(mt);
		}
		return JSONMeetings;
	}

	private JSONArray getTimeSlots(int calendarID, String year, String month, String day) {
		DatabasePersistance dbp = new DatabasePersistance();
		List<aviliableTimeSlotModel> timeSlots = dbp.getTimeSlots(calendarID, year, month, day);

		JSONArray JSONMeetings = new JSONArray();
		for (int i = 0; i < timeSlots.size(); ++i) {
			aviliableTimeSlotModel timeSlot = timeSlots.get(i);
			JSONObject ts = new JSONObject();
			ts.put("timeSlotID", timeSlot.getTimeSlotID());
			ts.put("date", timeSlot.getDate());
			ts.put("calendarID", timeSlot.getCalendarID());
			ts.put("timeSlotStatus", timeSlot.getTimeSlotStatus());

			JSONMeetings.add(ts);
		}
		return JSONMeetings;
	}

	private JSONArray getTimeSlotsWithMeetings(int calendarID, String year, String month, String day) {
		DatabasePersistance dbp = new DatabasePersistance();
		List<aviliableTimeSlotModel> timeSlots = dbp.getTimeSlots(calendarID, year, month, day);
		List<MeetingModel> meetings = dbp.getMeetings(calendarID, year, month, day);
		List<DailyModel> dailyList = new ArrayList<DailyModel>();
		JSONArray JSONTimeSlots = new JSONArray();

		for (aviliableTimeSlotModel timeslot : timeSlots) {
			JSONObject ts = new JSONObject();
			int timeslotID = timeslot.getTimeSlotID();
			String date = timeslot.getDate();
			int calID = timeslot.getCalendarID();
			int timeslotStatus = timeslot.getTimeSlotStatus();
			int meetingID = 0;
			String meetingName = null;
			String meetingLocaion = null;
			String meetingPerticipent = null;
			for (MeetingModel meeting : meetings) {
				if (meeting.getTimeSlotID() == timeslotID) {
					meetingID = meeting.getMeetingID();
					meetingName = meeting.getMeetingName();
					meetingLocaion = meeting.getMeetingLocaion();
					meetingPerticipent = meeting.getMeetingPerticipent();
				}
			}
			ts.put("timeslotID", timeslotID);
			ts.put("date", date);
			ts.put("calendarID", calID);
			ts.put("timeslotStatus", timeslotStatus);
			ts.put("meetingID", meetingID);
			ts.put("meetingName", meetingName);
			ts.put("meetingLocaion", meetingLocaion);
			ts.put("meetingPerticipent", meetingPerticipent);
			JSONTimeSlots.add(ts);
		}

		return JSONTimeSlots;
	}

}