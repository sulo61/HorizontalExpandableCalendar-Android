package com.mikesu.expandablecalendar.view.cell;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.mikesu.expandablecalendar.R;

/**
 * Created by MikeSu on 04/08/16.
 * www.michalsulek.pl
 */

public class CellView extends FrameLayout {

  private TextView text;
  private TimeType timeType;

  public CellView(Context context) {
    super(context);
    init();
  }

  public CellView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public CellView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    initView();
  }

  private void initView() {
    inflate(getContext(), R.layout.month_cell_view, this);

    text = (TextView) findViewById(R.id.text);
  }

  public void setText(String text) {
    this.text.setText(text);
  }

  public void setTimeType(TimeType timeType) {
    this.timeType = timeType;
    setTextColorByType();
  }

  private void setTextColorByType() {
    if (this.timeType == TimeType.CURRENT) {
      text.setTextColor(Color.BLACK);
    } else {
      text.setTextColor(Color.GRAY);
    }
  }

  public enum TimeType {
    PAST,
    CURRENT,
    FUTURE
  }
}
