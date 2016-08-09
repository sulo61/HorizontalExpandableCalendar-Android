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
  private static long lastClearTimestamp = 0;
  private static boolean locked = false;
  private static final long CLEAR_DELAY = 10000;

  public static void init() {
    marksMap = new HashMap<>();
  }

  public static void markToday() {
    if (isLocked()) {
      return;
    }
    lock();
    MarkSetup markSetup = getMark(new DateTime());
    if (markSetup == null) {
      addMark(new DateTime(), new MarkSetup(true, false));
    } else {
      markSetup.setToday(true);
    }
    unlock();
  }

  public static void refreshMarkSelected(boolean selected) {
    if (isLocked()) {
      return;
    }
    lock();
    MarkSetup markSetup = getMark(Config.currentDate);
    if (markSetup == null) {
      addMark(Config.currentDate, new MarkSetup(false, selected));
    } else {
      markSetup.setSelected(selected);
    }
    unlock();
  }


  public static void clear() {
    marksMap.clear();
  }

  public static void addMark(DateTime dateTime, MarkSetup markSetup) {
    marksMap.put(dateTimeToStringKey(dateTime), markSetup);
    clearUselessMarks();
  }

  public static MarkSetup getMark(DateTime dateTime) {
    clearUselessMarks();
    return marksMap.get(dateTimeToStringKey(dateTime));
  }

  private static String dateTimeToStringKey(DateTime dateTime) {
    return dateTime.getYear() + "-" + dateTime.getMonthOfYear() + "-" + dateTime.getDayOfMonth();
  }

  public static void clearUselessMarks() {
    if (System.currentTimeMillis() - lastClearTimestamp > CLEAR_DELAY) {
      lastClearTimestamp = System.currentTimeMillis();
      for (Map.Entry<String, MarkSetup> entry : marksMap.entrySet()) {
        MarkSetup markSetup = entry.getValue();
        if (isEmptyMark(markSetup)) {
          marksMap.remove(markSetup);
        }
      }
    }
  }

  private static boolean isEmptyMark(MarkSetup markSetup) {
    return (markSetup == null || (!markSetup.isSelected() && !markSetup.isToday()));
  }

  public static void lock() {
    locked = true;
  }

  public static void unlock() {
    locked = false;
  }

  public static boolean isLocked() {
    return locked;
  }

}
