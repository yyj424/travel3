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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class RecordMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private FirebaseUser user;
    private String currentUid;

    RecordAdapter recordAdapter;
    ArrayAdapter<String> folderAdapter;
    ArrayList<String> folders;
    List<Record> recordList;
    Spinner spinner;
    RecyclerView recyclerView;
    ImageButton btnAddRecord;
    Calendar calendar;
    String dateFormat;
    SimpleDateFormat sdf;

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
        setContentView(R.layout.activity_record_main);

        // DB
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("records");

        // currentUser
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUid = user.getUid();
        }

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        // 날짜
        calendar = Calendar.getInstance();
        dateFormat = "yyyy-MM-dd";
        sdf = new SimpleDateFormat(dateFormat, Locale.KOREA);

        folders = new ArrayList<>();
        recordList = new ArrayList<>();
        btnAddRecord = findViewById(R.id.btnAddRecord);
        recyclerView = findViewById(R.id.record_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recordAdapter = new RecordAdapter(this, recordList);
        recyclerView.setAdapter(recordAdapter);

        recordAdapter.setOnItemClickListener(new RecordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                String key = recordAdapter.getItem(pos).getKey();
                Log.d("goeun", "click");
                Intent intent = new Intent(RecordMain.this, RecordDayActivity.class);
                intent.putExtra("currentUid", currentUid);
                intent.putExtra("isNew", false);
                intent.putExtra("recordKey", key);
                intent.putStringArrayListExtra("folders", folders);
                startActivity(intent);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    // 아래로 스크롤
                    btnAddRecord.setVisibility(View.INVISIBLE);
                    btnAddRecord.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.fade_out));
                } else if (dy < 0) {
                    // 위로 스크롤
                    btnAddRecord.setVisibility(View.VISIBLE);
                    btnAddRecord.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.fade_in));
                }

            }
        });
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
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("records");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s:snapshot.getChildren()) {
                    Log.d("goeun", "k"
                            +s.getKey());
                    Map<String, Object> data = (Map) s.getValue();
                    Log.d("goeun", "rf" +data);
                    if (data.get("recordDate").equals(date) && data.get("uid").equals(currentUid)) {
                        String key = s.getKey();
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
                        Log.d("goeun", key + thumbnailImg + recordTitle + recordDate);
                        recordList.add(new Record(key, thumbnailImg, recordTitle, recordDate));
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

    public void selectFolder(final String folder) {
        Log.d("goeun", "ff" + folder);
        recordList.clear();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("records");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s:snapshot.getChildren()) {
                    Log.d("goeun", "k"
                            +s.getKey());
                    Map<String, Object> data = (Map) s.getValue();
                    Log.d("goeun", "rf" +data);
                    if (data.get("recordFolder").equals(folder) && data.get("uid").equals(currentUid)) {
                        String key = s.getKey();
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
                        Log.d("goeun", key + thumbnailImg + recordTitle + recordDate);
                        recordList.add(new Record(key, thumbnailImg, recordTitle, recordDate));
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

    // 폴더 관리
    public void mgrRecordFolder() {
        DatabaseReference dbRefFolder = database.getReference("folders");
        folders.clear();
        folders.add("전체");

        folderAdapter = new ArrayAdapter<>(this, R.layout.record_spinner_item, folders);
        spinner = findViewById(R.id.spRecordMainFolder);
        spinner.setAdapter(folderAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    getRecords();
                }
                else {
                    selectFolder(spinner.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 처음 실행 시 데이터 가져옴, 변경될 시 변경된 데이터만 가져옴(!! 수정 삭제 구현 할말 !!)
        dbRefFolder.child(currentUid).child("folderNames").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                folders.add((String) snapshot.getValue());
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


    }

    public void getRecords() {
        recordList.clear();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("records");
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Map<String, Object> data = (Map) snapshot.getValue();
                if (data.get("uid").equals(currentUid)) {
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
                    Log.d("goeun", key + thumbnailImg + recordTitle + recordDate);
                    recordList.add(new Record(key, thumbnailImg, recordTitle, recordDate));
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

    @Override
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
        mgrRecordFolder();
        //getRecords();

        recordAdapter.notifyDataSetChanged();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddRecord:
                Intent intent = new Intent(this, RecordDayActivity.class);
                intent.putExtra("currentUid", currentUid);
                intent.putExtra("isNew", true);
                intent.putStringArrayListExtra("folders", folders);
                startActivity(intent);
                break;
        }
    }
}
