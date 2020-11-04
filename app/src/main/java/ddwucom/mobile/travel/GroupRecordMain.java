package ddwucom.mobile.travel;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
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
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GroupRecordMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "GroupRecordMain";

    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private String currentUid;
    private String currentNickname;
    private String currentGid;
    private String groupName;

    RecordAdapter recordAdapter;
    List<Record> recordList;
    RecyclerView recyclerView;
    ImageButton btnAddGroupRecord;
    Calendar calendar;
    String dateFormat;
    SimpleDateFormat sdf;
    TextView tvGroupRecordTitle;

    DatePickerDialog.OnDateSetListener recordDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            selectDate(sdf.format(calendar.getTime()));
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_record_main);

        // DB
        database = FirebaseDatabase.getInstance();

        currentNickname = (String) getIntent().getSerializableExtra("currentNickname");
        currentUid = (String) getIntent().getSerializableExtra("currentUid");
        currentGid = (String) getIntent().getSerializableExtra("currentGid");
        groupName = (String) getIntent().getSerializableExtra("groupName");

        Toolbar tbGroupRecord = findViewById(R.id.tbGroupRecord);
        setSupportActionBar(tbGroupRecord);
        getSupportActionBar().setTitle(null);

        // 날짜
        calendar = Calendar.getInstance();
        dateFormat = "yyyy-MM-dd";
        sdf = new SimpleDateFormat(dateFormat, Locale.KOREA);

        recordList = new ArrayList<>();
        btnAddGroupRecord = findViewById(R.id.btnAddGroupRecord);
        recyclerView = findViewById(R.id.rvGroupRecord);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recordAdapter = new RecordAdapter(this, true, recordList);
        recyclerView.setAdapter(recordAdapter);

        recordAdapter.setOnItemClickListener(new RecordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                String key = recordAdapter.getItem(pos).getKey();
                Log.d("goeun", "click");
                Intent intent = new Intent(GroupRecordMain.this, GRecordDayActivity.class);
                intent.putExtra("currentUid", currentUid);
                intent.putExtra("currentGid", currentGid);
                intent.putExtra("currentNickname", currentNickname);
                intent.putExtra("isNew", false);
                intent.putExtra("recordKey", key);
                startActivity(intent);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    // 아래로 스크롤
                    btnAddGroupRecord.setVisibility(View.INVISIBLE);
                    btnAddGroupRecord.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.fade_out));
                } else if (dy < 0) {
                    // 위로 스크롤
                    btnAddGroupRecord.setVisibility(View.VISIBLE);
                    btnAddGroupRecord.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.fade_in));
                }

            }
        });

        tvGroupRecordTitle = findViewById(R.id.tvGroupRecordTitle);
        tvGroupRecordTitle.setText(groupName);
    }

    public void onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnRecordDate:
                new DatePickerDialog(this, R.style.DialogTheme, recordDatePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    public void selectDate(final String date) {
        recordList.clear();
        dbRef = database.getReference("group_records");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s:snapshot.getChildren()) {
                    Map<String, Object> data = (Map) s.getValue();
                    if (data.get("recordDate").equals(date) && s.getKey().equals(currentGid)) {
                        String key = s.getKey();
                        String recordTitle = null;
                        if (data.get("recordTitle") != null) {
                            recordTitle = data.get("recordTitle").toString();
                        }
                        String recordDate = null;
                        if (data.get("recordDate") != null) {
                            recordDate = data.get("recordDate").toString();
                        }
                        String thumbnailImg = null;
                        if (data.get("thumbnailImg") != null) {
                            thumbnailImg = data.get("thumbnailImg").toString();
                        }
                        else {
                            thumbnailImg = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.g_no_image_icon).toString();
                        }
                        String nickname = data.get("nickname").toString();
                        Log.d("goeun", key + thumbnailImg + recordTitle + recordDate);
                        recordList.add(new Record(key, nickname, thumbnailImg, recordTitle, recordDate));
                        Collections.sort(recordList, new Record.SortByDate());

                        Log.d("goeun", String.valueOf(recordList.size()));
                        recordAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getRecords() {
        recordList.clear();
        dbRef = database.getReference("group_records");
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Map<String, Object> data = (Map) snapshot.getValue();
                if (data.get("gid").equals(currentGid)) {
                    String key = snapshot.getKey();
                    Log.d("goeun", key);
                    String recordTitle = null;
                    if (data.get("recordTitle") != null) {
                        recordTitle = data.get("recordTitle").toString();
                    }
                    String recordDate = null;
                    if (data.get("recordDate") != null) {
                        recordDate = data.get("recordDate").toString();
                    }
                    String thumbnailImg = null;
                    if (data.get("thumbnailImg") != null) {
                        thumbnailImg = data.get("thumbnailImg").toString();
                    }
                    else {
                        thumbnailImg = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.g_no_image_icon).toString();
                    }
                    String nickname = data.get("nickname").toString();
                    Log.d("goeun", key + thumbnailImg + recordTitle + recordDate);
                    recordList.add(new Record(key, nickname, thumbnailImg, recordTitle, recordDate));
                    Collections.sort(recordList, new Record.SortByDate());

                    Log.d("goeun", String.valueOf(recordList.size()));
                    recordAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("goeun", snapshot.getValue().toString());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_record_calendar, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRecords();
        recordAdapter.notifyDataSetChanged();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddGroupRecord:
                Intent intent = new Intent(this, GRecordDayActivity.class);
                intent.putExtra("currentUid", currentUid);
                intent.putExtra("currentGid", currentGid);
                intent.putExtra("currentNickname", currentNickname);
                intent.putExtra("isNew", true);
                startActivity(intent);
                break;
        }
    }
}
