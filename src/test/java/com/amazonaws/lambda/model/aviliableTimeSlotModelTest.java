package com.amazonaws.lambda.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class aviliableTimeSlotModelTest {

	aviliableTimeSlotModel test = new aviliableTimeSlotModel(1,"11-19-2018",2,1);
	@Test
	public void testGetID() {
		assertNotNull(test.getTimeSlotID());
		assertNotNull(test.getDate());
		assertNotNull(test.getCalendarID());
		assertNotNull(test.getTimeSlotStatus());
	
	}

}
