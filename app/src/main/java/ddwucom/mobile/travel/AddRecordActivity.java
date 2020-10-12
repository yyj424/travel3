package ddwucom.mobile.travel;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnMultiSelectedListener;

public class AddRecordActivity extends Activity {
    // image upload
    private StorageReference mStorageRef;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    final int maxImg = 4;
    int selectableImgCnt = maxImg;

    RecordContent recordContent;
    AddRecordImageAdapter addRecordImageAdapter;
    ArrayList<Uri> selectedImageList;
    ArrayList<String> images;
    RecyclerView recyclerView;
    //Map<String, String> images;

    EditText et_location;
    EditText et_content;

    String imageFolderName;
    String recordKey;
    String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_add);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        recordKey = (String) getIntent().getSerializableExtra("recordKey");
        currentUid = (String) getIntent().getSerializableExtra("currentUid");

        selectedImageList = new ArrayList<>();
        images = new ArrayList<>();
        //images = new HashMap<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.add_record_img_list);
        recyclerView.setLayoutManager(layoutManager);

        et_location = findViewById(R.id.et_location);
        et_content = findViewById(R.id.et_content);

        database = FirebaseDatabase.getInstance();
//        dbRef = database.getReference("records");
        dbRef = database.getReference("records").child(recordKey).child("contents").push();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_image:
                selectableImgCnt = maxImg - selectedImageList.size(); // 선택된 사진 개수 확인
                TedImagePicker.with(this)
                        .max(selectableImgCnt, "사진은 4장까지만 추가할 수 있습니다.")
                        .startMultiImage(new OnMultiSelectedListener() {
                            @Override
                            public void onSelected(@NotNull List<? extends Uri> uriList) {
                                selectedImageList.addAll(uriList);
                                addRecordImageAdapter = new AddRecordImageAdapter(AddRecordActivity.this, selectedImageList);
                                recyclerView.setAdapter(addRecordImageAdapter);
                            }
                        });
                break;
            case R.id.btn_add_record:
                Log.d("goeun", "btn_add_record");

                // DTO 객체 생성
                recordContent = new RecordContent();

                String location = et_location.getText().toString();
                String content = et_content.getText().toString();

                if (content.length() == 0) {
                    Toast.makeText(this, "내용을 입력하세요!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 객체에 이미지 외의 데이터 저장
                if (location.length() != 0) {
                    recordContent.setLocation(location);
                }
                recordContent.setContent(content);

                if (selectedImageList.size() > 0) {
                    firebaseStorage = FirebaseStorage.getInstance();  Log.d("goeun", firebaseStorage.getReference().getName());
                    mStorageRef = firebaseStorage.getReference(); Log.d("goeun", mStorageRef.getName());

                    // 폴더명 지정
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    Date now = new Date();
                    imageFolderName = formatter.format(now) + "/";

//                    for (int i = 0; i < selectedImageList.size(); i++) { // 선택한 사진 개수만큼 반복
                    for (final Uri imageUri : selectedImageList) {
                        // storage에 사진 업로드

                        mStorageRef = firebaseStorage.getReferenceFromUrl("gs://travel3-262be.appspot.com").child("record_images/" + currentUid + "/" + imageFolderName + imageUri.getLastPathSegment());
                        mStorageRef.putFile(imageUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String tmp = uri.toString();
                                                images.add(tmp);
                                                Log.d("goeun", String.valueOf(images.size()));
                                                if (images.size() == selectedImageList.size()) {
                                                    dbRef.setValue(recordContent);
                                                    dbRef.child("images").setValue(images);
                                                    finish();
                                                }
                                            }
                                        });

                                        Log.d("goeun", String.valueOf(images.size()));
                                        Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();

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
                break;
        }
    }
}
