package com.mikesu.horizontalexpcalendar.common;

import android.util.Log;

import com.mikesu.horizontalexpcalendar.model.MarkSetup;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MikeSu on 09.08.2016.
 * www.michalsulek.pl
 */

public class Marks {

  private static final String TAG = "Marks";
  private static Map<String, MarkSetup> marksMap;
  private static boolean locked = false;

  public static final float MARK_CUSTOM1_SIZE_PROPORTION_TO_CELL = 0.15f;
  public static final float MARK_CUSTOM2_HEIGHT_PROPORTION_TO_CELL = 0.4f;
  public static final float MARK_CUSTOM2_WIDTH_PROPORTION_TO_CELL = 0.08f;

  public static void init() {
    marksMap = new HashMap<>();
  }

  public static void markToday() {
    if (isLocked()) {
      return;
    }
    lock();
    MarkSetup markSetup = getMark(LocalDate.now());
    if (markSetup == null) {
      addNewMark(LocalDate.now(), new MarkSetup(true, false));
    } else {
      markSetup.setToday(true);
    }
    unlock();
  }

  public static void refreshMarkSelected(LocalDate newSelection) {
    if (isLocked()) {
      return;
    }
    lock();
    MarkSetup oldSelectionSetup = getMark(Config.selectionDate);
    if (oldSelectionSetup != null) {
      oldSelectionSetup.setSelected(false);
      if (oldSelectionSetup.canBeDeleted()) {
        marksMap.remove(dateTimeToStringKey(Config.selectionDate));
      }
    }

    MarkSetup newSelectionSetup = getMark(newSelection);
    if (newSelectionSetup == null) {
      newSelectionSetup = new MarkSetup();
      marksMap.put(dateTimeToStringKey(newSelection), newSelectionSetup);
    }
    newSelectionSetup.setSelected(true);

    Config.selectionDate = newSelection;

    unlock();
  }

  public static void refreshCustomMark(LocalDate dateTime, CustomMarks customMarks, boolean mark) {
    if (isLocked()) {
      return;
    }
    lock();
    MarkSetup markSetup = getMark(dateTime);
    if (markSetup == null) {
      markSetup = new MarkSetup();
      addNewMark(dateTime, markSetup);
    }
    switch (customMarks) {
      case CUSTOM1:
        markSetup.setCustom1(mark);
        break;
      case CUSTOM2:
        markSetup.setCustom2(mark);
        break;
      default:
        Log.e(TAG, "markCustom, unknown case: " + customMarks.name());
    }

    if (mark) {
      if (markSetup.canBeDeleted()) {
        marksMap.remove(dateTimeToStringKey(dateTime));
      }
    }

    unlock();
  }

  public static void clear() {
    marksMap.clear();
  }

  private static void addNewMark(LocalDate dateTime, MarkSetup markSetup) {
    marksMap.put(dateTimeToStringKey(dateTime), markSetup);
  }

  public static MarkSetup getMark(LocalDate dateTime) {
    return marksMap.get(dateTimeToStringKey(dateTime));
  }

  @NotNull
  private static String dateTimeToStringKey(@NotNull LocalDate dateTime) {
    return dateTime.getYear() + "-" + dateTime.getMonthValue() + "-" + dateTime.getDayOfMonth();
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

  public enum CustomMarks {
    CUSTOM1,
    CUSTOM2
  }

}
