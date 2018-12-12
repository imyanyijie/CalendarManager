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
public class DeleteMeetingHandlerTest {

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
		DeleteMeetingHandler deleteMeeting = new DeleteMeetingHandler();
		JSONObject test = new JSONObject();
		setUp();
		test.put("meetingID","1");
		test.put("timeslotID","2");
		
		byte[] data = test.toString().getBytes();
		InputStream testInput = new ByteArrayInputStream(data);

		//testInput.read(data);
		OutputStream testOutput =new ByteArrayOutputStream();
		//Context testContext = new Context();
		
		
		deleteMeeting.handleRequest(testInput, testOutput, testContext);
		System.out.println(testOutput.toString());
		//assertNotNull(meeting.insertCal_asJson);
		//assertEquals("output should be same as input",test.toJSONString(),calendar.insertCal_asJson);
	}
}
