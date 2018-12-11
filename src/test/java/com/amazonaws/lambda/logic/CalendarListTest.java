package com.amazonaws.lambda.logic;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.amazonaws.lambda.model.CalendarModel;

public class CalendarListTest {

	//CalendarList cL = new CalendarList();
	CalendarModel cM = new CalendarModel("11","1","1",1,1,1);
	@Test
	public void testAdd() throws Exception {
		CalendarList.getInstance().add(cM);
		List<CalendarModel> list =  CalendarList.getInstance().getCalendarList();
		assertNotNull(list);
	}

}
