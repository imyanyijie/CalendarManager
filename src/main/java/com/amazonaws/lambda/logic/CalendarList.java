package com.amazonaws.lambda.logic;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.lambda.db.DatabasePersistance;
import com.amazonaws.lambda.model.CalendarModel;

public class CalendarList {
	private List<CalendarModel> calendarList;
	private DatabasePersistance persistance;
	
	private static CalendarList _instance;
	
	public static CalendarList getInstance(){
		if(_instance == null) {
			_instance = new CalendarList();
		}
		return _instance; 
	}
	
	private CalendarList() {
		calendarList = new ArrayList<CalendarModel>();
		persistance = new DatabasePersistance();
	}

	
	public void add(CalendarModel cal) throws Exception {
		
		persistance.addCalendar(cal);
		calendarList.add(cal);
	}
	
	public CalendarModel remove(int CalendarID) throws Exception {
		persistance.deleteCalendar(CalendarID);
		CalendarModel result = calendarList.remove(CalendarID);
		return result;
	}
	
	
	public List<CalendarModel> getCalendarList() {
		calendarList = persistance.getAllCalendar();
		return calendarList;
	}

	public void setCalendarList(List<CalendarModel> calendarList) {
		this.calendarList = calendarList;
	}
	
	public CalendarModel getCalendar(int CalendarID) throws Exception {
		DatabasePersistance dbp = new DatabasePersistance();
		CalendarModel cal = dbp.getCalendar(CalendarID);
		return cal;
	}
	
}
