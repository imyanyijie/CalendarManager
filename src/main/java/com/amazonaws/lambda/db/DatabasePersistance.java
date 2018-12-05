package com.amazonaws.lambda.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.lambda.logic.CalendarList;
import com.amazonaws.lambda.model.CalendarModel;
import com.amazonaws.lambda.model.MeetingModel;
import com.amazonaws.lambda.model.aviliableTimeSlotModel;

public class DatabasePersistance {
	private Connection conn;

	// make initial connection
	public DatabasePersistance() {
		try {
			conn = DatabaseManage.connect();
		} catch (Exception e) {
			conn = null;
			e.printStackTrace();
		}
	}

	// get a single calendar using ID
	public CalendarModel getCalendar(int calendarID) throws Exception {
		CalendarModel calendar = null;
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Calendar WHERE idCalendar=?;");
			ps.setInt(1, calendarID);
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {

				int calID = resultSet.getInt("idCalendar");
				String calendarName = resultSet.getString("calendarName");
				String startDate = resultSet.getString("startDate");
				String endDate = resultSet.getString("endDate");
				int startHour = resultSet.getInt("startHour");
				int endHouar = resultSet.getInt("endHour");
				int duration = resultSet.getInt("duration");

				calendar = new CalendarModel(calID, calendarName, startDate, endDate, startHour, endHouar, duration);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new Exception("Failed to get Calendar: " + e.getMessage());
		}
		return calendar;
	}

	// get all calendar in the database
	public List<CalendarModel> getAllCalendar() {
		List<CalendarModel> calendarList = new ArrayList<CalendarModel>();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Calendar;");
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				int calID = resultSet.getInt("idCalendar");
				String calendarName = resultSet.getString("calendarName");
				String startDate = resultSet.getString("startDate");
				String endDate = resultSet.getString("endDate");
				int startHour = resultSet.getInt("startHour");
				int endHouar = resultSet.getInt("endHour");
				int duration = resultSet.getInt("duration");

				CalendarModel calendar = new CalendarModel(calID, calendarName, startDate, endDate, startHour, endHouar,
						duration);

				calendarList.add(calendar);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return calendarList;
	}

	// delete a single calendar
	public boolean deleteCalendar(int calendarID) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM Calendar WHERE idCalendar = ?;");
			ps.setInt(1, calendarID);
			int numAffected = ps.executeUpdate();
			ps.close();

			return (numAffected == 1);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// add a new calendar to the database
	public boolean addCalendar(CalendarModel calendar) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO Calendar (calendarName,startDate,endDate,startHour,endHour,duration) values(?,?,?,?,?,?);");

