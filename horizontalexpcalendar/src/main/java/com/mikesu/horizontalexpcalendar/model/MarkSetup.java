package com.mikesu.horizontalexpcalendar.model;

/**
 * Created by MikeSu on 09.08.2016.
 * www.michalsulek.pl
 */

public class MarkSetup {

  private boolean today;
  private boolean selected;

  public MarkSetup() {
    this.today = false;
    this.selected = false;
  }

  public MarkSetup(boolean today, boolean selected) {
    this.today = today;
    this.selected = selected;
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

  public boolean canBeDeleted() {
    return !today && !selected;
  }
}
