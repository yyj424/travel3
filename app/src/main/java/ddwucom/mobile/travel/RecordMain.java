package ddwucom.mobile.travel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecordMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private FirebaseUser user;
    private String currentUid;

    RecordAdapter recordAdapter;
    ArrayList<String> folders;
    List<Record> recordList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_main);

        // DB
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("records");
        //database.getReference("records").child(recordKey).child("contents").push();

        // currentUser
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUid = user.getUid();
        }
        Log.d("goeun", currentUid);
//        else {
//            Toast.makeText(this, "로그인이 필요합니다!", Toast.LENGTH_SHORT).show();
//        }

        mgrRecordFolder();

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_btn);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getRecords();

        recordList = new ArrayList<>();
        recyclerView = findViewById(R.id.record_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recordAdapter = new RecordAdapter(this, recordList);
//        recordAdapter.setClickListener(this);
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
    }

    // 폴더 관리
    public void mgrRecordFolder() {
        DatabaseReference dbRefFolder = database.getReference("folders");

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
    }

    public void getRecords() {
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
                    recordList.add(new Record(key, thumbnailImg, recordTitle, recordDate));

                    Log.d("goeun", String.valueOf(recordList.size()));
                    recordAdapter.notifyDataSetChanged();
                }
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
