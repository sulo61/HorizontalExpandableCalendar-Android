package com.mikesu.expandablecalendarandroid;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.mikesu.horizontalexpcalendar.CalendarView;
import com.mikesu.horizontalexpcalendar.common.Config;

import java.time.LocalDate;

/**
 * Created by MikeSu on 04/08/16.
 * www.michalsulek.pl
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalendarView calendarView = findViewById(R.id.calendar);
        calendarView.setHorizontalExpCalListener(new CalendarView.HorizontalExpCalListener() {
            @Override
            public void onCalendarScroll(LocalDate dateTime) {
                Log.i(TAG, "onCalendarScroll: " + dateTime.toString());
            }

            @Override
            public void onDateSelected(LocalDate dateTime) {
                Log.i(TAG, "onDateSelected: " + dateTime.toString());
            }

            @Override
            public void onChangeViewPager(Config.ViewPagerType viewPagerType) {
                Log.i(TAG, "onChangeViewPager: " + viewPagerType.name());
            }
        });
    }
}
