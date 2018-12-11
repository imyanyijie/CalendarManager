package com.amazonaws.lambda.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class DailyModelTest {

	DailyModel test = new DailyModel(1,"10-28-2017",1,0,12,"meeting1","wpi","Mike");
	@Test
	public void testGetID() {
		assertNotNull(test.getTimeSlotID());
		assertNotNull(test.getDate());
		assertNotNull(test.getCalendarID());
		assertNotNull(test.getTimeSlotStatus());
		assertNotNull(test.getMeetingID());
		assertNotNull(test.getMeetingName());
		assertNotNull(test.getMeetingLocaion());
		assertNotNull(test.getMeetingPerticipent());
	}

}
