package com.mikesu.horizontalexpcalendar.common;

import android.graphics.Color;

import java.time.LocalDate;

/**
 * Created by MikeSu on 08/08/16.
 * www.michalsulek.pl
 */

public class Config {

  /* CONFIGURATION */
  private static final ViewPagerType INIT_VIEW = ViewPagerType.MONTH;
  private static final int RANGE_MONTHS_BEFORE_INIT = 12;
  private static final int RANGE_MONTHS_AFTER_INIT = 18;
  public static final LocalDate INIT_DATE = LocalDate.now();
  public static final FirstDay FIRST_DAY_OF_WEEK = FirstDay.MONDAY;
  public static final int CELL_WEEKEND_BACKGROUND = Color.WHITE;
  public static final int CELL_NON_WEEKEND_BACKGROUND = Color.WHITE;
  public static final int CELL_TEXT_CURRENT_MONTH_COLOR = Color.BLACK;
  public static final int CELL_TEXT_ANOTHER_MONTH_COLOR = Color.parseColor("#c3c6cd");
  public static final boolean USE_DAY_LABELS = true;
  public static final boolean SCROLL_TO_SELECTED_AFTER_COLLAPSE = true;
  /* END CONFIGURATION */

  private static LocalDate getStartDate() {
    LocalDate START_BACK_BY_RANGE = INIT_DATE.minusMonths(RANGE_MONTHS_BEFORE_INIT);
    LocalDate START_BACK_TO_FIRST_DAY_OF_MONTH = START_BACK_BY_RANGE.plusDays(-START_BACK_BY_RANGE.getDayOfMonth() + 1);
    return START_BACK_TO_FIRST_DAY_OF_MONTH.plusDays(-START_BACK_TO_FIRST_DAY_OF_MONTH.getDayOfWeek().getValue() + 1);
  }

  private static LocalDate getEndDate() {
    LocalDate END_FORWARD_BY_RANGE = INIT_DATE.plusMonths(RANGE_MONTHS_AFTER_INIT + 1);
    LocalDate END_BACK_TO_FIRST_DAY_OF_MONTH = END_FORWARD_BY_RANGE.plusDays(-END_FORWARD_BY_RANGE.getDayOfMonth() + 1);
    return END_BACK_TO_FIRST_DAY_OF_MONTH.plusDays(7 - END_BACK_TO_FIRST_DAY_OF_MONTH.getDayOfWeek().getValue() + 1);
  }

  public static LocalDate START_DATE = getStartDate();
  public static final LocalDate END_DATE = getEndDate();

  public static final int MONTH_ROWS = 6;
  public static final int WEEK_ROWS = 1;
  public static final int COLUMNS = 7;

  public static ViewPagerType currentViewPager = INIT_VIEW;
  public static LocalDate scrollDate = INIT_DATE;
  public static LocalDate selectionDate = LocalDate.now();
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
