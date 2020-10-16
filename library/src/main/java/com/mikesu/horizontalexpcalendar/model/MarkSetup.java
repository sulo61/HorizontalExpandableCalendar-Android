package com.mikesu.horizontalexpcalendar.model;

/**
 * Created by MikeSu on 09.08.2016.
 * www.michalsulek.pl
 */

public class MarkSetup {

  private boolean today;
  private boolean selected;
  private boolean custom1;
  private boolean custom2;

  public MarkSetup() {
    this.today = false;
    this.selected = false;
    this.custom1 = false;
    this.custom2 = false;
  }

  public MarkSetup(boolean today, boolean selected) {
    this(today, selected, false);
  }

  public MarkSetup(boolean today, boolean selected, boolean custom1) {
    this(today, selected, custom1, false);
  }

  public MarkSetup(boolean today, boolean selected, boolean custom1, boolean custom2) {
    this.today = today;
    this.selected = selected;
    this.custom1 = custom1;
    this.custom2 = custom2;
  }

  public boolean isToday() {
    return today;
  }

  public void setToday(boolean today) {
    this.today = today;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public boolean isCustom1() {
    return custom1;
  }

  public void setCustom1(boolean custom1) {
    this.custom1 = custom1;
  }

  public boolean isCustom2() {
    return custom2;
  }

  public void setCustom2(boolean custom2) {
    this.custom2 = custom2;
  }

  public boolean canBeDeleted() {
    return !today && !selected && !custom1 && !custom2;
  }
}
