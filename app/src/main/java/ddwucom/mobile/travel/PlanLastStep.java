package ddwucom.mobile.travel;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlanLastStep extends AppCompatActivity {
    Spinner daySpinner;
    EditText etDate;
    TextView stDate;
    TextView enDate;
    CompactCalendarView stCalendar;
    CompactCalendarView enCalendar;
    int start;
    int end;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_3);

        stDate = findViewById(R.id.y_pickStartDate);
        enDate = findViewById(R.id.y_pickEndDate);
        daySpinner = findViewById(R.id.daySpinner);

        stCalendar = (CompactCalendarView) findViewById(R.id.y_startCalendar);
        stCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        stCalendar.setVisibility(View.GONE);
        enCalendar = (CompactCalendarView) findViewById(R.id.y_EndCalendar);
        enCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        enCalendar.setVisibility(View.GONE);

        stCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                SimpleDateFormat stdate = new SimpleDateFormat("yyyy.MM.dd");
                stDate.setText(stdate.format(dateClicked));
                SimpleDateFormat day = new SimpleDateFormat("dd");
                start = Integer.parseInt(day.format(dateClicked));
                stCalendar.setVisibility(View.GONE);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

            }
        });

        enCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                SimpleDateFormat stdate = new SimpleDateFormat("yyyy.MM.dd");
                enDate.setText(stdate.format(dateClicked));
                SimpleDateFormat day = new SimpleDateFormat("dd");
                end = Integer.parseInt(day.format(dateClicked));
                enCalendar.setVisibility(View.GONE);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

            }
        });

        if (stDate.length() != 0 && enDate.length() != 0) {
            Log.d("yj", "stDate: "+stDate.length());
            Log.d("yj", "enDate: "+enDate.length());
            Log.d("yj", start + ","+ end + "들어옴");
            List<String> day = null;
            Log.d("yj", "리스트는 널");
            for (int i = 1; i < end - start + 1; i++) {
                day.add("Day " + i);
                Log.d("yj", day.get(i - 1));
            }
            Log.d("yj", "반복끝");
            spinnerClick(daySpinner, day);
            Log.d("yj", "함수실행");
        }
    }

    public void spinnerClick(Spinner spinner, List list){
        SpinnerAdapter adapter = new SpinnerAdapter(PlanLastStep.this, list);
        spinner.setAdapter(adapter);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.y_pickStartDate:
                stCalendar.setVisibility(View.VISIBLE);
                break;
            case R.id.y_pickEndDate:
                enCalendar.setVisibility(View.VISIBLE);;
                break;
            case R.id.y_save_plan:
//                Intent intent = new Intent(PlanLastStep.this, Map.class);
//                startActivity(intent);
                Toast.makeText(PlanLastStep.this, "계획 저장 완료!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
