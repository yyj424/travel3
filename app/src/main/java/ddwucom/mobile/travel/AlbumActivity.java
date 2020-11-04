package ddwucom.mobile.travel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnMultiSelectedListener;

public class AlbumActivity extends AppCompatActivity {
    private static final String TAG = "AlbumActivity";

    private StorageReference mStorageRef;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    Toolbar toolbar;
    RecyclerView rvAlbum;
    AlbumAdapter albumAdapter;
    ArrayList<String> imageList;
    ArrayList<String> uploadImages;
    ArrayList<Uri> selectedImageList;
    String folderName;
    String albumName;
    String currentGid;
    ImageView btnHome, btnGroup, btnCourse, btnMap;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upload_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        selectedImageList.clear();
        if (id == R.id.btnUploadImg) {
            TedImagePicker.with(this)
                    .max(30, "사진은 한 번에 30장까지만 업로드 할 수 있습니다.")
                    .startMultiImage(new OnMultiSelectedListener() {
                        @Override
                        public void onSelected(@NotNull List<? extends Uri> uriList) {
                            selectedImageList.addAll(uriList);
                            uploadImage();
                        }
                    });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void uploadImage() {
        if (selectedImageList.size() > 0) {
            firebaseStorage = FirebaseStorage.getInstance();

            // 폴더명 지정
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Date now = new Date();
            folderName = formatter.format(now) + "/";

            uploadImages.clear();
            for (final Uri imageUri : selectedImageList) {
                Log.d(TAG, "uri " + imageUri);
                mStorageRef = firebaseStorage.getReference().child("group_album/" + currentGid + "/" + folderName + imageUri.getLastPathSegment());
                mStorageRef.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String tmp = uri.toString();
                                        uploadImages.add(tmp);
                                        if (uploadImages.size() == selectedImageList.size()) {
                                            imageList.addAll(uploadImages);
                                            albumAdapter.notifyDataSetChanged();
                                            rvAlbum.smoothScrollToPosition(imageList.size() - 1);
                                            dbRef.child(currentGid).child(albumName).setValue(imageList, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                    Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    public void getImages() {
        dbRef.child(currentGid).child(albumName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> tmp = (ArrayList<String>) snapshot.getValue();
                if (!tmp.get(0).equals("이미지 없음")) {
                    imageList.addAll(tmp);
                    albumAdapter.notifyDataSetChanged();
                    rvAlbum.smoothScrollToPosition(imageList.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onClick(View v) { // 충돌 위험 있으니 push는 하지 마삼!!
        switch (v.getId()) { // 본인 필요한 부분만 주석 풀어서 쓰세욥.
            case R.id.btn_home:
                Intent home = new Intent(AlbumActivity.this, HomeActivity.class);
                startActivity(home);
                break;
            case R.id.btn_friends:
                btnHome.setImageResource(R.drawable.home_icon_grey);
                btnGroup.setImageResource(R.drawable.friends_icon_yellow);
                btnCourse.setImageResource(R.drawable.course_icon_grey);
                btnMap.setImageResource(R.drawable.map_icon_grey);
                Intent list = new Intent(AlbumActivity.this, GroupListActivity.class);
                startActivity(list);
                break;
            case R.id.btn_map:
                Intent map = new Intent(AlbumActivity.this, OnlyMap.class);
                startActivity(map);
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        btnHome = findViewById(R.id.btn_home);
        btnGroup = findViewById(R.id.btn_friends);
        btnCourse = findViewById(R.id.btn_course);
        btnMap = findViewById(R.id.btn_map);

        toolbar = findViewById(R.id.tbAlbum);
        setSupportActionBar(toolbar);

        selectedImageList = new ArrayList<>();
        uploadImages = new ArrayList<>();

        currentGid = (String) getIntent().getSerializableExtra("currentGid");
        albumName = (String) getIntent().getSerializableExtra("albumName");
        setTitle(albumName);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("group_album");

        imageList = new ArrayList<>();
        getImages();

        rvAlbum = findViewById(R.id.rvAlbum);
        rvAlbum.setLayoutManager(new GridLayoutManager(this, 3));
        rvAlbum.setHasFixedSize(true);

        albumAdapter = new AlbumAdapter(this, imageList);
        rvAlbum.setAdapter(albumAdapter);

        rvAlbum.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d(TAG, "onClick" + position);
                        Intent intent = new Intent(AlbumActivity.this, AlbumDetailActivity.class);
                        intent.putStringArrayListExtra("imageList", imageList);
                        intent.putExtra("pos", position);
                        startActivity(intent);
                    }
                }));
    }
}
