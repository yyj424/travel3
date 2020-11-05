package ddwucom.mobile.travel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class GroupMainActivity extends AppCompatActivity {
    ImageView btnHome, btnGroup, btnCourse, btnMap;

    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    TextView tvGroupStartDate;
    TextView tvGroupEndDate;
    TextView y_group_main;

    ArrayList<Record> recordList;
    RecordAdapter recordAdapter;
    RecyclerView rvRecordList;
    RecyclerView rvAlbumList;
    ArrayList<Album> albumList;
    GroupAlbumAdapter albumAdapter;
    String currentUid;
    String currentNickname;
    Group selectedGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_main);

        database = FirebaseDatabase.getInstance();

        currentUid = (String) getIntent().getSerializableExtra("currentUid");
        currentNickname = (String) getIntent().getSerializableExtra("currentNickname");
        selectedGroup = (Group) getIntent().getSerializableExtra("selectedGroup");

        btnHome = findViewById(R.id.btn_home);
        btnGroup = findViewById(R.id.btn_friends);
        btnCourse = findViewById(R.id.btn_course);
        btnMap = findViewById(R.id.btn_map);

        tvGroupStartDate = findViewById(R.id.tvGroupStartDate);
        tvGroupStartDate.setText(selectedGroup.getStartDate());
        tvGroupEndDate = findViewById(R.id.tvGroupEndDate);
        tvGroupEndDate.setText(selectedGroup.getEndDate());
        y_group_main = findViewById(R.id.y_group_main);
        y_group_main.setText(selectedGroup.getGroupName());

        rvRecordList = findViewById(R.id.y_group_records);
        recordList = new ArrayList<>();
        recordAdapter = new RecordAdapter(this, true, recordList);
        rvRecordList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvRecordList.setAdapter(recordAdapter);
        detailRecord();

        rvAlbumList = findViewById(R.id.y_group_albums);
        albumList = new ArrayList<>();
        albumAdapter = new GroupAlbumAdapter(this, albumList);
        rvAlbumList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvAlbumList.setAdapter(albumAdapter);
        albumAdapter.setOnItemClickListener(new GroupAlbumAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(GroupMainActivity.this, AlbumActivity.class);
                intent.putExtra("currentGid", selectedGroup.getGid());
                intent.putExtra("albumName", albumList.get(position).getAlbumName());
                startActivity(intent);
            }
        });
    }

    public void detailRecord() {
        recordAdapter.setOnItemClickListener(new RecordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                String key = recordAdapter.getItem(pos).getKey();
                Intent intent = new Intent(GroupMainActivity.this, GRecordDayActivity.class);
                intent.putExtra("currentUid", currentUid);
                intent.putExtra("currentGid", selectedGroup.getGid());
                intent.putExtra("groupName", selectedGroup.getGroupName());
                intent.putExtra("currentNickname", currentNickname);
                intent.putExtra("isNew", false);
                intent.putExtra("recordKey", key);
                startActivity(intent);
            }
        });
    }

    public void onClick(View v) { // 충돌 위험 있으니 push는 하지 마삼!!
        Intent intent;
        switch (v.getId()) { // 본인 필요한 부분만 주석 풀어서 쓰세욥.
            case R.id.btn_home:
                Intent home = new Intent(GroupMainActivity.this, HomeActivity.class);
                home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home);
                finish();
                break;
            case R.id.btn_friends:
                Intent list = new Intent(GroupMainActivity.this, GroupListActivity.class);
                list.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(list);
                finish();
                break;
            case R.id.btn_map:
                Intent map = new Intent(GroupMainActivity.this, OnlyMap.class);
                map.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(map);
                finish();
                break;
            case R.id.y_group_readAllRecords:
                intent = new Intent(this, GroupRecordMain.class);
                intent.putExtra("currentGid", selectedGroup.getGid());
                intent.putExtra("groupName", selectedGroup.getGroupName());
                intent.putExtra("currentNickname", currentNickname);
                intent.putExtra("currentUid", currentUid);
                startActivity(intent);
                break;
            case R.id.y_group_readAllAlbums:
                intent = new Intent(this, GroupGalleryActivity.class);
                intent.putExtra("currentGid", selectedGroup.getGid());
                startActivity(intent);
                break;
            case R.id.y_group_plan_start:
                intent = new Intent(this, GroupPlan.class);
                intent.putExtra("groupKey", selectedGroup.getGid());
                startActivity(intent);
                break;
        }
    }

    public void getRecords() {
        recordList.clear();
        Log.d("group record", "레코드 가져오기");
        dbRef = database.getReference("group_records");
        dbRef.orderByChild("recordDate").limitToLast(5).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Map<String, Object> data = (Map) snapshot.getValue();
                Log.d("goeun", snapshot.getValue().toString());
                if (data.get("gid").equals(selectedGroup.getGid())) {
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
                    Log.d("group record", "add 전 " +recordList.size());
                    recordList.add(new Record(key, nickname, thumbnailImg, recordTitle, recordDate));
                    Log.d("group record", "add 후 " +recordList.size());
                    Collections.sort(recordList, new Record.SortByDate());
                    Log.d("group record", "sort 후 " +recordList.size());

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

    public void getAlbums()
    {
        albumList.clear();
        dbRef = database.getReference("group_album").child(selectedGroup.getGid());
        dbRef.limitToFirst(5).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ArrayList<String> imageList = (ArrayList<String>) snapshot.getValue();
                int size = imageList.size();
                String thumbnail = imageList.get(size - 1);
                String albumName = snapshot.getKey();
                if (!imageList.get(0).equals("이미지 없음")) {
                    albumList.add(new Album(thumbnail, albumName, size));
                }
                else {
                    albumList.add(new Album(null, albumName, 0));
                }
                albumAdapter.notifyDataSetChanged();
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("group record", "onResume");
        getRecords();
        getAlbums();
    }
}
