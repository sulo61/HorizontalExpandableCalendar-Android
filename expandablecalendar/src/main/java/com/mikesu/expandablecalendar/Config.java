package com.mikesu.expandablecalendar;

import org.joda.time.DateTime;

/**
 * Created by MikeSu on 08/08/16.
 * www.michalsulek.pl
 */
public class Config {

  public static final DateTime START_DATE = new DateTime().plusYears(-1);
  public static final DateTime END_DATE = new DateTime().plusYears(1).plusMonths(1);
  public static final DateTime INIT_DAY = new DateTime();

  public static final int MONTH_ROWS = 6;
  public static final int WEEK_ROWS = 1;
  public static final int COLUMNS = 7;

  public static int monthsBetweenStartAndInit = 0;
  public static int weeksBetweenStartAndInit = 0;
  public static int cellWidth = 0;
  public static int cellHeight = 0;

}