			ps.setString(1, calendar.getCalendarName());
			ps.setString(2, calendar.getStartDate());
			ps.setString(3, calendar.getEndDate());
			ps.setInt(4, calendar.getStartHour());
			ps.setInt(5, calendar.getEndHouar());
			ps.setInt(6, calendar.getDuration());
			ps.execute();
			return true;

		} catch (Exception e) {
			throw new Exception("Failed to insert Calendar: " + e.getMessage());
		}
	}

	// add a new meeting to the table
	public boolean addMeeting(MeetingModel meeting) throws Exception {
		try {

			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO Meeting (name,location,participant,date,timeSlotID,calendarID) values(?,?,?,?,?,?);");
			ps.setString(1, meeting.getMeetingName());
			ps.setString(2, meeting.getMeetingLocaion());
			ps.setString(3, meeting.getMeetingPerticipent());
			ps.setString(4, meeting.getMeetingDate());
			ps.setInt(5, meeting.getTimeSlotID());
			ps.setInt(6, meeting.getCalendarID());
			ps.execute();
			return true;

		} catch (Exception e) {
			throw new Exception("Failed to insert constant: " + e.getMessage());
		}
	}

	// remove a meeting from meeting table
	public boolean removeMeeting(int meetingID) {
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM Meeting WHERE meetingID = ?;");
			ps.setInt(1, meetingID);
			int numAffected = ps.executeUpdate();
			ps.close();

			return (numAffected == 1);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<MeetingModel> getAllMeetings(int calendarID) {
		List<MeetingModel> MeetingModels = new ArrayList<MeetingModel>();

		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Meeting WHERE calendarID=?;");
			ps.setInt(1, calendarID);
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				int meetingID = resultSet.getInt("meetingID");
				String meetingName = resultSet.getString("meetingName");
				String meetingLocaion = resultSet.getString("meetingLocaion");
				String meetingPerticipent = resultSet.getString("meetingPerticipent");
				String meetingDate = resultSet.getString("meetingDate");
				int timeSlotID = resultSet.getInt("timeSlotID");

				MeetingModel meetingModel = new MeetingModel(meetingID, meetingName, meetingLocaion, meetingPerticipent,
						meetingDate, timeSlotID, calendarID);

				MeetingModels.add(meetingModel);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return MeetingModels;
	}

	public boolean checkTimeSlot(int calendarID) {
		boolean flag=true;
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement("SELECT * FROM avilableTimeSlot WHERE calendarID = ?;");

			ps.setInt(1, calendarID);
			ResultSet resultSet = ps.executeQuery();
			// already present?
			while (resultSet.next()) {
				resultSet.close();
				flag=  false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	// add time slots to the table
	public boolean addTimeSlot(int calendarID, aviliableTimeSlotModel timeSlot) throws Exception {
		try {

			PreparedStatement ps = conn
					.prepareStatement("INSERT INTO avilableTimeSlot (date,calendarID,timeSlotStatus) values(?,?,?);");
			ps.setString(1, timeSlot.getDate());
			ps.setInt(2, timeSlot.getCalendarID());
			ps.setInt(3, timeSlot.getTimeSlotStatus());
			ps.execute();
			return true;

		} catch (Exception e) {
			throw new Exception("Failed to insert TimeSlot: " + e.getMessage());
		}
	}

	// remove a timeslot from the calendar
	public boolean removeTimeSlot(int timeslotID) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM AviliableTimeSlot WHERE timeslotID = ?;");
			ps.setInt(1, timeslotID);
			int numAffected = ps.executeUpdate();
			ps.close();

			return (numAffected == 1);

		} catch (Exception e) {
			throw new Exception("Failed to insert removeTimeSlot: " + e.getMessage());
		}
	}

	// get all timeslot from the table
	public List<aviliableTimeSlotModel> getAllTimeslot(int calendarID) throws Exception {
		List<aviliableTimeSlotModel> timeSlotList = new ArrayList<aviliableTimeSlotModel>();

		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM avilableTimeSlot WHERE calendarID=?;");
			ps.setInt(1, calendarID);
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				int timeSlotID = resultSet.getInt("timeSlotID");
				String date = resultSet.getString("date");
				int calID = resultSet.getInt("calendarID");
				int timeSlotStatus = resultSet.getInt("timeSlotStatus");

				aviliableTimeSlotModel timeSlot = new aviliableTimeSlotModel(timeSlotID, date, calendarID,
						timeSlotStatus);

				timeSlotList.add(timeSlot);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new Exception("Failed to  getAllTimeslot: " + e.getMessage());
		}
		return timeSlotList;
	}

	// get meetings
	public List<MeetingModel> getMeetings(int calendarID, String year, String month, String day) {
		List<MeetingModel> MeetingModels = new ArrayList<MeetingModel>();
		PreparedStatement ps;
		try {
			if (day.equals("00")) {
				ps = conn.prepareStatement("SELECT * FROM Meeting WHERE calendarID=? AND date LIKE ?;");
				ps.setInt(1, calendarID);
				ps.setString(2, month+"%"+year+"%");
			} else {
				ps = conn.prepareStatement("SELECT * FROM Meeting WHERE calendarID=? AND date LIKE ?;");
				ps.setInt(1, calendarID);
				ps.setString(2, month+"-"+day+"-"+year+"%");
			}
			
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				int meetingID = resultSet.getInt("meetingID");
				String meetingName = resultSet.getString("name");
				String meetingLocaion = resultSet.getString("location");
				String meetingPerticipent = resultSet.getString("participant");
				String meetingDate = resultSet.getString("date");
				int timeSlotID = resultSet.getInt("timeSlotID");

				MeetingModel meetingModel = new MeetingModel(meetingID, meetingName, meetingLocaion, meetingPerticipent,
						meetingDate, timeSlotID, calendarID);
				
				MeetingModels.add(meetingModel);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return MeetingModels;
	}
	
	// set the time slot status
	public boolean setTimeslotStatus(int timeslotID, int statusFlag) throws Exception {
		CalendarModel calendar = null;
		try {
			PreparedStatement ps = conn
					.prepareStatement("UPDATE avilableTimeSlot SET timeSlotStatus=? WHERE timeSlotID=?;");
			ps.setInt(1, statusFlag);
			ps.setInt(2, timeslotID);
			int numAffected = ps.executeUpdate();
			ps.close();
			return (numAffected == 1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new Exception("Failed to  setTimeslotStatus: " + e.getMessage());
		}
	}
	
	public List<aviliableTimeSlotModel> getTimeSlots(int calendarID, String year, String month, String day) {
		List<aviliableTimeSlotModel> timeSlots = new ArrayList<aviliableTimeSlotModel>();
		PreparedStatement ps;
		try {
			if (day.equals("00")) {
				ps = conn.prepareStatement("SELECT * FROM avilableTimeSlot WHERE calendarID=? AND date LIKE ?;");
				ps.setInt(1, calendarID);
				ps.setString(2, month+"-"+"%"+"-"+year+"%");
			} else {
				ps = conn.prepareStatement("SELECT * FROM avilableTimeSlot WHERE calendarID=? AND date LIKE ?;");
				ps.setInt(1, calendarID);
				ps.setString(2, month+"-"+day+"-"+year+"%");
			}
			
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				int timeSlotID = resultSet.getInt("timeSlotID");
				String date = resultSet.getString("date");
				int timeSlotStatus = resultSet.getInt("timeSlotStatus");
				int calID = resultSet.getInt("calendarID");

				aviliableTimeSlotModel ts = new aviliableTimeSlotModel(timeSlotID, date, calID, timeSlotStatus);
				
				timeSlots.add(ts);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timeSlots;
	}
}
