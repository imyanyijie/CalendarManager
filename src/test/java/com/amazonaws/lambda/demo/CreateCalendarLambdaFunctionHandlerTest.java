package com.amazonaws.lambda.demo;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.simple.JSONObject;
import org.junit.Test;

public class CreateCalendarLambdaFunctionHandlerTest {

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
		CreateCalendarLambdaFunctionHandler calendar = new CreateCalendarLambdaFunctionHandler();
		JSONObject test = new JSONObject();
		setUp();
		test.put("calendarName","personal");
		test.put("startDate","2018/11/29");
		test.put("endDate","2018/12/14");
		test.put("startHour","10");
		test.put("endHour","5");
		test.put("duration","20");
		
		byte[] data = test.toString().getBytes();
		InputStream testInput = new ByteArrayInputStream(data);

		//testInput.read(data);
		OutputStream testOutput = new ByteArrayOutputStream();
		//Context testContext = new Context();
		

		calendar.handleRequest(testInput, testOutput, testContext);
		//System.out.println(calendar.insertCal_asJson);
		assertNotNull(calendar.insertCal_asJson);
		//assertEquals("output should be same as input",test.toJSONString(),calendar.insertCal_asJson);
	}
}
