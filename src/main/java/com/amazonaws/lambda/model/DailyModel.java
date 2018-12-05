package com.amazonaws.lambda.model;

public class DailyModel {
	private int timeSlotID;
	private String date;
	private int calendarID;
	private int timeSlotStatus;
	private int meetingID;
	private String meetingName;
	private String meetingLocaion;
	private String meetingPerticipent;
	
	public DailyModel(int timeSlotID, String date, int calendarID, int timeSlotStatus, int meetingID,
			String meetingName, String meetingLocaion, String meetingPerticipent) {
		super();
		this.timeSlotID = timeSlotID;
		this.date = date;
		this.calendarID = calendarID;
		this.timeSlotStatus = timeSlotStatus;
		this.meetingID = meetingID;
		this.meetingName = meetingName;
		this.meetingLocaion = meetingLocaion;
		this.meetingPerticipent = meetingPerticipent;
	}
	public int getTimeSlotID() {
		return timeSlotID;
	}
	public void setTimeSlotID(int timeSlotID) {
		this.timeSlotID = timeSlotID;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getCalendarID() {
		return calendarID;
	}
	public void setCalendarID(int calendarID) {
		this.calendarID = calendarID;
	}
	public int getTimeSlotStatus() {
		return timeSlotStatus;
	}
	public void setTimeSlotStatus(int timeSlotStatus) {
		this.timeSlotStatus = timeSlotStatus;
	}
	public int getMeetingID() {
		return meetingID;
	}
	public void setMeetingID(int meetingID) {
		this.meetingID = meetingID;
	}
	public String getMeetingName() {
		return meetingName;
	}
	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}
	public String getMeetingLocaion() {
		return meetingLocaion;
	}
	public void setMeetingLocaion(String meetingLocaion) {
		this.meetingLocaion = meetingLocaion;
	}
	public String getMeetingPerticipent() {
		return meetingPerticipent;
	}
	public void setMeetingPerticipent(String meetingPerticipent) {
		this.meetingPerticipent = meetingPerticipent;
	}
	
}
