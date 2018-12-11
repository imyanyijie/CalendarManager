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
public class CloseDayHandlerTest {

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
		RemoveDayHandler calendar = new RemoveDayHandler();
		setUp();
		JSONObject test = new JSONObject();
		JSONObject test2 = new JSONObject();
		test2.put("date","10-22-2018");
		test2.put("calendarID","2");
		test.put("body",test2.toJSONString());
		
		
		
		
		byte[] data = test.toString().getBytes();
		InputStream testInput = new ByteArrayInputStream(data);

		//testInput.read(data);
		OutputStream testOutput = new ByteArrayOutputStream();
		//Context testContext = new Context();
		

		calendar.handleRequest(testInput, testOutput, testContext);
		//System.out.println(calendar.insertCal_asJson);
		assertNotNull(calendar.responseBody);
		//assertEquals("output should be same as input",test.toJSONString(),calendar.insertCal_asJson);
	}
}
