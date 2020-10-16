package com.mikesu.horizontalexpcalendar.common;

import android.content.res.Resources;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Random;

/**
 * Created by MikeSu on 08/08/16.
 * www.michalsulek.pl
 */

public class Utils {

  public static int monthPositionFromDate(@NotNull LocalDate dateTo) {
    LocalDate dateFrom = Config.START_DATE.with(ChronoField.DAY_OF_WEEK,7);
    return ((dateTo.getYear() - dateFrom.getYear()) * 12) + (dateTo.getMonthValue() - dateFrom.getMonthValue());
  }

  public static int weekPositionFromDate(@NotNull LocalDate dateTo) {
    LocalDate dateFrom = Config.START_DATE;
    LocalDate dateToWithFixedSeconds = dateTo.minusDays(firstDayOffset());
    int weeksBetween = 0;
    while (dateFrom.isBefore(dateToWithFixedSeconds.plusDays(1))) {
      weeksBetween++;
      dateFrom = dateFrom.plusWeeks(1);
    }
    return weeksBetween - 1;
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

  public static LocalDate getDateByMonthPosition(int position) {
    return Config.START_DATE.with(ChronoField.DAY_OF_WEEK,7 + firstDayOffset()).withDayOfMonth(1).plusMonths(position);
  }

  public static LocalDate getDateByWeekPosition(int position) {
    return Config.START_DATE.with(ChronoField.DAY_OF_WEEK,7 + firstDayOffset()).plusWeeks(position);
  }

  public static boolean isTheSameMonthToScrollDate(LocalDate dateTime) {
    return isTheSameMonth(Config.scrollDate, dateTime);
  }

  public static boolean isTheSameMonth(@NotNull LocalDate dateTime1, @NotNull LocalDate dateTime2) {
    return (dateTime1.getYear() == dateTime2.getYear()) && (dateTime1.getMonthValue() == dateTime2.getMonthValue());
  }

  public static boolean isTheSameWeekToScrollDate(LocalDate dateTime) {
    return isTheSameWeek(Config.scrollDate, dateTime);
  }

  public static boolean isTheSameWeek(@NotNull LocalDate dateTime1, @NotNull LocalDate dateTime2) {
    LocalDate firstDateMovedByFirstDayOfWeek = dateTime1.minusDays(firstDayOffset());
    LocalDate secondDateMovedByFirstDayOfWeek = dateTime2.minusDays(firstDayOffset());
    return (firstDateMovedByFirstDayOfWeek.getYear() == secondDateMovedByFirstDayOfWeek.getYear()) &&
        (firstDateMovedByFirstDayOfWeek.get(ChronoField.ALIGNED_WEEK_OF_YEAR) == secondDateMovedByFirstDayOfWeek.get(ChronoField.ALIGNED_WEEK_OF_YEAR));
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

  public static int getWeekOfMonth(@NotNull LocalDate dateTime) {
    //todo check if correctly converted return ((dateTime.getDayOfMonth() + dateTime.withDayOfMonth(1).getDayOfWeek() - 2 - firstDayOffset()) / 7) + 1;
  return dateTime.  get(ChronoField.ALIGNED_WEEK_OF_MONTH);
  }

  public static int animateContainerExtraTopOffset(@NotNull Resources resources) {
    float density = resources.getDisplayMetrics().density;
    if (density >= 4.0) {
      return 0;
    }
    if (density >= 3.0) {
      return 0;
    }
    if (density >= 2.0) {
      return 1;
    }
    if (density >= 1.5) {
      return 2;
    }
    if (density >= 1.0) {
      return 2;
    }
    return 0;
  }

  public static int animateContainerExtraSideOffset(@NotNull Resources resources) {
    float density = resources.getDisplayMetrics().density;
    if (density >= 4.0) {
      return 2;
    }
    if (density >= 3.0) {
      return 2;
    }
    if (density >= 2.0) {
      return 2;
    }
    if (density >= 1.5) {
      return 2;
    }
    if (density >= 1.0) {
      return 0;
    }
    return 0;
  }
}
