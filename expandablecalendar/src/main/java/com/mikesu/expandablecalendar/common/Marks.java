package com.mikesu.expandablecalendar.common;

import com.mikesu.expandablecalendar.model.MarkSetup;
import java.util.HashMap;
import java.util.Map;
import org.joda.time.DateTime;

/**
 * Created by MikeSu on 09.08.2016.
 * www.michalsulek.pl
 */
public class Marks {

  private static Map<String, MarkSetup> marksMap;

  public static void init() {
    marksMap = new HashMap<>();
    generateSampleDate();
  }


  private static void generateSampleDate() {
    addMark(new DateTime().withYear(2016).withMonthOfYear(8).withDayOfMonth(2), new MarkSetup(true, false));
    addMark(new DateTime().withYear(2016).withMonthOfYear(8).withDayOfMonth(20), new MarkSetup(false, true));
  }


  public static void clear() {
    marksMap.clear();
  }

  public static void addMark(DateTime dateTime, MarkSetup markSetup) {
    marksMap.put(dateTimeToStringKey(dateTime), markSetup);
  }

  public static MarkSetup getMark(DateTime dateTime) {
    return marksMap.get(dateTimeToStringKey(dateTime));
  }

  private static String dateTimeToStringKey(DateTime dateTime) {
    return dateTime.getYear() + "-" + dateTime.getMonthOfYear() + "-" + dateTime.getDayOfMonth();
  }

}
