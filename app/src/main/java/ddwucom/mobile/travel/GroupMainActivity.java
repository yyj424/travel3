package ddwucom.mobile.travel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GroupMainActivity extends AppCompatActivity {
    ImageView btnHome, btnGroup, btnCourse, btnMap;
    TextView gname;
    TextView gstart;
    TextView gend;

    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private FirebaseAuth firebaseAuth;
    String currentUid;

    ArrayList<Record> recordList;
    RecordAdapter recordAdapter;
    RecyclerView recordistview;

    RecyclerView albumlistview;
    ArrayList<Album> albumList;
    GroupAlbumAdapter albumAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_main);

        database = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        currentUid = firebaseAuth.getCurrentUser().getUid();

        btnHome = findViewById(R.id.btn_home);
        btnGroup = findViewById(R.id.btn_friends);
        btnCourse = findViewById(R.id.btn_course);
        btnMap = findViewById(R.id.btn_map);

        recordistview = findViewById(R.id.y_group_records);
        recordList = new ArrayList<>();
        recordAdapter = new RecordAdapter(this, false, recordList);
        recordistview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recordistview.setAdapter(recordAdapter);
        getGroupRecords();

        albumlistview = findViewById(R.id.y_group_albums);
        albumList = new ArrayList<>();
        albumAdapter = new GroupAlbumAdapter(this, albumList);
        albumlistview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        albumlistview.setAdapter(albumAdapter);
        getGroupAlbums();
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
            case R.id.y_group_readAllRecords:
                Intent plan = new Intent(GroupMainActivity.this, GroupRecordMain.class);
                startActivity(plan);
                break;
            case R.id.y_group_readAllAlbums:
                Intent intent = new Intent(GroupMainActivity.this, GroupGalleryActivity.class);
                startActivity(intent);
                break;
//            case R.id.btn_menu:
//                drawerLayout.openDrawer(drawerView);
//                break;
        }
    }

    public void getGroupRecords()
    {

    }

    public void getGroupAlbums()
    {

    }
}
