package ddwucom.mobile.travel;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GRecordDayActivity extends AppCompatActivity {
    ImageView btnHome, btnGroup, btnCourse, btnMap;
    private static final String TAG = "GRecordDayActivity";
    private StorageReference storageRef;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase database;
    private DatabaseReference dbRefRecord;

    ImageButton btnAddGRecordContent;
    EditText etGRecordTitle;
    EditText etGRecordDate;
    Calendar calendar;
    String dateFormat;
    SimpleDateFormat sdf;
    Boolean isNew;
    String recordKey;
    String title;
    String date;

    RecyclerView recyclerView;
    RecordDayAdapter recordDayAdapter;
    LinearLayoutManager layoutManager;

    List<RecordContent> recordContents;
    String currentUid;
    String currentGid;
    String currentNickname;

    DatePickerDialog.OnDateSetListener recordDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            etGRecordDate.setText(sdf.format(calendar.getTime()));
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_record_day);

        btnHome = findViewById(R.id.btn_home);
        btnGroup = findViewById(R.id.btn_friends);
        btnCourse = findViewById(R.id.btn_course);
        btnMap = findViewById(R.id.btn_map);

        database = FirebaseDatabase.getInstance();

        isNew = (boolean) getIntent().getSerializableExtra("isNew");
        currentUid = getIntent().getStringExtra("currentUid");
        currentGid = getIntent().getStringExtra("currentGid");
        Log.d("goeun", "그룹" + currentGid);
        currentNickname = getIntent().getStringExtra("currentNickname");
        Log.d("goeun", currentNickname);

        if (isNew) {
            recordKey = database.getReference("group_records").push().getKey();
        } else {
            recordKey = (String) getIntent().getSerializableExtra("recordKey");
        }

        btnAddGRecordContent = findViewById(R.id.btnAddGRecordContent);
        etGRecordDate = findViewById(R.id.etGRecordDate);
        etGRecordTitle = findViewById(R.id.etGRecordTitle);
        recyclerView = findViewById(R.id.rvGRecordDay);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recordContents = new ArrayList<>();
        recordDayAdapter = new RecordDayAdapter(this, true, recordContents);
        recyclerView.setAdapter(recordDayAdapter);

        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference("record_images");

        dbRefRecord = database.getReference("group_records/" + recordKey);

        // 일기 가져오기
        getRecordContents();

        if (!isNew) {
            dbRefRecord.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Map<String, Object> data = (Map) snapshot.getValue();
                    etGRecordDate.setText(data.get("recordDate").toString());
                    etGRecordTitle.setText(data.get("recordTitle").toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        // 날짜
        calendar = Calendar.getInstance();
        dateFormat = "yyyy-MM-dd";
        sdf = new SimpleDateFormat(dateFormat, Locale.KOREA);

        etGRecordDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(GRecordDayActivity.this, R.style.DialogTheme, recordDatePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    // 아래로 스크롤
                    btnAddGRecordContent.setVisibility(View.INVISIBLE);
                    btnAddGRecordContent.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.fade_out));
                } else if (dy < 0) {
                    // 위로 스크롤
                    btnAddGRecordContent.setVisibility(View.VISIBLE);
                    btnAddGRecordContent.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.fade_in));
                }

            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddGRecordContent:
                title = etGRecordTitle.getText().toString();
                date = etGRecordDate.getText().toString();

                if (title.matches("") || date.matches("")) {
                    Toast.makeText(this, "필수 항목을 입력하세요!", Toast.LENGTH_LONG).show();
                    return;
                }
                saveRecordInDB();
                Intent intent = new Intent(this, AddRecordActivity.class);
                intent.putExtra("currentUid", currentUid);
                intent.putExtra("currentGid", currentGid);
                intent.putExtra("currentNickname", currentNickname);
                intent.putExtra("recordKey", recordKey);
                intent.putExtra("isGroup", true);
                startActivity(intent);
                break;
            case R.id.btn_home:
                btnHome.setImageResource(R.drawable.home_icon_yellow);
                btnGroup.setImageResource(R.drawable.friends_icon_grey);
                btnCourse.setImageResource(R.drawable.course_icon_grey);
                btnMap.setImageResource(R.drawable.map_icon_grey);
                Intent home = new Intent(GRecordDayActivity.this, HomeActivity.class);
                startActivity(home);
                break;
            case R.id.btn_friends:
                btnHome.setImageResource(R.drawable.home_icon_grey);
                btnGroup.setImageResource(R.drawable.friends_icon_yellow);
                btnCourse.setImageResource(R.drawable.course_icon_grey);
                btnMap.setImageResource(R.drawable.map_icon_grey);
                Intent list = new Intent(GRecordDayActivity.this, GroupListActivity.class);
                startActivity(list);
                break;
            case R.id.btn_map:
                btnHome.setImageResource(R.drawable.home_icon_grey);
                btnGroup.setImageResource(R.drawable.friends_icon_grey);
                btnCourse.setImageResource(R.drawable.course_icon_grey);
                btnMap.setImageResource(R.drawable.map_icon_yellow);
                Intent map = new Intent(GRecordDayActivity.this, OnlyMap.class);
                startActivity(map);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        title = etGRecordTitle.getText().toString();
        date = etGRecordDate.getText().toString();

        if (title.matches("") && date.matches("")) {
            finish();
            return;
        }

        if (title.matches("") || date.matches("")) {
            Toast.makeText(this, "필수 항목을 입력하세요!", Toast.LENGTH_LONG).show();
            return;
        }

        saveRecordInDB();
        finish();
    }

    public void saveRecordInDB() {
        Record record = new Record();
        record.setGid(currentGid);
        Log.d("goeun", "저장"+currentNickname);
        record.setNickname(currentNickname);
        record.setRecordTitle(title);
        record.setRecordDate(date);
        if (isNew) {
            dbRefRecord.setValue(record);
            isNew = false;
        }
        else {
            dbRefRecord.updateChildren(record.toMap());
        }
    }

    public void getRecordContents() {
        dbRefRecord.child("contents").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Map<String, Object> data = (Map) snapshot.getValue();
                String location = null;
                if (data.get("location") != null) {
                    location = data.get("location").toString();
                }
                String content = data.get("content").toString();
                List<String> images = new ArrayList<String>();
                if (data.get("images") != null) {
                    String strImages = data.get("images").toString();
                    Log.d("goeun", strImages);
                    String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
                    Pattern p = Pattern.compile(regex);
                    Matcher matcher = p.matcher(strImages);
                    while (matcher.find()) {
                        images.add(matcher.group(0));
                    }
                } else {
                    images.add(Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.g_no_image_icon).toString());
                }
                String nickname = null;
                if (data.get("nickname") != null) {
                    nickname = data.get("nickname").toString();
                }
                recordContents.add(new RecordContent(location, content, nickname, images));

                Log.d("goeun", String.valueOf(recordContents.size()));
                recordDayAdapter.notifyDataSetChanged();
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
}
