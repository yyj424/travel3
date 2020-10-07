package ddwucom.mobile.travel;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

    EditText etRecordFolder;
    EditText etRecordTitle;
    EditText etRecordDate;
    Calendar calendar;
    String dateFormat;
    SimpleDateFormat sdf;
    Boolean isNew;
    String recordKey;

    RecyclerView recyclerView;
    RecyclerView.Adapter recordDayAdapter;
    RecyclerView.LayoutManager layoutManager;
    
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

        isNew = (boolean) getIntent().getSerializableExtra("isNew");
        recordKey = (String) getIntent().getSerializableExtra("recordKey");
        currentUid = (String) getIntent().getSerializableExtra("currentUid");

        etRecordDate = findViewById(R.id.etRecordDate);
        etRecordTitle = findViewById(R.id.etRecordTitle);
        recyclerView = findViewById(R.id.record_day_recycler_view);
        spinner = findViewById(R.id.spRecordFolder);

        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference("record_images");
        database = FirebaseDatabase.getInstance();
        dbRefRecord = database.getReference("records/" + recordKey);
        dbRefFolder = database.getReference("folders");

        // 폴더 추가 관리(수정, 삭제 구현 할말?)
        mgrRecordFolder();

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

        // DB 접근


//        dbRefRecord.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Query query = snapshot.getRef().child("uid").equalTo(currentUid);
//
//                if (recordContentDB != null) {
//                    // 일기 리스트
//                    recyclerView.setHasFixedSize(true);
//                    layoutManager = new LinearLayoutManager(this);
//                    recyclerView.setLayoutManager(layoutManager);
//
//                    recordDayAdapter = new RecordDayActivity(recordContent);
//                    recyclerView.setAdapter(recordDayAdapter)
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

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
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
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
                        ((ViewGroup)addFolderLayout.getParent()).removeView(addFolderLayout);
                    }
                    alertDialog.show();
                    alertDialog.getWindow().setLayout(1200, 750);

                    TextView textView = alertDialog.findViewById(android.R.id.message);
                    Typeface face = Typeface.createFromAsset(getAssets(),"fonts/tmoney_regular.ttf");
                    textView.setTypeface(face);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddRecordContent:
                Intent intent = new Intent(this, AddRecordActivity.class);
                intent.putExtra("currentUid", currentUid);
                intent.putExtra("recordKey", recordKey);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        dbRefRecord = database.getReference("records/" + recordKey);
        String title = etRecordTitle.getText().toString();
        String date = etRecordDate.getText().toString();
        int folderPos = spinner.getSelectedItemPosition();

        if (recordContents == null && title.length() == 0 && date.length() == 0 && folderPos < 2) {
            finish();
        }

        if (recordContents != null && (title.length() == 0 || date.length() == 0|| folderPos < 2)) {
            Toast.makeText(this, "필수 항목을 입력하세요!", Toast.LENGTH_LONG).show();
            return;
        }

        dbRefRecord.child("uid").setValue(currentUid);
        dbRefRecord.child("recordDate").setValue(date);
        dbRefRecord.child("recordFolder").setValue(spinner.getSelectedItem().toString());
        dbRefRecord.child("recordTitle").setValue(title);
        finish();
    }
}