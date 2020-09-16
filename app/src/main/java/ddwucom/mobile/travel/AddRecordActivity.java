package ddwucom.mobile.travel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.net.Uri;


import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnMultiSelectedListener;
import gun0912.tedimagepicker.builder.listener.OnSelectedListener;

public class AddRecordActivity extends Activity {
    private final int GET_GALLERY_IMAGE = 100;


    // image upload
    private StorageReference mStorageRef;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    private Intent intent;
    private Uri selectedImageUri;
    private RecordContent recordContent;
    private ImageView img_record_1;
    private ImageView img_record_2;
    private ImageView img_record_3;
    private ImageView img_record_4;
    private ArrayList<ImageView> imageViews;
    private EditText et_location;
    private EditText et_content;
    private String filename;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_add);

        imageViews = new ArrayList<>();
        imageViews.add((ImageView) findViewById(R.id.img_record_1));
        imageViews.add((ImageView) findViewById(R.id.img_record_2));
        imageViews.add((ImageView) findViewById(R.id.img_record_3));
        imageViews.add((ImageView) findViewById(R.id.img_record_4));
//        img_record_1 = findViewById(R.id.img_record_1);
//        img_record_2 = findViewById(R.id.img_record_2);
//        img_record_3 = findViewById(R.id.img_record_3);
//        img_record_4 = findViewById(R.id.img_record_4);

        et_location = findViewById(R.id.et_location);
        et_content = findViewById(R.id.et_content);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReferenceFromUrl("https://travel3-262be.firebaseio.com/");

        for (ImageView iv:imageViews) {
            iv.setVisibility(View.GONE);
        }
//        img_record_1.setVisibility(View.GONE);
//        img_record_2.setVisibility(View.GONE);
//        img_record_3.setVisibility(View.GONE);
//        img_record_4.setVisibility(View.GONE);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_image:
                TedImagePicker.with(this)
                        .max(4, "사진은 4장까지만 추가 가능합니다.")
                        .startMultiImage(new OnMultiSelectedListener() {
                            @Override
                            public void onSelected(@NotNull List<? extends Uri> uriList) {
                                int visibleCnt = 0;
                                if (visibleCnt != 4) {
                                    for (Uri uri:uriList) {
                                        for (ImageView iv:imageViews) {
                                            if (iv.getVisibility() != View.GONE) {
                                                continue;
                                            }
                                            iv.setVisibility(View.VISIBLE);
                                            iv.setImageURI(uri);
                                            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                            break;
                                        }
                                    }
                                }
                                //iv.setImageURI(selectedImageUri);
                            }
                        });
                break;
//            case R.id.btn_add_record:
//                Log.d("goeun", "btn_add_record");
//                final ProgressDialog progressDialog = new ProgressDialog(this);
//                progressDialog.setTitle("업로드중...");
//                progressDialog.show();
//
//                firebaseStorage = FirebaseStorage.getInstance();  Log.d("goeun", firebaseStorage.getReference().getName());
////                mStorageRef = firebaseStorage.getReference(); Log.d("goeun", mStorageRef.getName());
//
//                // 파일명
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
//                Date now = new Date();
//                filename = formatter.format(now) + ".png";
//
//                mStorageRef = firebaseStorage.getReferenceFromUrl("gs://travel3-262be.appspot.com").child("record_images/" + filename);
//                mStorageRef.putFile(selectedImageUri)
//                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
//                                Task<Uri> downloadUri = mStorageRef.getDownloadUrl();
//                                downloadUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) { //4.Url 받는곳
//                                        imageUrl = uri.toString();
//                                        Log.d("이미지",imageUrl);
//                                        recordContent = new RecordContent();
//                                        recordContent.setImageResIds(imageUrl); Log.d("goeun", imageUrl);
//                                        String location = et_location.getText().toString();
//                                        String content = et_content.getText().toString();
//                                        recordContent.setLocation(location);
//                                        recordContent.setContent(content);
//
//                                        dbRef.child("record_content_list").push().setValue(recordContent);
////                                        dbRef.child("record_content_list").push().
//                                    }
//                                });
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                                @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
//                                        double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
//                                //dialog에 진행률을 퍼센트로 출력해 준다
//                                progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
//                                if (progress == 100) {
//                                    progressDialog.cancel();
//                                }
//                            }
//                        });
//                break;
        }

    }
}
