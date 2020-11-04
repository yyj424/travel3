package ddwucom.mobile.travel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnMultiSelectedListener;

public class ReviewForm extends AppCompatActivity {
    EditText reviewContent;
    RecyclerView listview;
    SeekBar seekbar1;
    SeekBar seekbar2;
    SeekBar seekbar3;
    SeekBar seekbar4;
    TextView sbView1;
    TextView sbView2;
    TextView sbView3;
    TextView sbView4;
    ReviewFormImageAdapter reviewFormImageAdapter;
    ArrayList<Uri> ImageList = null;
    ArrayList<String> reviewImages = null;

    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference userDBRef;
    private DatabaseReference imageDBRef;

    String uid;
    String pid;
    double finalRating;
    String content;
    long score1;
    long score2;
    long score3;
    long score4;
    String folderName;
    String pname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_write_form);

        final TextView ratingView = findViewById(R.id.y_rating);
        reviewContent = findViewById(R.id.y_etReviewContent);
        listview = findViewById(R.id.y_RimageList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listview.setLayoutManager(layoutManager);
        ImageList = new ArrayList();
        reviewImages = new ArrayList();
        seekbar1 = findViewById(R.id.seekBar1);
        seekbar2 = findViewById(R.id.seekBar2);
        seekbar3 = findViewById(R.id.seekBar3);
        seekbar4 = findViewById(R.id.seekBar4);
        sbView1 = findViewById(R.id.y_sbView1);
        sbView2 = findViewById(R.id.y_sbView2);
        sbView3 = findViewById(R.id.y_sbView3);
        sbView4 = findViewById(R.id.y_sbView4);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        Intent intent = getIntent();
        pid = intent.getStringExtra("placeId");
        pname = intent.getStringExtra("placeName");

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingView.setText(String.valueOf(rating));
                finalRating = rating;
            }
        });

        seekBarListener(seekbar1, sbView1);
        seekBarListener(seekbar2, sbView2);
        seekBarListener(seekbar3, sbView3);
        seekBarListener(seekbar4, sbView4);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void seekBarListener(SeekBar sb, final TextView tv) {
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //tv.setText(String.valueOf(seekBar.getProgress()));
            }
        });
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.y_addPhoto:
                TedImagePicker.with(this)
                        .startMultiImage(new OnMultiSelectedListener() {
                            @Override
                            public void onSelected(List<? extends Uri> list) {
                                ImageList.addAll(list);
                                reviewFormImageAdapter = new ReviewFormImageAdapter(ReviewForm.this, ImageList);
                                listview.setAdapter(reviewFormImageAdapter);
                            }
                        });
                break;
            case R.id.y_reviewRegister:
                if (ImageList.size() == 0) {
                    Toast.makeText(this, "이미지를 업로드 해야합니다.", Toast.LENGTH_LONG).show();
                    return;
                }
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("리뷰 업로드 중");
                progressDialog.show();

                // 객체에 데이터 저장
                Intent intent = getIntent();
                uid = intent.getStringExtra("uid");
                content = reviewContent.getText().toString();
                score1 = Long.parseLong(sbView1.getText().toString());
                score2 = Long.parseLong(sbView2.getText().toString());
                score3 = Long.parseLong(sbView3.getText().toString());
                score4 = Long.parseLong(sbView4.getText().toString());

                userDBRef = firebaseDatabase.getReference("user_list");
                userDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot s : snapshot.getChildren()) {
                            if (s.child("uid").getValue().toString().equals(uid)) {
                                MyReview myReview = new MyReview();
                                myReview.setUserId(s.child("nickname").getValue().toString());
                                myReview.setPid(pid);
                                long now = System.currentTimeMillis();
                                Date date = new Date(now);
                                SimpleDateFormat sdfNow = new SimpleDateFormat("yy/MM/dd");
                                myReview.setDate(sdfNow.format(date));
                                myReview.setRating(finalRating);
                                myReview.setContent(content);
                                myReview.setScore1(score1);
                                myReview.setScore2(score2);
                                myReview.setScore3(score3);
                                myReview.setScore4(score4);
                                myReview.setPname(pname);

                                reviewRegister(myReview);
                                finish();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
        }
    }

    public void reviewRegister(final MyReview myReview) {
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        // 폴더명 지정
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date now = new Date();
        folderName = formatter.format(now) + "/";

//                    for (int i = 0; i < selectedImageList.size(); i++) { // 선택한 사진 개수만큼 반복
        for (final Uri imageUri : ImageList) {
            // storage에 사진 업로드

            storageReference = firebaseStorage.getReference().child("review_images/" + uid + "/" + folderName + imageUri.getLastPathSegment());
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String tmp = uri.toString();
                                    reviewImages.add(tmp);
                                    if (reviewImages.size() == ImageList.size()) {
                                        myReview.setReviewImages(reviewImages);
                                        databaseReference.child("review_content_list").push().setValue(myReview);
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
}
