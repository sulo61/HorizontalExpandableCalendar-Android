package com.mikesu.expandablecalendar;

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
}
