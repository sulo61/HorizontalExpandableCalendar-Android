package com.mikesu.expandablecalendar.view.pages;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;
import com.mikesu.expandablecalendar.R;

/**
 * Created by MikeSu on 04/08/16.
 * www.michalsulek.pl
 */

public class MonthPageView extends GridLayout {

  public MonthPageView(Context context) {
    super(context);
    init();
  }

  public MonthPageView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public MonthPageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    initViews();
  }

  private void initViews() {
    inflate(getContext(), R.layout.month_page_view, this);
  }
}
