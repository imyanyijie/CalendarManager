package com.amazonaws.lambda.demo;

import static org.junit.Assert.assertNotNull;

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
public class CloseTimeSlotHandlerTest {

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
		CloseTimeSlotHandler calendar = new CloseTimeSlotHandler();
		JSONObject test = new JSONObject();
		JSONObject test2 = new JSONObject();
		setUp();
		test.put("calendarID","1");
		test.put("timeSlotID","2");
		test.put("date","10");
		test.put("type","00");
		
		test2.put("body","test.toJSONString()");
		
		byte[] data = test2.toString().getBytes();
		InputStream testInput = new ByteArrayInputStream(data);

		//testInput.read(data);
		OutputStream testOutput = new ByteArrayOutputStream();
		//Context testContext = new Context();
		

		calendar.handleRequest(testInput, testOutput, testContext);
		//System.out.println(calendar.insertCal_asJson);
		assertNotNull(testOutput.toString());
		//assertEquals("output should be same as input",test.toJSONString(),calendar.insertCal_asJson);
	}
}
