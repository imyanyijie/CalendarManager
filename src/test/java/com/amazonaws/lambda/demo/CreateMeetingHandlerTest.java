package com.amazonaws.lambda.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class CreateMeetingHandlerTest {

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
		CreateMeetingHandler meeting = new CreateMeetingHandler();
		JSONObject test = new JSONObject();
		setUp();
		test.put("meetingName","meeting1");
		test.put("meetingPerticipent","Mike");
		test.put("meetingLocaion","wpi");
		test.put("meetingDate","11/29/2018");
		test.put("calendarID","5");
		test.put("timeSlotID","20");
		
		byte[] data = test.toString().getBytes();
		InputStream testInput = new ByteArrayInputStream(data);

		//testInput.read(data);
		OutputStream testOutput = new ByteArrayOutputStream();
		//Context testContext = new Context();
		

		meeting.handleRequest(testInput, testOutput, testContext);
		System.out.println(meeting.insertCal_asJson);
		//assertNotNull(meeting.insertCal_asJson);
		//assertEquals("output should be same as input",test.toJSONString(),calendar.insertCal_asJson);
	}
  
}
