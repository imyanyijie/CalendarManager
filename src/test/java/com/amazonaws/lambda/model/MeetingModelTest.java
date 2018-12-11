package com.amazonaws.lambda.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class MeetingModelTest {

	MeetingModel test = new MeetingModel(1,"meting1","wpi","mike","10-22-2017",1,9);
	@Test
	public void testgetTimeSlotID() {
		assertNotNull(test.getTimeSlotID());
		assertNotNull(test.getMeetingID());
		assertNotNull(test.getMeetingName());
		assertNotNull(test.getMeetingPerticipent());
		assertNotNull(test.getMeetingDate());
		assertNotNull(test.getCalendarID());
	}
}
