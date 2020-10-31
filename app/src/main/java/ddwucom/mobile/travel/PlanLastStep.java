package ddwucom.mobile.travel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    ArrayList<String> keyList;
    private RecyclerView listview;
    private ArrayList<MyCourse> placeList = null;
    PlaceListAdapter placeListAdapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference dayDBRef;
    private DatabaseReference userDBRef;
    private DatabaseReference planDBRef;
    private DatabaseReference dayListDBRef;
    String uid;
    long t;

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
        keyList = new ArrayList<>();

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
                    //spinnerClick(daySpinner, Day);
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
            if (ct - t > 200) {
                planDBRef = firebaseDatabase.getReference("plan_list");
                dayListDBRef = firebaseDatabase.getReference("days_list");
                // if(planDBRef != null) {//plandb가 있다면 읽어올것임 //그거에 대한 daydb를 찾아올것임
                planDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int cnt = 0;
                        int i = 0;
                        int pos = position;
                        for (DataSnapshot s : snapshot.getChildren()) {
                            if (s.child("uid").getValue().toString().equals(uid) && s.child("planName").getValue().toString().equals(planName.getText().toString())) {
                                dayDBRef = s.child("dayList").getRef();//나중에 수정할 떄 쓸 수 있을수도있어서
                                placeList.clear();
                                Log.d("yyj", "리스트 클리어");
//                                for (DataSnapshot ds : s.child("dayList").getChildren()) {
//                                    //ds.child가 days_List에 저장된 애랑 비교해서 가져오기
//                                    if (cnt == pos) {
//                                        Log.d("yyj", "cnt == pos");
//                                        Map<String, Object> map = (Map) ds.getValue();
//                                        for (Map.Entry<String, Object> entry : map.entrySet()) {
//                                            placeList.add(new MyCourse(i + 1, entry.getKey(), (String) entry.getValue()));
//                                            i++;
//                                            Log.d("yyj", "리스트에 추가됨 : " + i + "번째");
//                                        }
//                                        break;
//                                    }
//                                    cnt++;
//                                }
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                dayListDBRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        int i = 0;
                        if(keyList.size() > 0) {
                            for (DataSnapshot s : snapshot.getChildren()) {
                                Log.d("yyj", "day스냅샷 for문 안");
                                if (s.getKey() == keyList.get(i)) {
                                    Log.d("yyj", "if 키리스트 0" + keyList.get(i) + "이랑 s.getKey값이 같으면" + s.getKey());
                                    Map<String, Object> map = (Map) s.getValue();
                                    Log.d("yyj", "리스트 추가 끝");
                                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                                        Log.d("yyj", "DAY 맵 값" + entry.getKey() + ", " + (String) entry.getValue());
                                        placeList.add(new MyCourse(i + 1, entry.getKey(), (String) entry.getValue()));
                                        Log.d("yyj", "리스트에 추가됨 : " + i + "번째");
                                    }
                                    i++;
                                }
                            }
                        }
                        Log.d("yyj", "리스트 추가 끝");
                        placeListAdapter = new PlaceListAdapter(PlanLastStep.this, placeList, onLongClickItem);
                        Log.d("yyj", "어댑터 리스트생성");
                        listview.setAdapter(placeListAdapter);
                        Log.d("yyj", "어댑터 셋팅");
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                if (planName.getText() != null && placeList.size() > 0) {
                    addkey();
                    MyPlan plan = new MyPlan();
                    plan.setUid(uid);
                    plan.setPlanName(planName.getText().toString());
                    plan.setStartDate(stDate.getText().toString());
                    plan.setEndDate(enDate.getText().toString());
                    plan.setDaysList(keyList);
                    databaseReference.child("plan_list").push().setValue(plan);
                    Log.d("yyj", "plan db 푸시");
                }
                //daydb에서 수정하고 싶을떄
                //daydb에서 삭제하고 싶을때
                //daydb에서 추가하고 싶을때
                //  }
                // else {//맨처음에 plandb가 없으니까 선택한 day 값이랑 plandb내용 추가함

                //  }
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    @SuppressLint("RestrictedApi")
    public void addkey() {
        Map<String, Object> day = new HashMap<>();
        for (int i = 0; i < placeList.size(); i++) {
            day.put(placeList.get(i).getPlaceName(), placeList.get(i).getMemo());
        }
        final String key = databaseReference.child("days_list").push().getKey();Log.d("yyj", "데이 리스트 푸시");
        databaseReference.child("days_list").child(key).updateChildren(day);Log.d("yyj", String.valueOf(databaseReference.child("days_list").getPath()));
        keyList.add(key);
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

//    public void spinnerClick(Spinner spinner, List list) {
//        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                userDBRef = firebaseDatabase.getReference("user_list");
//                userDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot s : snapshot.getChildren()) {
//                            if (s.child("uid").getValue().toString().equals(uid)) {
//                                MyDay day = new MyDay();
//                                day.setDay();
//                                break;
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//            }
//        });
//        SpinnerAdapter adapter = new SpinnerAdapter(PlanLastStep.this, list);
//        spinner.setAdapter(adapter);
//    }

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
                break;
        }
    }
}
