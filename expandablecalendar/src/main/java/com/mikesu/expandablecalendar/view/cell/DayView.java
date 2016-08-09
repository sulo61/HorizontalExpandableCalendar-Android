package com.mikesu.expandablecalendar.view.cell;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;
import com.mikesu.expandablecalendar.R;
import com.mikesu.expandablecalendar.common.Config;

/**
 * Created by MikeSu on 04/08/16.
 * www.michalsulek.pl
 */

public class DayView extends CellView {

  private TextView text;
  private TimeType timeType;

  public DayView(Context context) {
    super(context);
    init();
  }

  public DayView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public DayView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    initView();
  }

  private void initView() {
    inflate(getContext(), R.layout.day_cell_view, this);

    text = (TextView) findViewById(R.id.text);
  }

  public void setText(String text) {
    this.text.setText(text);
  }

  public void setTimeType(TimeType timeType) {
    this.timeType = timeType;
    setTextColorByTimeType();
  }

  public void setDayType(DayType dayType) {
    this.dayType = dayType;
    setTextBackgroundByDayType();
  }


  private void setTextColorByTimeType() {
    if (this.timeType == TimeType.CURRENT) {
      text.setTextColor(Color.BLACK);
    } else {
      text.setTextColor(Color.GRAY);
    }
  }

}
