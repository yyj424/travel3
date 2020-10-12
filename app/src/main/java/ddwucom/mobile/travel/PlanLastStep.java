package ddwucom.mobile.travel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PlanLastStep extends AppCompatActivity {
    Spinner daySpinner;
    TextView stYM;
    TextView enYM;
    TextView stDate;
    TextView enDate;
    CompactCalendarView stCalendar;
    CompactCalendarView enCalendar;
    long start;
    long end;
    List<String> Day;
    private RecyclerView listview;
    private ArrayList<MyCourse> placeList = null;
    PlaceListAdapter placeListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_3);

        stDate = findViewById(R.id.y_pickStartDate);
        enDate = findViewById(R.id.y_pickEndDate);
        daySpinner = findViewById(R.id.daySpinner);
        stYM = findViewById(R.id.stYM);
        enYM = findViewById(R.id.enYM);

        stCalendar = (CompactCalendarView) findViewById(R.id.y_startCalendar);
        stCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        stCalendar.setVisibility(View.GONE);
        enCalendar = (CompactCalendarView) findViewById(R.id.y_EndCalendar);
        enCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        enCalendar.setVisibility(View.GONE);
        long now = System.currentTimeMillis();
        final Date date = new Date(now);
        final SimpleDateFormat stdate = new SimpleDateFormat("yyyy-MM");
        String getTime = stdate.format(date);
        stYM.setText(getTime);
        stYM.setVisibility(View.GONE);
        enYM.setText(getTime);
        enYM.setVisibility(View.GONE);

        listview = findViewById(R.id.placeList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listview.setLayoutManager(layoutManager);

        placeList = new ArrayList<>();

        stCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                SimpleDateFormat stdate = new SimpleDateFormat("yyyy.MM.dd");
                stDate.setText(stdate.format(dateClicked));
                start = dateClicked.getTime();
                stCalendar.setVisibility(View.GONE);
                stYM.setVisibility(View.GONE);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                stYM.setText(stdate.format(firstDayOfNewMonth));
            }
        });

        enCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                SimpleDateFormat stdate = new SimpleDateFormat("yyyy.MM.dd");
                enDate.setText(stdate.format(dateClicked));
                end = dateClicked.getTime();
                enCalendar.setVisibility(View.GONE);
                enYM.setVisibility(View.GONE);
                long days = (end-start)/(1000*60*60*24);
                if (stDate.length() != 0 && enDate.length() != 0) {
                    Day = new ArrayList<>();
                    for (int i = 0; i <= days; i++) {
                        Day.add("Day " + (i + 1));
                    }
                    spinnerClick(daySpinner, Day);
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                enYM.setText(stdate.format(firstDayOfNewMonth));
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            placeList = (ArrayList<MyCourse>) data.getSerializableExtra("placeList");
            placeListAdapter = new PlaceListAdapter(this, placeList, onLongClickItem);
            listview.setAdapter(placeListAdapter);
        }
    }

    private View.OnLongClickListener onLongClickItem = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            listview.removeView(v);
            return false;
        }
    };

    public void spinnerClick(Spinner spinner, List list){
        SpinnerAdapter adapter = new SpinnerAdapter(PlanLastStep.this, list);
        spinner.setAdapter(adapter);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.y_pickStartDate:
                stCalendar.setVisibility(View.VISIBLE);
                stYM.setVisibility(View.VISIBLE);
                break;
            case R.id.y_pickEndDate:
                enCalendar.setVisibility(View.VISIBLE);
                enYM.setVisibility(View.VISIBLE);
                break;
            case R.id.y_save_plan:
//                Intent intent = new Intent(PlanLastStep.this, MapActivity.class);
//                startActivity(intent);
                Toast.makeText(PlanLastStep.this, "계획 저장 완료!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.y_addPlace:
                Intent intent = new Intent(PlanLastStep.this, MapActivity.class);
                intent.putExtra("courseList", placeList);
                startActivityForResult(intent, 300);
        }
    }
}
