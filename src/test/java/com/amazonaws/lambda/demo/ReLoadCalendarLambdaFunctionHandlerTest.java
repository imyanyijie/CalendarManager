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
public class ReLoadCalendarLambdaFunctionHandlerTest {

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
   public void testReLoadCalendarLambdaFunctionHandler() throws Exception {
       ReLoadCalendarLambdaFunctionHandler handler = new ReLoadCalendarLambdaFunctionHandler();
       JSONObject test = new JSONObject();
       JSONObject test2 = new JSONObject();
       setUp();
       test2.put("calendarID","1");
       test.put("queryStringParameters",test2.toJSONString());
       
       byte[] data = test.toString().getBytes();
		InputStream testInput = new ByteArrayInputStream(data);
		
		
		OutputStream testOutput = new ByteArrayOutputStream();
		
       handler.handleRequest(testInput, testOutput, testContext);
       Assert.assertNotNull(testOutput.toString());
   }
}
