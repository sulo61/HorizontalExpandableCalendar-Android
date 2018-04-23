package com.mikesu.horizontalexpcalendar.common;

import android.graphics.Color;
import android.util.Log;

import org.joda.time.DateTime;

/**
 * Created by MikeSu on 08/08/16.
 * www.michalsulek.pl
 */

public class Config {

  /* CONFIGURATION */
  private static final ViewPagerType INIT_VIEW = ViewPagerType.MONTH;
  private static final int RANGE_MONTHS_BEFORE_INIT = 12;
  private static final int RANGE_MONTHS_AFTER_INIT = 18;
  public static final DateTime INIT_DATE = new DateTime();
  public static final FirstDay FIRST_DAY_OF_WEEK = FirstDay.MONDAY;
  public static final int CELL_WEEKEND_BACKGROUND = Color.WHITE;
  public static final int CELL_NON_WEEKEND_BACKGROUND = Color.WHITE;
  public static final int CELL_TEXT_CURRENT_MONTH_COLOR = Color.BLACK;
  public static final int CELL_TEXT_ANOTHER_MONTH_COLOR = Color.parseColor("#c3c6cd");
  public static final boolean USE_DAY_LABELS = true;
  public static final boolean SCROLL_TO_SELECTED_AFTER_COLLAPSE = true;
  /* END CONFIGURATION */

  private static DateTime getStartDate() {
    DateTime START_BACK_BY_RANGE = INIT_DATE.plusMonths(-RANGE_MONTHS_BEFORE_INIT);
    DateTime START_BACK_TO_FIRST_DAY_OF_MONTH = START_BACK_BY_RANGE.plusDays(-START_BACK_BY_RANGE.getDayOfMonth() + 1);
    return START_BACK_TO_FIRST_DAY_OF_MONTH.plusDays(-START_BACK_TO_FIRST_DAY_OF_MONTH.getDayOfWeek() + 1);
  }

  private static DateTime getEndDate() {
    DateTime END_FORWARD_BY_RANGE = INIT_DATE.plusMonths(RANGE_MONTHS_AFTER_INIT + 1);
    DateTime END_BACK_TO_FIRST_DAY_OF_MONTH = END_FORWARD_BY_RANGE.plusDays(-END_FORWARD_BY_RANGE.getDayOfMonth() + 1);
    return END_BACK_TO_FIRST_DAY_OF_MONTH.plusDays(7 - END_BACK_TO_FIRST_DAY_OF_MONTH.getDayOfWeek() + 1);
  }

  public static DateTime START_DATE = getStartDate();
  public static final DateTime END_DATE = getEndDate();

  public static final int MONTH_ROWS = 6;
  public static final int WEEK_ROWS = 1;
  public static final int COLUMNS = 7;

  public static ViewPagerType currentViewPager = INIT_VIEW;
  public static DateTime scrollDate = INIT_DATE;
  public static DateTime selectionDate = new DateTime();
  public static int cellWidth = 0;
  public static int cellHeight = 0;
  public static int monthViewPagerHeight;
  public static int weekViewPagerHeight;

  public enum ViewPagerType {
    MONTH,
    WEEK
  }

  public enum FirstDay {
    SUNDAY,
    MONDAY
  }

}
