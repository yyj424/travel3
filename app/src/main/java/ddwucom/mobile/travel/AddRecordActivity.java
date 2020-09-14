package ddwucom.mobile.travel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddRecordActivity extends Activity {
    private final int GET_GALLERY_IMAGE = 100;

    // image upload
    StorageReference mStorageRef;
    FirebaseStorage firebaseStorage;
    FirebaseDatabase database;
    DatabaseReference dbRef;

    Intent intent;
    Uri selectedImageUri;
    RecordContent recordContent;
    ImageView iv;
    EditText et_location;
    EditText et_content;
    String filename;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_add);

        iv = findViewById(R.id.img_record);
        et_location = findViewById(R.id.et_location);
        et_content = findViewById(R.id.et_content);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReferenceFromUrl("https://travel3-262be.firebaseio.com/");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            iv.setImageURI(selectedImageUri);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_image:
                intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
                break;
            case R.id.btn_add_record:
                Log.d("goeun", "btn_add_record");
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("업로드중...");
                progressDialog.show();

                firebaseStorage = FirebaseStorage.getInstance();  Log.d("goeun", firebaseStorage.getReference().getName());
//                mStorageRef = firebaseStorage.getReference(); Log.d("goeun", mStorageRef.getName());

                // 파일명
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
                Date now = new Date();
                filename = formatter.format(now) + ".png";

                mStorageRef = firebaseStorage.getReferenceFromUrl("gs://travel3-262be.appspot.com").child("record_images/" + filename);
                mStorageRef.putFile(selectedImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                                Task<Uri> downloadUri = mStorageRef.getDownloadUrl();
                                downloadUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) { //4.Url 받는곳
                                        imageUrl = uri.toString();
                                        Log.d("이미지",imageUrl);
                                        recordContent = new RecordContent();
                                        recordContent.setImageResIds(imageUrl); Log.d("goeun", imageUrl);
                                        String location = et_location.getText().toString();
                                        String content = et_content.getText().toString();
                                        recordContent.setLocation(location);
                                        recordContent.setContent(content);

                                        dbRef.child("record_content_list").push().setValue(recordContent);
                                    }
                                });
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
                                @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                        double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                                //dialog에 진행률을 퍼센트로 출력해 준다
                                progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                                if (progress == 100) {
                                    progressDialog.cancel();
                                }
                            }
                        });
                break;
        }

    }
}
