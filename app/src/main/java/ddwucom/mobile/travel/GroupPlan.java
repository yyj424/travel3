package ddwucom.mobile.travel;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupPlan extends AppCompatActivity {
    EditText memo;
    TextView planName;
    Spinner spinner;
    String stDate;
    String enDate;
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
    String gKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_plan);

        t = System.currentTimeMillis();

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        uid = user.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        planName = findViewById(R.id.y_group_plan_name);
        spinner = findViewById(R.id.y_group_plan_spinner);
        memo = findViewById(R.id.y_placeMemo);

        placeList = new ArrayList<>();
        keyList = new HashMap<>();

        listview = findViewById(R.id.y_group_plan_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listview.setLayoutManager(layoutManager);

//        Intent intent = getIntent();
//        gKey = (String) intent.getSerializableExtra("groupKey");
        gKey = "-ML7VAsNH5KHPqnBrBry";

            planDBRef = firebaseDatabase.getReference("Groups");
            planDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot s : snapshot.getChildren()) {
                        if (s.getKey().equals(gKey)) {
                            planName.setText(s.child("groupName").getValue().toString());
                            Log.d("yyj", String.valueOf(s.child("daysList").getValue()));
                            ArrayList<String> daysList = (ArrayList<String>) s.child("daysList").getValue();
                            if(daysList != null) {
                                for (int i = 0; i < daysList.size(); i++) {
                                    keyList.put(String.valueOf(i), daysList.get(i));
                                }
                            }
                            stDate = s.child("startDate").getValue().toString();
                            enDate = s.child("endDate").getValue().toString();
                            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date sdate = transFormat.parse(stDate);
                                Date edate = transFormat.parse(enDate);
                                long days = (edate.getTime() - sdate.getTime()) / (1000 * 60 * 60 * 24);
                                if (stDate.length() != 0 && enDate.length() != 0) {
                                    Day = new ArrayList<>();
                                    for (int i = 0; i <= days; i++) {
                                        Day.add("Day " + (i + 1));
                                    }
                                    SpinnerAdapter adapter = new SpinnerAdapter(GroupPlan.this, Day);
                                    spinner.setAdapter(adapter);
                                    spinner.setOnItemSelectedListener(daySelectedListener);
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

    AdapterView.OnItemSelectedListener daySelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> spinner, View container,
                                   final int position, long id) {
            long ct = System.currentTimeMillis();
            if ((ct - t)*1000 > 0.00001) {
                Log.d("yyj", "<<<<<<<<<<<스피너 클릭!!!!!! : " + position + " 번째자리, Day : " + (position+1) +" >>>>>>>>>>>>>");
                dayListDBRef = firebaseDatabase.getReference("days_list");
                Log.d("yyj", "리스트 사이즈!!!!!" + String.valueOf(placeList.size()));
                Log.d("yyj", "contains Key!!!!!  : " + String.valueOf(keyList.containsKey(String.valueOf(position))) + "***키가 있는지 없는지***");
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
                                        Log.d("yyj", "리스트에 추가됨 : " + i + "번째");
                                        i++;
                                    }
                                    break;
                                }
                            }

                            Log.d("yyj", "리스트 추가 끝");
                            placeListAdapter = new PlaceListAdapter(GroupPlan.this, placeList, onLongClickItem);
                            Log.d("yyj", "어댑터 리스트생성");
                            listview.setAdapter(placeListAdapter);
                            Log.d("yyj", "어댑터 셋팅");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    daysValue();
                    plan();
                }
                else {
                    Log.d("yyj", "else문 들어왔음");
                    if (planName.getText() != null && placeList.size() > 0) {
                        daysValue();
                        plan();
                    }
                }
                pos = position;
                placeList.clear();
                placeListAdapter = new PlaceListAdapter(GroupPlan.this, placeList, onLongClickItem);
                listview.setAdapter(placeListAdapter);
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    public void plan() {
        if (placeList.size() > 0) {
            planDBRef.child("daysList").updateChildren(keyList);
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
            Log.d("yyj", "데이 리스트 푸시");
            keyList.put(String.valueOf(pos), key);
        }
        databaseReference.child("days_list").child(key).updateChildren(day);
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
            case R.id.y_group_plan_save:
                if (planName.getText() != null && placeList.size() > 0) {
                    daysValue();
                    plan();
                    Toast.makeText(GroupPlan.this, "계획 저장 완료!", Toast.LENGTH_SHORT).show();
//                    Intent planListintent = new Intent(GroupPlan.this, PlanList.class);
//                    startActivity(planListintent);
//                    finish();
                }
                else
                    Toast.makeText(GroupPlan.this, "계획을 제대로 완성하세요!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.y_group_plan_add_btn:
                Intent intent = new Intent(GroupPlan.this, MapActivity.class);
                intent.putExtra("courseList", placeList);
                startActivityForResult(intent, 300);
                break;
        }
    }
}
