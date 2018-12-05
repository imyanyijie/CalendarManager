package com.amazonaws.lambda.logic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.amazonaws.lambda.db.DatabasePersistance;
import com.amazonaws.lambda.model.aviliableTimeSlotModel;

public class timeSlotList {

	private static timeSlotList _instance;
	private static DateTimeFormatter dfDay = DateTimeFormat.forPattern(Setting.dateDayFormat);
	private static DateTimeFormatter dfHour = DateTimeFormat.forPattern(Setting.hourFormat);
	private static DateTimeFormatter dfTimeSlot = DateTimeFormat.forPattern(Setting.dateSlotFormat);
	private DatabasePersistance persistance;

	public static timeSlotList getInstance() {
		if (_instance == null) {
			_instance = new timeSlotList();
		}
		return _instance;
	}

	public timeSlotList() {
		persistance = new DatabasePersistance();
	}

	public void createTimeSlot(int calendarID, String startDate, String endDate, int startHour, int endHour,
			int duration) throws Exception {
		try {
			DateTime startDay = dfDay.parseDateTime(startDate);
			DateTime endDay = dfDay.parseDateTime(endDate);
			startDay = startDay.withHourOfDay(startHour);
			startDay = startDay.withMinuteOfHour(0);
			int duration_inLoops = (endHour - startHour) * 60 / duration;

			boolean validation = persistance.checkTimeSlot(calendarID);
			if (validation) {
				for (DateTime date = startDay; date.isBefore(endDay.plusDays(1)); date = date.plusDays(1)) {
					date = date.withHourOfDay(startHour);
					for (int i = 0; i < duration_inLoops; i++) {
						String date_asString = date.toString(dfTimeSlot);
						aviliableTimeSlotModel timeslot = new aviliableTimeSlotModel(date_asString, calendarID, 0); // open
																													// timeslot
						persistance.addTimeSlot(calendarID, timeslot);
						date = date.plusMinutes(duration);
					}

				}
			}
		} catch (Exception ex) {
			throw new Exception("Failed to create Timeslot " + ex.getMessage());
		}
	}

	public List<String> getTimeslotByDay(int calendarID) throws Exception {
		List<aviliableTimeSlotModel> timeslotList = new ArrayList<aviliableTimeSlotModel>();
		List<String> dayList = new ArrayList<String>();
		timeslotList = persistance.getAllTimeslot(calendarID);

		for (aviliableTimeSlotModel timeslot : timeslotList) {
			String timeslot_temp = timeslot.getDate();
			DateTime day = dfTimeSlot.parseDateTime(timeslot_temp);
			timeslot_temp = day.toString(dfDay);
			dayList.add(timeslot_temp);
		}

		Set<String> hs = new HashSet<>();
		hs.addAll(dayList);
		dayList.clear();
		dayList.addAll(hs);
		return dayList;
	}

}
