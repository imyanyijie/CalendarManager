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
public class AddDayHandlerTest {

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
	        testContext.setFunctionName("addDay");
	    }

  @Test
  public void testReLoadCalendarLambdaFunctionHandler() throws Exception {
	  AddDayHandler day = new AddDayHandler();
      JSONObject test = new JSONObject();
      JSONObject test2 = new JSONObject();
      test2.put("date","10-22-2018");
      test2.put("calendarID", "1");
      test.put("body",test2.toJSONString());
      
      setUp();
      byte[] data = test.toString().getBytes();
		InputStream testInput = new ByteArrayInputStream(data);
		
		
		OutputStream testOutput= new ByteArrayOutputStream();
		
      day.handleRequest(testInput, testOutput, testContext);
      Assert.assertNotNull(day.responseBody);
  }
}
