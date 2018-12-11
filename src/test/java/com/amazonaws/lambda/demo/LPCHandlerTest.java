package com.amazonaws.lambda.demo;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.lambda.model.MeetingModel;
import com.amazonaws.lambda.model.aviliableTimeSlotModel;
import com.amazonaws.services.lambda.runtime.Context;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class LPCHandlerTest {

	
    private TestContext testContext;
	
	 public void setUp() throws Exception {
	       // subject = new ExampleAwsLambdaHandler();  
	        testContext = new TestContext(){
	            // implement all methods of this interface and setup your test context. 
	            // For instance, the function name:
	            @Override
	            public String getFunctionName() {
	                return "ExampleAwsLambda";
	            }
	        };
	    }

   
	 @Test
		public void testHandleRequest() throws Exception {
			LPCHandler loadCalendar = new LPCHandler();
			JSONObject test = new JSONObject();
			JSONObject test2 = new JSONObject();
			setUp();
			test.put("calendarID","1");
			test.put("date","10000000");
			
			test2.put("queryStringParameters",test.toJSONString());
			
			byte[] data = test.toString().getBytes();
			InputStream testInput = new ByteArrayInputStream(data);

			//testInput.read(data);
			OutputStream testOutput = new ByteArrayOutputStream();
			//Context testContext = new Context();
			

			loadCalendar.handleRequest(testInput, testOutput, testContext);
			System.out.println(loadCalendar.responseJson);
			System.out.println(loadCalendar.responseBody);
			
		//	assertNotNull(loadCalendar.meetings);
			//assertEquals("output should be same as input",test.toJSONString(),calendar.insertCal_asJson);
		}
	 
	 @Test 
	 public void testgetMeetings() {
		 LPCHandler loadCalendar = new LPCHandler();
		 List<MeetingModel> meetings = loadCalendar.getMeetings(1, "2018","02","09");
		 assertNotNull(meetings);
	 }
	 
	 @Test
	 public void testgetTimeSlots() {
		 LPCHandler loadCalendar = new LPCHandler();
		 JSONArray JSONMeetings = new JSONArray();
		 JSONMeetings = loadCalendar.getTimeSlots(1,"2019","09","21");
		 assertNotNull(JSONMeetings);
	 }
	 
	 @Test
	 public void testgetTimeSlotsWithMeetings() {
		 LPCHandler loadCalendar = new LPCHandler();
		 JSONArray JSONTimeSlots = new JSONArray();
		 JSONTimeSlots  = loadCalendar.getTimeSlotsWithMeetings(2, "2018","09", "21");
		 assertNotNull(JSONTimeSlots );
	 }
}
