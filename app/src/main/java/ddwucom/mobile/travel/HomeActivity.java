package ddwucom.mobile.travel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    Button btnLogout;
    ImageView btnHome, btnGroup, btnCourse, btnMap;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private String currentUid;
    boolean [] clicked = {true, false, false, false};
    int [] btn_names = {R.id.btn_home, R.id.btn_friends, R.id.btn_course, R.id.btn_map};

    ArrayList<String> folders;
    ArrayList<Record> recordList;
    RecordAdapter recordAdapter;
    RecyclerView rvHomeRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        database = FirebaseDatabase.getInstance();

        //btnLogout = findViewById(R.id.btnLogout);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUid = firebaseAuth.getCurrentUser().getUid();


        btnHome = findViewById(R.id.btn_home);
        btnGroup = findViewById(R.id.btn_friends);
        btnCourse = findViewById(R.id.btn_course);
        btnMap = findViewById(R.id.btn_map);

        rvHomeRecord = findViewById(R.id.rvHomeRecord);
        folders = new ArrayList<>();
        recordList = new ArrayList<>();
        recordAdapter = new RecordAdapter(this, recordList);
        rvHomeRecord.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        rvHomeRecord.setAdapter(recordAdapter);
        mgrRecords();
    }

    public void onClick(View v) { // 충돌 위험 있으니 push는 하지 마삼!!
//        Drawable tempImg, tempRes;
//        Bitmap tmpBitmap, tmpBitmapRes;
        switch (v.getId()) { // 본인 필요한 부분만 주석 풀어서 쓰세욥.
//
//            case R.id.btnLogout:
//
//                if(firebaseAuth.getCurrentUser() != null){
//                    //이미 로그인 되었다면 이 액티비티를 종료함
//                    firebaseAuth.signOut();
//                    finish();
//                    startActivity(new Intent(getApplicationContext(), LoginForm.class));
//                }
//                break;
            case R.id.btn_home:
                    btnHome.setImageResource(R.drawable.home_icon_yellow);
                    btnGroup.setImageResource(R.drawable.friends_icon_grey);
                    btnCourse.setImageResource(R.drawable.course_icon_grey);
                    btnMap.setImageResource(R.drawable.map_icon_grey);

//                tempImg = btnHome.getDrawable();
//                tempRes = HomeActivity.this.getResources().getDrawable(R.drawable.home_icon_yellow);
//                tmpBitmap = ((BitmapDrawable)tempImg).getBitmap();
//                tmpBitmapRes = ((BitmapDrawable)tempRes).getBitmap();
//
//                if(tmpBitmap.equals(tmpBitmapRes)) {
//                    btnHome.setImageResource(R.drawable.home_icon_grey);
//                    //로직 수행
//                }else{
//                    btnHome.setImageResource(R.drawable.home_icon_yellow);
//                    //로직 수행
//                }
                break;
            case R.id.btn_friends:
                btnHome.setImageResource(R.drawable.home_icon_grey);
                btnGroup.setImageResource(R.drawable.friends_icon_yellow);
                btnCourse.setImageResource(R.drawable.course_icon_grey);
                btnMap.setImageResource(R.drawable.map_icon_grey);
                break;
            case R.id.btn_course:
                btnHome.setImageResource(R.drawable.home_icon_grey);
                btnGroup.setImageResource(R.drawable.friends_icon_grey);
                btnCourse.setImageResource(R.drawable.course_icon_yellow);
                btnMap.setImageResource(R.drawable.map_icon_grey);
                break;
            case R.id.btn_map:
                btnHome.setImageResource(R.drawable.home_icon_grey);
                btnGroup.setImageResource(R.drawable.friends_icon_grey);
                btnCourse.setImageResource(R.drawable.course_icon_grey);
                btnMap.setImageResource(R.drawable.map_icon_yellow);
                break;
            case R.id.tvPlanAll:
                break;
            case R.id.tvRecordAll:
                Intent intent = new Intent(HomeActivity.this, RecordMain.class);
                startActivity(intent);
                break;
            case R.id.tvReviewAll:
                break;
        }
    }

    public void mgrRecords() {
        mgrRecordFolder();
        getRecords();

        recordAdapter.setOnItemClickListener(new RecordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                String key = recordAdapter.getItem(pos).getKey();
                Intent intent = new Intent(HomeActivity.this, RecordDayActivity.class);
                intent.putExtra("currentUid", currentUid);
                intent.putExtra("isNew", false);
                intent.putExtra("recordKey", key);
                intent.putStringArrayListExtra("folders", folders);
                startActivity(intent);
            }
        });
    }

    public void mgrRecordFolder() {
        DatabaseReference dbRefFolder = database.getReference("folders");

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
        DatabaseReference dbRef = database.getReference("records");
        dbRef.orderByChild("recordDate").limitToLast(5).addChildEventListener(new ChildEventListener() {
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
                    Collections.sort(recordList, new Record.SortByDate());

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
}
