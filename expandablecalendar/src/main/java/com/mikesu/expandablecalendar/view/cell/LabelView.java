package com.mikesu.expandablecalendar.view.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.mikesu.expandablecalendar.R;
import com.mikesu.expandablecalendar.common.Config;

/**
 * Created by MikeSu on 04/08/16.
 * www.michalsulek.pl
 */

public class LabelView extends CellView {

  private TextView text;
  private DayType dayType;

  public LabelView(Context context) {
    super(context);
    init();
  }

  public LabelView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public LabelView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    initView();
  }

  private void initView() {
    inflate(getContext(), R.layout.label_cell_view, this);

    text = (TextView) findViewById(R.id.text);
  }

  public void setText(String text) {
    this.text.setText(text);
  }

  public void setDayType(DayType dayType) {
    this.dayType = dayType;
    setTextBackgroundByDayType();
  }

  private void setTextBackgroundByDayType() {
    if (this.dayType == DayType.WEEKEND) {
      text.setBackgroundColor(Config.CELL_WEEKEND_BACKGROUND);
    } else {
      text.setBackgroundColor(Config.CELL_NON_WEEKEND_BACKGROUND);
    }
  }

}
