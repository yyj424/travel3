package ddwucom.mobile.travel;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GroupGalleryActivity extends AppCompatActivity {
    private static final String TAG = "GroupGalleryActivity";

    EditText etAlbumName;
    ListView lvAlbumList;
    Toolbar toolbar;

    GalleryListAdapter galleryAdapter;
    LinearLayout addAlbumLayout;
    FirebaseDatabase database;
    DatabaseReference dbRef;

    ArrayList<Album> albumList;
    String currentGid;
    ImageView btnHome, btnGroup, btnCourse, btnMap;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_album_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btnAddAlbum) {
            TextView dialogTitle = new TextView(this);
            dialogTitle.setText("앨범 추가하기");
            dialogTitle.setIncludeFontPadding(false);
            dialogTitle.setTypeface(ResourcesCompat.getFont(this, R.font.tmoney_regular));
            dialogTitle.setGravity(Gravity.CENTER);
            dialogTitle.setPadding(10, 70, 10, 70);
            dialogTitle.setTextSize(20F);
            dialogTitle.setBackgroundResource(R.color.colorTop);
            dialogTitle.setTextColor(Color.DKGRAY);

            AlertDialog alertDialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
            builder.setCustomTitle(dialogTitle)
                    .setView(addAlbumLayout)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etAlbumName = addAlbumLayout.findViewById(R.id.etDialog);
                            String albumName = etAlbumName.getText().toString();

                            if (!albumName.equals("") && !isExistAlbumName(albumName)) {
                                dbRef.child(albumName).child("0").setValue("이미지 없음");
                                Intent intent = new Intent(GroupGalleryActivity.this, AlbumActivity.class);
                                intent.putExtra("currentGid", currentGid);
                                intent.putExtra("albumName", albumName);
                                startActivity(intent);
                            } else {
                                Toast.makeText(GroupGalleryActivity.this, "이미 존재하는 앨범명입니다!", Toast.LENGTH_LONG);
                                return;
                            }
                        }
                    })
                    .setNegativeButton("CANCEL", null);
            alertDialog = builder.create();
            if (addAlbumLayout.getParent() != null) {
                ((ViewGroup) addAlbumLayout.getParent()).removeView(addAlbumLayout);
            }
            alertDialog.show();
            alertDialog.getWindow().setLayout(1200, 800);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isExistAlbumName(String albumName) {
        for (Album a:albumList) {
            if (a.getAlbumName().equals(albumName)) {
                return true;
            }
        }
        return false;
    }

    public void onClick(View v) { // 충돌 위험 있으니 push는 하지 마삼!!
        switch (v.getId()) { // 본인 필요한 부분만 주석 풀어서 쓰세욥.
            case R.id.btn_home:
                Intent home = new Intent(GroupGalleryActivity.this, HomeActivity.class);
                startActivity(home);
                break;
            case R.id.btn_friends:
                btnHome.setImageResource(R.drawable.home_icon_grey);
                btnGroup.setImageResource(R.drawable.friends_icon_yellow);
                btnCourse.setImageResource(R.drawable.course_icon_grey);
                btnMap.setImageResource(R.drawable.map_icon_grey);
                Intent list = new Intent(GroupGalleryActivity.this, GroupListActivity.class);
                startActivity(list);
                break;
            case R.id.btn_map:
                Intent map = new Intent(GroupGalleryActivity.this, OnlyMap.class);
                startActivity(map);
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_gallery);

        btnHome = findViewById(R.id.btn_home);
        btnGroup = findViewById(R.id.btn_friends);
        btnCourse = findViewById(R.id.btn_course);
        btnMap = findViewById(R.id.btn_map);

        addAlbumLayout = (LinearLayout) View.inflate(this, R.layout.dialog_edittext, null);

        currentGid = (String) getIntent().getSerializableExtra("currentGid");

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("group_album").child(currentGid);

        albumList = new ArrayList<>();

        lvAlbumList = findViewById(R.id.lvAlbumList);
        toolbar = findViewById(R.id.tbGallery);
        setSupportActionBar(toolbar);
        setTitle(null);

        galleryAdapter = new GalleryListAdapter(this, albumList);
        lvAlbumList.setAdapter(galleryAdapter);

        lvAlbumList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GroupGalleryActivity.this, AlbumActivity.class);
                intent.putExtra("currentGid", currentGid);
                intent.putExtra("albumName", albumList.get(position).getAlbumName());
                startActivity(intent);
            }
        });
    }

    public void getAlbumList() {
        albumList.clear();
        dbRef.addChildEventListener(new ChildEventListener() {
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
                galleryAdapter.notifyDataSetChanged();
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
        getAlbumList();
    }
}
