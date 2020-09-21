package ddwucom.mobile.travel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

    Intent intent;
    RecordContent recordContent;
    AddRecordImageAdapter addRecordImageAdapter;
    ArrayList<Uri> selectedImageList;
    ArrayList<String> dlUriList;
    RecyclerView recyclerView;

    EditText et_location;
    EditText et_content;

    String folderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_add);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        selectedImageList = new ArrayList<>();
        dlUriList = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.add_record_img_list);
        recyclerView.setLayoutManager(layoutManager);

        et_location = findViewById(R.id.et_location);
        et_content = findViewById(R.id.et_content);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReferenceFromUrl("https://travel3-262be.firebaseio.com/");
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
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("업로드중...");
                progressDialog.show();

                firebaseStorage = FirebaseStorage.getInstance();  Log.d("goeun", firebaseStorage.getReference().getName());
//                mStorageRef = firebaseStorage.getReference(); Log.d("goeun", mStorageRef.getName());

                // 폴더명 지정
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
                Date now = new Date();
                folderName = formatter.format(now) + "/";

                for (int i = 0; i < selectedImageList.size(); i++) { // 선택한 사진 개수만큼 반복
                    // storage에 사진 업로드
                    mStorageRef = firebaseStorage.getReferenceFromUrl("gs://travel3-262be.appspot.com").child("record_images/" + folderName + selectedImageList.get(i).getLastPathSegment());
                    mStorageRef.putFile(selectedImageList.get(i))
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    @SuppressWarnings("VisibleForTests")
                                            double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                                    //dialog에 진행률을 퍼센트로 출력해 준다
                                    progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                                    if (progress == 100) {
                                        progressDialog.cancel();
                                    }
                                }
                            });
                }

                // DTO 객체 생성
                recordContent = new RecordContent();

                // 객체에 데이터 저장
                recordContent.setImageFolderName(folderName);
                String location = et_location.getText().toString();
                String content = et_content.getText().toString();
                recordContent.setLocation(location);
                recordContent.setContent(content);

                // db에 recordContent 저장
                dbRef.child("record_content_list").push().setValue(recordContent);
                break;
        }
    }
}
