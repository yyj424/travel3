package ddwucom.mobile.travel;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
                            Log.d(TAG, albumName);
                            if (!albumName.matches("") && isExistAlbumName(albumName)) {
                                Log.d(TAG, "앨범 추가");
                                dbRef.child(albumName).setValue(new ArrayList<String>().add("이미지 없음"));
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_gallery);

        addAlbumLayout = (LinearLayout) View.inflate(this, R.layout.dialog_edittext, null);

        // 현재 그룹 아이디 가져와야함!!
        currentGid = "-ML7VAsNH5KHPqnBrBry";

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("group_album").child(currentGid);

        albumList = new ArrayList<>();
        getAlbumList();

        lvAlbumList = findViewById(R.id.lvAlbumList);
        toolbar = findViewById(R.id.tbGallery);
        setSupportActionBar(toolbar);

        galleryAdapter = new GalleryListAdapter(this, albumList);
        lvAlbumList.setAdapter(galleryAdapter);
    }

    public void getAlbumList() {
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
}
