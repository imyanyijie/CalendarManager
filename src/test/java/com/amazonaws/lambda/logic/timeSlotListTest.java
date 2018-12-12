package com.amazonaws.lambda.logic;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class timeSlotListTest {

	@Test
	public void testCreateTimeSlot() throws Exception {
		timeSlotList.getInstance().createTimeSlot(1, "11-01-2018", "11-05-2018", 2, 4, 2);
		assertNotNull(timeSlotList.getInstance().getTimeslotByDay(1));
	}
	
	@Test
	public void testgetTimeslotByDay() throws Exception {
		List<String> dayList=	timeSlotList.getInstance().getTimeslotByDay(1);
		assertNotNull(dayList);
	}
	
	@Test
	public void testcloseTimeslot() throws Exception {
		timeSlotList.getInstance().closeTimeslot(1,2,"10-22-2018T10;30","closeTimeslot");
	}
	
	@Test
	public void testaddDaytoCalendar() throws Exception {
		timeSlotList.getInstance().addDaytoCalendar(3,"10-09-2018");
	}
	
	@Test
	public void testremoveDaytoCalendar() throws Exception {
		timeSlotList.getInstance().removeDaytoCalendar(1,"10-09-2018");
	}
}
