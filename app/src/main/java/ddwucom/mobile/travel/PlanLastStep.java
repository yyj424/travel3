package ddwucom.mobile.travel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanLastStep extends AppCompatActivity {
    EditText memo;
    EditText planName;
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
    Map<String, Object> keyList;
    private RecyclerView listview;
    private ArrayList<MyCourse> placeList = null;
    PlaceListAdapter placeListAdapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference planDBRef;
    private DatabaseReference dayListDBRef;
    private DatabaseReference getPlanDBRef;
    String uid;
    long t;
    int pos = 0;
    String key;
    String pname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_3);

        t = System.currentTimeMillis();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        planName = findViewById(R.id.y_etPlanName);
        stDate = findViewById(R.id.y_pickStartDate);
        enDate = findViewById(R.id.y_pickEndDate);
        daySpinner = findViewById(R.id.daySpinner);
        stYM = findViewById(R.id.stYM);
        enYM = findViewById(R.id.enYM);
        memo = findViewById(R.id.y_placeMemo);

        stCalendar = (CompactCalendarView) findViewById(R.id.y_startCalendar);
        stCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        enCalendar = (CompactCalendarView) findViewById(R.id.y_EndCalendar);
        enCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        long now = System.currentTimeMillis();
        final Date date = new Date(now);
        final SimpleDateFormat stdate = new SimpleDateFormat("yyyy-MM");
        String getTime = stdate.format(date);
        stYM.setText(getTime);
        enYM.setText(getTime);

        listview = findViewById(R.id.placeList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listview.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        pname = (String) intent.getSerializableExtra("pname");

            if(pname == null) {
                planDBRef = databaseReference.child("plan_list").push();
            }
            else {
                planDBRef = firebaseDatabase.getReference("plan_list");
                planDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot s : snapshot.getChildren()) {
                            if (s.child("uid").getValue().toString().equals(uid) && s.child("planName").getValue().toString().equals(pname)) {
                                planName.setText(pname);
                                stDate.setText(s.child("startDate").getValue().toString());
                                enDate.setText(s.child("endDate").getValue().toString());
                                for (DataSnapshot snap:s.child("daysList").getChildren()) {
                                    keyList.put(snap.getKey(), snap.getValue());
                                }
                                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");
                                try {
                                    Date sdate = transFormat.parse(stDate.getText().toString());
                                    Date edate = transFormat.parse(enDate.getText().toString());
                                    long days = (edate.getTime() - sdate.getTime()) / (1000 * 60 * 60 * 24);
                                    if (stDate.length() != 0 && enDate.length() != 0) {
                                        Day = new ArrayList<>();
                                        for (int i = 0; i <= days; i++) {
                                            Day.add("Day " + (i + 1));
                                        }
                                        SpinnerAdapter adapter = new SpinnerAdapter(PlanLastStep.this, Day);
                                        daySpinner.setAdapter(adapter);
                                        daySpinner.setOnItemSelectedListener(daySelectedListener);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                planDBRef = s.getRef();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        placeList = new ArrayList<>();
        keyList = new HashMap<>();

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
                long days = (end - start) / (1000 * 60 * 60 * 24);
                if (stDate.length() != 0 && enDate.length() != 0) {
                    Day = new ArrayList<>();
                    for (int i = 0; i <= days; i++) {
                        Day.add("Day " + (i + 1));
                    }
                    SpinnerAdapter adapter = new SpinnerAdapter(PlanLastStep.this, Day);
                    daySpinner.setAdapter(adapter);
                    daySpinner.setOnItemSelectedListener(daySelectedListener);
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                enYM.setText(stdate.format(firstDayOfNewMonth));
            }
        });
    }

    AdapterView.OnItemSelectedListener daySelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> spinner, View container,
                                   final int position, long id) {
            long ct = System.currentTimeMillis();
            if ((ct - t)*1000 > 0.00001) {
                dayListDBRef = firebaseDatabase.getReference("days_list");
                if (keyList.containsKey(String.valueOf(position))) {//키리스트에 지금 누른 값이 있으면 디비를 불러올것임
                    dayListDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int i = 1;
                            for (DataSnapshot s : snapshot.getChildren()) {
                                if (s.getKey().equals(keyList.get(String.valueOf(position)))) {
                                    Map<String, Object> map = (Map) s.getValue();
                                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                                        placeList.add(new MyCourse(i, entry.getKey(), (String) entry.getValue()));
                                        i++;
                                    }
                                    break;
                                }
                            }
                            placeListAdapter = new PlaceListAdapter(PlanLastStep.this, placeList, onLongClickItem);
                            listview.setAdapter(placeListAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    daysValue();
                    plan();
                }
                else {
                    if (planName.getText() != null && placeList.size() > 0) {
                        daysValue();
                        plan();
                    }
                }
                pos = position;
                placeList.clear();
                placeListAdapter = new PlaceListAdapter(PlanLastStep.this, placeList, onLongClickItem);
                listview.setAdapter(placeListAdapter);
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    public void plan() {
        if (placeList.size() > 0) {
            MyPlan plan = new MyPlan();
            plan.setUid(uid);
            plan.setPlanName(planName.getText().toString());
            plan.setStartDate(stDate.getText().toString());
            plan.setEndDate(enDate.getText().toString());
            plan.setDaysList(keyList);
            planDBRef.setValue(plan);
        }
    }

    public void daysValue() {
        Map<String, Object> day = new HashMap<>();
        for (int i = 0; i < placeList.size(); i++) {
            day.put(placeList.get(i).getPlaceName(), placeList.get(i).getMemo());
        }
        if(keyList.containsKey(String.valueOf(pos)))//키가 있을때
            key = (String) keyList.get(String.valueOf(pos));
        else {
            key = databaseReference.child("days_list").push().getKey();
        }
        if(day.size() > 0) {
            keyList.put(String.valueOf(pos), key);
            databaseReference.child("days_list").child(key).updateChildren(day);
        }
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
                if (planName.getText() != null && placeList.size() > 0) {
                    daysValue();
                    plan();
                    Toast.makeText(PlanLastStep.this, "계획 저장 완료!", Toast.LENGTH_SHORT).show();
                    Intent planListintent = new Intent(PlanLastStep.this, PlanList.class);
                    startActivity(planListintent);
                    finish();
                }
                else
                    Toast.makeText(PlanLastStep.this, "계획을 제대로 완성하세요!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.y_addPlace:
                Intent intent = new Intent(PlanLastStep.this, MapActivity.class);
                intent.putExtra("courseList", placeList);
                startActivityForResult(intent, 300);
                break;
        }
    }
}
