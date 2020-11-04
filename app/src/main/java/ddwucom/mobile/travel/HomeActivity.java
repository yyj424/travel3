package ddwucom.mobile.travel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ImageView btnHome, btnGroup, btnCourse, btnMap;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private FirebaseAuth firebaseAuth;

    private String currentUid;
    static String nv_phone, nv_email, nv_nickname;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    TextView tx_name, tx_email, tx_phone;

    ArrayList<String> folders;
    ArrayList<Record> recordList;
    RecordAdapter recordAdapter;
    RecyclerView rvHomeRecord;

    RecyclerView planListview;
    ArrayList<MyPlan> planList;
    HomePlanAdapter planAdapter;
    String pname;

    RecyclerView reviewListview;
    ArrayList<MyReview> reviewList;
    HomeReviewAdapter reviewAdapter;
    String nick;
    String rvimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);

        database = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        currentUid = firebaseAuth.getCurrentUser().getUid();

        btnHome = findViewById(R.id.btn_home);
        btnGroup = findViewById(R.id.btn_friends);
        btnCourse = findViewById(R.id.btn_course);
        btnMap = findViewById(R.id.btn_map);

        //햄버거 버튼 누르면 상세정보 사이드바
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        drawerLayout = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                dbRef = database.getReference("user_list");
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("sera", "CLick22!!");
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Log.d("sera", "ValueEventListener : " + snapshot.child("nickname").getValue());
                            if (currentUid.equals(snapshot.child("uid").getValue().toString())) {
                                nv_phone = snapshot.child("phone").getValue().toString();
                                nv_email = snapshot.child("email").getValue().toString();
                                nv_nickname = snapshot.child("nickname").getValue().toString();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                tx_name = findViewById(R.id.tx_name);
                tx_email = findViewById(R.id.tx_email);
                tx_phone = findViewById(R.id.tx_phone);

                tx_name.setText(nv_nickname);
                tx_email.setText(nv_email);
                tx_phone.setText(nv_phone);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
               super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        };

        Log.d("sera", "toggle : "+toggle);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_btn);

        navigationView = findViewById(R.id.nv_view);
        navigationView.setNavigationItemSelectedListener(this);

        rvHomeRecord = findViewById(R.id.rvHomeRecord);
        folders = new ArrayList<>();
        recordList = new ArrayList<>();
        recordAdapter = new RecordAdapter(this, false, recordList);
        rvHomeRecord.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvHomeRecord.setAdapter(recordAdapter);
        detailRecord();

        planListview = findViewById(R.id.y_home_plans);
        planList = new ArrayList<>();
        planAdapter = new HomePlanAdapter(this, planList);
        planListview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        planListview.setAdapter(planAdapter);

        reviewListview = findViewById(R.id.y_home_reviews);
        reviewList = new ArrayList<>();
        reviewAdapter = new HomeReviewAdapter(this, reviewList);
        reviewListview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        reviewListview.setAdapter(reviewAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
         //Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id){
            case R.id.nav_signout:
                if(firebaseAuth.getCurrentUser() != null){
                    //이미 로그인 되었다면 이 액티비티를 종료함
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginForm.class));
                }
                else{
                    Log.d("sera", "firebaseAuth.getCurrentUser() : null   [nav_signout]");
                }
                break;
            case R.id.nav_revoke:
                if(firebaseAuth.getCurrentUser() != null){ //회원탈퇴
                    //이미 로그인 되었다면 이 액티비티를 종료함
                    dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d("sera", snapshot.toString());
                            Log.d("sera", snapshot.getValue().toString());
                            Log.d("sera", snapshot.getChildren().toString());
                            Log.d("sera", snapshot.child("uid").getValue().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    firebaseAuth.getCurrentUser().delete();
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginForm.class));
                }
                else{
                    Log.d("sera", "firebaseAuth.getCurrentUser() : null  [nav_revoke]");
                }
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClick(View v) { // 충돌 위험 있으니 push는 하지 마삼!!
        switch (v.getId()) { // 본인 필요한 부분만 주석 풀어서 쓰세욥.
            case R.id.btn_home:
                    btnHome.setImageResource(R.drawable.home_icon_yellow);
                    btnGroup.setImageResource(R.drawable.friends_icon_grey);
                    btnCourse.setImageResource(R.drawable.course_icon_grey);
                    btnMap.setImageResource(R.drawable.map_icon_grey);
                break;
            case R.id.btn_friends:
                Intent group = new Intent(HomeActivity.this, GroupListActivity.class);
                startActivity(group);
                break;
            case R.id.btn_map:
                Intent map = new Intent(HomeActivity.this, OnlyMap.class);
                startActivity(map);
                break;
            case R.id.tvPlanAll:
                Intent plan = new Intent(HomeActivity.this, PlanList.class);
                startActivity(plan);
                break;
            case R.id.tvRecordAll:
                Intent intent = new Intent(HomeActivity.this, RecordMain.class);
                startActivity(intent);
                break;
            case R.id.tvReviewAll:
                Intent review = new Intent(HomeActivity.this, ReviewList.class);
                review.putExtra("nickName", nick);
                startActivity(review);
                break;
        }
    }

    public void detailRecord() {
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

    public void mgrRecords() {
        mgrRecordFolder();
        getRecords();
    }

    public void mgrRecordFolder() {
        folders.clear();
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

    public void getPlans() {
        planList.clear();
        DatabaseReference plandb = database.getReference("plan_list");
        plandb.limitToLast(5).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    if (s.child("uid").getValue().toString().equals(currentUid)) {
                        pname = s.child("planName").getValue().toString();
                        planList.add(new MyPlan(pname, s.child("startDate").getValue().toString(), s.child("endDate").getValue().toString()));
                    }
                }
                planAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        planAdapter.setOnItemClickListener(new HomePlanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent getPlan = new Intent(HomeActivity.this, PlanLastStep.class);
                getPlan.putExtra("pname", planList.get(position).getPlanName());
                startActivity(getPlan);
            }
        });
    }

    public void getReview()
    {
        DatabaseReference userdb = database.getReference("user_list");
        userdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    if (s.child("uid").getValue().toString().equals(currentUid)) {
                        nick = s.child("nickname").getValue().toString();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reviewList.clear();
        DatabaseReference reviewdb = database.getReference("review_content_list");
        reviewdb.limitToLast(5).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    if (s.child("userId").getValue().toString().equals(nick)) {
                        for (DataSnapshot rvis : s.child("reviewImages").getChildren()) {
                            rvimg = rvis.getValue().toString();
                            break;
                        }
                        reviewList.add(new MyReview(s.child("pname").getValue().toString(), rvimg, Double.parseDouble(String.valueOf(s.child("rating").getValue()))));
                    }
                }
                reviewAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mgrRecords();
        getPlans();
        getReview();
    }
}
