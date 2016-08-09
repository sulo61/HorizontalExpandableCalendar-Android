package com.mikesu.expandablecalendar.model;

/**
 * Created by MikeSu on 09.08.2016.
 * www.michalsulek.pl
 */

public class Mark {

  private boolean today;
  private boolean selected;

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
}
