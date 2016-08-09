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
  private static boolean locked = false;

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
    MarkSetup markSetup = getMark(Config.selectionDate);
    if (markSetup == null) {
      markSetup = new MarkSetup();
      marksMap.put(dateTimeToStringKey(Config.selectionDate), markSetup);
    }

    markSetup.setSelected(selected);
    if (markSetup.canBeDeleted()) {
      marksMap.remove(dateTimeToStringKey(Config.selectionDate));
    }

    unlock();
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
