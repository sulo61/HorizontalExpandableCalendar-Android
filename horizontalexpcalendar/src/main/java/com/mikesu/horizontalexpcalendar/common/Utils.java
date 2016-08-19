package com.mikesu.horizontalexpcalendar.common;

import java.util.Random;
import org.joda.time.DateTime;

/**
 * Created by MikeSu on 08/08/16.
 * www.michalsulek.pl
 */

public class Utils {

  public static int monthPositionFromDate(DateTime dateTo) {
    DateTime dateFrom = Config.START_DATE.withDayOfWeek(7);
    return ((dateTo.getYear() - dateFrom.getYear()) * 12) + (dateTo.getMonthOfYear() - dateFrom.getMonthOfYear());
  }

  public static int weekPositionFromDate(DateTime dateTo) {
    DateTime dateFrom = Config.START_DATE.toDateTime();
    while (dateFrom.getYear() < dateTo.getYear()) {
      dateFrom = dateFrom.plusYears(1);
    }
    return dateTo.getWeekOfWeekyear() - Config.START_DATE.getWeekOfWeekyear();
  }

  public static boolean isWeekendByColumnNumber(int column) {
    switch (Config.FIRST_DAY_OF_WEEK) {
      case SUNDAY:
        return (column == 0 || column == 6);
      case MONDAY:
        return (column == 5 || column == 6);
      default:
        return false;
    }
  }

  public static int getRandomColor() {
    return new Random().nextInt(40) + 215;
  }

  public static int dayLabelExtraRow() {
    return Config.USE_DAY_LABELS ? 1 : 0;
  }

  public static int dayLabelExtraChildCount() {
    return Config.USE_DAY_LABELS ? 7 : 0;
  }

  public static boolean isMonthView() {
    return Config.currentViewPager == Config.ViewPagerType.MONTH;
  }

  public static DateTime getDateByMonthPosition(int position) {
    return Config.START_DATE.withDayOfWeek(7).plusMonths(position);
  }

  public static DateTime getDateByWeekPosition(int position) {
    return Config.START_DATE.withDayOfWeek(7).plusWeeks(position);
  }

  public static boolean isTheSameMonthToScrollDate(DateTime dateTime) {
    return isTheSameMonth(Config.scrollDate, dateTime);
  }

  public static boolean isTheSameMonth(DateTime dateTime1, DateTime dateTime2) {
    return (dateTime1.getYear() == dateTime2.getYear()) && (dateTime1.getMonthOfYear() == dateTime2.getMonthOfYear());
  }

  public static boolean isTheSameWeekToScrollDate(DateTime dateTime) {
    return isTheSameWeek(Config.scrollDate, dateTime);
  }

  public static boolean isTheSameWeek(DateTime dateTime1, DateTime dateTime2) {
    return (dateTime1.getYear() == dateTime2.getYear()) && (dateTime1.getWeekOfWeekyear() == dateTime2.getWeekOfWeekyear());
  }

  public static int firstDayOffset() {
    switch (Config.FIRST_DAY_OF_WEEK) {
      case SUNDAY:
        return -1;
      case MONDAY:
        return 0;
    }
    return 0;
  }

  public static int getWeekOfMonth(DateTime dateTime) {
    return ((dateTime.getDayOfMonth() + dateTime.withDayOfMonth(1).getDayOfWeek() - 2 + firstDayOffset()) / 7) + 1;
  }
}
