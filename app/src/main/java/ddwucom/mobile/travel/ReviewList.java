package ddwucom.mobile.travel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ReviewList extends AppCompatActivity {

    private ArrayList<MyReview> reviewList;
    private ReviewListAdapter adapter;
    private ListView listView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    String uid;
    String pid;
    double rating;
    String date;
    String content;
    long score1;
    long score2;
    long score3;
    long score4;
    double total = 0.0;
    TextView cnt;
    TextView rv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_list);

        Intent intent = getIntent();
        pid = intent.getStringExtra("placeId");
        reviewList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        cnt = findViewById(R.id.y_review_cnt);
        rv = findViewById(R.id.y_rvAvg);

        databaseReference = firebaseDatabase.getReference("review_content_list");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    if (s.child("pid").getValue().toString().equals(pid)) {
                        uid = s.child("userId").getValue().toString();
                        rating = Double.parseDouble(String.valueOf(s.child("rating").getValue()));
                        total += rating;
                        content = s.child("content").getValue().toString();
                        date = s.child("date").getValue().toString();
                        score1 = (long) s.child("score1").getValue();
                        score2 = (long) s.child("score2").getValue();
                        score3 = (long) s.child("score3").getValue();
                        score4 = (long) s.child("score4").getValue();
                        reviewList.add(new MyReview(uid, rating, date, content, score1, score2, score3, score4));
                    }
                }
                adapter = new ReviewListAdapter(ReviewList.this, R.layout.reviewlist_adapter_view, reviewList);
                listView = findViewById(R.id.y_review_list);
                listView.setAdapter(adapter);
                cnt.setText(String.valueOf(adapter.getCount()));
                rv.setText(String.format("%.1f", total / adapter.getCount()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new ReviewListAdapter(ReviewList.this, R.layout.reviewlist_adapter_view, reviewList);
        listView = findViewById(R.id.y_review_list);
        listView.setAdapter(adapter);

        cnt.setText("0");
        rv.setText("0");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.y_review_write:
                Intent intent = new Intent(ReviewList.this, ReviewForm.class);
                intent.putExtra("placeId", pid);
                startActivityForResult(intent, 100);
                break;
            case R.id.y_add_course:
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
                break;
            case R.id.y_filter:

                break;
        }
    }
}
