package com.mikesu.expandablecalendar.view.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by MikeSu on 08.08.2016.
 * www.michalsulek.pl
 */

public abstract class CellView extends FrameLayout {

  public CellView(Context context) {
    super(context);
  }

  public CellView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public CellView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public enum DayType {
    WEEKEND,
    NO_WEEKEND
  }

  public enum TimeType {
    PAST,
    CURRENT,
    FUTURE
  }
}
