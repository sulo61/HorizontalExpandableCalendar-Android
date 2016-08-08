package com.mikesu.expandablecalendar.common;

import android.graphics.Color;
import org.joda.time.DateTime;

/**
 * Created by MikeSu on 08/08/16.
 * www.michalsulek.pl
 */

public class Config {

  /**
   * TO CONFIGURE
   **/
  public static final DateTime INIT_DATE = new DateTime(); // today
  public static final int RANGE_MONTHS_BEFORE_INIT = 3;
  public static final int RANGE_MONTHS_AFTER_INIT = 3;
  public static final boolean USE_DAY_LABELS = true;
  public static final int CELL_WEEKEND_BACKGROUND = Color.parseColor("#11000000");
  public static final int CELL_NON_WEEKEND_BACKGROUND = Color.TRANSPARENT;
  /**
   * END CONFIGURATION
   **/

  public static final DateTime START_DATE =
      INIT_DATE
          .plusMonths(-RANGE_MONTHS_BEFORE_INIT)
          .plusDays(-INIT_DATE.plusMonths(-RANGE_MONTHS_BEFORE_INIT).getDayOfWeek());
  public static final DateTime END_DATE =
      INIT_DATE
          .plusMonths(RANGE_MONTHS_AFTER_INIT + 1)
          .plusDays(7 - INIT_DATE.plusMonths(RANGE_MONTHS_AFTER_INIT + 1).getDayOfWeek() + 1);

  public static final int MONTH_ROWS = 6;
  public static final int WEEK_ROWS = 1;
  public static final int COLUMNS = 7;

  public static CurrentVisibleViewPager currentVisibleViewPager = CurrentVisibleViewPager.MONTH;
  public static DateTime currentDate = INIT_DATE;
  public static int monthsBetweenStartAndInit = 0;
  public static int weeksBetweenStartAndInit = 0;
  public static int cellWidth = 0;
  public static int cellHeight = 0;

  public enum CurrentVisibleViewPager {
    MONTH,
    WEEK
  }

}
