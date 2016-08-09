package com.mikesu.expandablecalendar.view.page;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import com.mikesu.expandablecalendar.common.Config;
import com.mikesu.expandablecalendar.R;
import com.mikesu.expandablecalendar.common.Constants;
import com.mikesu.expandablecalendar.common.Utils;
import com.mikesu.expandablecalendar.view.cell.CellView;
import com.mikesu.expandablecalendar.view.cell.DayView;
import com.mikesu.expandablecalendar.view.cell.LabelView;
import org.joda.time.DateTime;

/**
 * Created by MikeSu on 04/08/16.
 * www.michalsulek.pl
 */

public class WeekPageView extends FrameLayout {

  private GridLayout gridLayout;
  private DateTime pageDate;

  public WeekPageView(Context context) {
    super(context);
    init();
  }

  public WeekPageView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public WeekPageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    initVariables();
    initViews();
  }

  private void initVariables() {
  }

  private void initViews() {
    inflate(getContext(), R.layout.week_page_view, this);
    gridLayout = (GridLayout) findViewById(R.id.grid_layout);
    gridLayout.setColumnCount(Config.COLUMNS);
    gridLayout.setRowCount(Config.WEEK_ROWS + (Utils.dayLabelExtraRow()));
  }

  public void setup(DateTime pageDate) {
    this.pageDate = pageDate;
    addCellsToGrid();
  }

  private void addCellsToGrid() {
    DateTime cellDate = pageDate.plusDays(-pageDate.getDayOfWeek() + 1);
    if (Config.USE_DAY_LABELS) {
      for (int l = 0; l < Config.COLUMNS; l++) {
        LabelView label = new LabelView(getContext());

        GridLayout.LayoutParams labelParams = new GridLayout.LayoutParams(GridLayout.spec(0), GridLayout.spec(l));
        labelParams.height = Config.cellHeight;
        labelParams.width = Config.cellWidth;
        label.setLayoutParams(labelParams);
        label.setText(Constants.NAME_OF_DAYS[l]);
        label.setDayType(Utils.isWeekendByColumnNumber(l) ? CellView.DayType.WEEKEND : CellView.DayType.NONWEEKEND);

        gridLayout.addView(label);
      }
    }
    for (int r = Utils.dayLabelExtraRow(); r < Config.WEEK_ROWS + (Utils.dayLabelExtraRow()); r++) {
      for (int c = 0; c < Config.COLUMNS; c++) {
        DayView dayView = new DayView(getContext());

        GridLayout.LayoutParams cellParams = new GridLayout.LayoutParams(GridLayout.spec(r), GridLayout.spec(c));
        cellParams.height = Config.cellHeight;
        cellParams.width = Config.cellWidth;
        dayView.setLayoutParams(cellParams);
        dayView.setText(String.valueOf(cellDate.getDayOfMonth()));
        dayView.setDayType(Utils.isWeekendByColumnNumber(c) ? CellView.DayType.WEEKEND : CellView.DayType.NONWEEKEND);

        gridLayout.addView(dayView);

        cellDate = cellDate.plusDays(1);
      }
    }
  }
}
