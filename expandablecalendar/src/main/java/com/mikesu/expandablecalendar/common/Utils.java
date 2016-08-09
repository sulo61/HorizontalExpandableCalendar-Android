package com.mikesu.expandablecalendar.common;

import java.util.Random;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.Weeks;

/**
 * Created by MikeSu on 08/08/16.
 * www.michalsulek.pl
 */

public class Utils {

  public static int monthsBetween(DateTime date1, DateTime date2) {
    return Months.monthsBetween(date1, date2).getMonths();
  }

  public static int weeksBetween(DateTime date1, DateTime date2) {
    return Weeks.weeksBetween(date1, date2).getWeeks();
  }

  public static boolean isWeekendByColumnNumber(int column) {
    return column == 5 || column == 6;
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
    return Config.currentViewPager == Config.CurrentViewPager.MONTH;
  }
}
