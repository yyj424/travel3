package ddwucom.mobile.travel;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class RecordDayActivity extends AppCompatActivity {
    private StorageReference storageRef;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase database;
    private DatabaseReference dbRefRecord;
    private DatabaseReference dbRefFolder;

    LinearLayout addFolderLayout;
    ArrayAdapter<String> spinnerAdapter;
    Spinner spinner;
    List<String> folders;

    ImageButton btnAddRecordContent;
    EditText etRecordFolder;
    EditText etRecordTitle;
    EditText etRecordDate;
    Calendar calendar;
    String dateFormat;
    SimpleDateFormat sdf;
    Boolean isNew;
    String recordKey;
    String title;
    String date;
    int folderPos;

    RecyclerView recyclerView;
    RecordDayAdapter recordDayAdapter;
    LinearLayoutManager layoutManager;

    List<RecordContent> recordContents;
    String currentUid;

    DatePickerDialog.OnDateSetListener recordDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            etRecordDate.setText(sdf.format(calendar.getTime()));
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_day);

        addFolderLayout = (LinearLayout) View.inflate(this, R.layout.add_folder_layout, null);

        database = FirebaseDatabase.getInstance();

        isNew = (boolean) getIntent().getSerializableExtra("isNew");
        currentUid = (String) getIntent().getSerializableExtra("currentUid");
        if (isNew) {
            recordKey = database.getReference("records").push().getKey();
        } else {
            recordKey = (String) getIntent().getSerializableExtra("recordKey");
        }
        // 임시!! RecordMain 구현시 꼭 지워야함
        recordKey = "-MJarmh2RX8AUNA-r6j_";
        isNew = false;

        btnAddRecordContent = findViewById(R.id.btnAddRecordContent);
        spinner = findViewById(R.id.spRecordFolder);
        etRecordDate = findViewById(R.id.etRecordDate);
        etRecordTitle = findViewById(R.id.etRecordTitle);
        recyclerView = findViewById(R.id.record_day_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recordContents = new ArrayList<>();
        recordDayAdapter = new RecordDayAdapter(this, recordContents);
        recyclerView.setAdapter(recordDayAdapter);

        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference("record_images");

        dbRefFolder = database.getReference("folders");
        dbRefRecord = database.getReference("records/" + recordKey);

        // 폴더 추가 관리(수정, 삭제 구현 할말?)
        mgrRecordFolder();

        // 일기 가져오기
        getRecordContents();

        if (!isNew) {
            dbRefRecord.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Map<String, Object> data = (Map) snapshot.getValue();
                    etRecordDate.setText(data.get("recordDate").toString());
                    spinner.setSelection(folders.indexOf(data.get("recordFolder").toString()));
                    etRecordTitle.setText(data.get("recordTitle").toString());
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

        etRecordDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RecordDayActivity.this, R.style.DialogTheme, recordDatePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    // 아래로 스크롤
                    btnAddRecordContent.setVisibility(View.INVISIBLE);
                    btnAddRecordContent.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.fade_out));
                } else if (dy < 0) {
                    // 위로 스크롤
                    btnAddRecordContent.setVisibility(View.VISIBLE);
                    btnAddRecordContent.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.fade_in));
                }

            }
        });
    }

    // 폴더 관리
    public void mgrRecordFolder() {
        folders = new ArrayList<String>();
        folders.add("폴더 선택하기");
        folders.add("폴더 추가하기");

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

        // spinner에 데이터 추가하기
        spinnerAdapter = new ArrayAdapter<>(this, R.layout.record_spinner_item, folders);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {

                    AlertDialog alertDialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecordDayActivity.this, R.style.DialogTheme);

                    builder.setTitle("폴더 추가하기")
                            .setView(addFolderLayout)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etRecordFolder = addFolderLayout.findViewById(R.id.etRecordFolder);
                                    String addFolderName = etRecordFolder.getText().toString();

                                    if (folders.contains(addFolderName)) {
                                        Toast.makeText(RecordDayActivity.this, "이미 존재하는 폴더입니다!", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else {
                                        dbRefFolder.child(currentUid).child("uid").setValue(currentUid);
                                        dbRefFolder.child(currentUid).child("folderNames").push().setValue(addFolderName);
                                        spinnerAdapter.notifyDataSetChanged();
                                    }
                                }
                            })
                            .setNegativeButton("CANCEL", null);
                    alertDialog = builder.create();
                    if (addFolderLayout.getParent() != null) {
                        ((ViewGroup) addFolderLayout.getParent()).removeView(addFolderLayout);
                    }
                    alertDialog.show();
                    alertDialog.getWindow().setLayout(1200, 750);

                    TextView textView = alertDialog.findViewById(android.R.id.message);
                    Typeface face = Typeface.createFromAsset(getAssets(), "fonts/tmoney_regular.ttf");
                    textView.setTypeface(face);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddRecordContent:
                title = etRecordTitle.getText().toString();
                date = etRecordDate.getText().toString();
                folderPos = spinner.getSelectedItemPosition();

                if (title.matches("") || date.matches("") || folderPos < 2) {
                    Toast.makeText(this, "필수 항목을 입력하세요!", Toast.LENGTH_LONG).show();
                    return;
                }
                saveRecordInDB();

                Intent intent = new Intent(this, AddRecordActivity.class);
                intent.putExtra("currentUid", currentUid);
                intent.putExtra("recordKey", recordKey);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        title = etRecordTitle.getText().toString();
        date = etRecordDate.getText().toString();
        folderPos = spinner.getSelectedItemPosition();

        if (title.matches("") && date.matches("") && folderPos < 2) {
            finish();
            return;
        }

        if (title.matches("") || date.matches("") || folderPos < 2) {
            Toast.makeText(this, "필수 항목을 입력하세요!", Toast.LENGTH_LONG).show();
            return;
        }

        saveRecordInDB();
        finish();
    }

    public void saveRecordInDB() {
        dbRefRecord.child("uid").setValue(currentUid);
        dbRefRecord.child("recordDate").setValue(date);
        dbRefRecord.child("recordFolder").setValue(spinner.getSelectedItem().toString());
        dbRefRecord.child("recordTitle").setValue(title);
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
                }
                else {
                    images.add(Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.g_no_image_icon).toString());
                }
                recordContents.add(new RecordContent(location, content, images));

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