package com.amazonaws.lambda.db;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.amazonaws.lambda.model.CalendarModel;
import com.amazonaws.lambda.model.MeetingModel;
import com.amazonaws.lambda.model.aviliableTimeSlotModel;

public class DatabasePersistanceTest {

	DatabasePersistance test = new DatabasePersistance();
	@Test
	public void testgetCalendar() throws Exception {
		CalendarModel  cM = test.getCalendar(1);
		assertNull(cM);  
	}
	
	@Test
	public void testdeleteCalendar() throws Exception {
		boolean res = test.deleteCalendar(1);
		assertEquals(false,res);
	}
	
	@Test
	public void testaddMeeting() throws Exception {
		MeetingModel meeting = new MeetingModel(1,"meting1","wpi","mike","10-22-2017",1,9);
		boolean res = test.addMeeting(meeting);
		assertEquals(true,res);
	}
	
	@Test
	public void testremoveMeeting() {
		
		boolean res = test.removeMeeting(1);
		assertEquals(false,res);
	}
	
	@Test
	public void testgetAllMeetings() {
		List<MeetingModel> list = test.getAllMeetings(2);
		assertNotNull(list);		
	}
	
	@Test
	public void testaddTimeSlot() throws Exception {
		aviliableTimeSlotModel time = new aviliableTimeSlotModel(1,"10-22-1-2019",9,1);
		boolean res = test.addTimeSlot(2,time);
		assertEquals(true,res);
	}
	@Test 
	public void testRemoveTiemSlot() throws Exception {
		boolean res = test.removeTimeSlot(2);
				assertEquals(false,res);
	}
	
	@Test 
	public void testgetAllTimeslot() throws Exception {
		List<aviliableTimeSlotModel> timeSlotList = test.getAllTimeslot(3);
		assertNotNull(timeSlotList);
	}
	
	@Test
	public void testGetMeeting() {
		List<MeetingModel> MeetingModels = test.getMeetings(2, "2018", "02", "22");
		assertNotNull(MeetingModels);
	}
	
	@Test
	public void testsetTimeslotStatus() throws Exception {
		boolean res = test.setTimeslotStatus(1, 0);
		assertEquals(true,res);
	}
	
	@Test
	public void testgetTimeSlots() {
		List<aviliableTimeSlotModel> list = test.getTimeSlots(1,"2018","02","22");
		assertNotNull(list);
	}

}
