package ddwucom.mobile.travel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ReviewList extends AppCompatActivity {

    private ArrayList<MyReview> reviewList;
    private ReviewListAdapter adapter;
    private RecyclerView listView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    long days;

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
    int selected = 0;
    String pname;
    String currentUid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_list);

        Intent intent = getIntent();
        pid = intent.getStringExtra("placeId");
        pname = intent.getStringExtra("placeName");
        currentUid = intent.getStringExtra("currentUid");

        reviewList = new ArrayList<>();
        listView = findViewById(R.id.y_review_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(layoutManager);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("review_content_list");
        cnt = findViewById(R.id.y_review_cnt);
        rv = findViewById(R.id.y_rvAvg);

        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.y_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reviewList.clear();
                readRVDB();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        readRVDB();
    }

    public void readRVDB() {
        adapter = new ReviewListAdapter(ReviewList.this, reviewList);
        listView.setAdapter(adapter);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    if (s.child("pid").getValue().toString().equals(pid)) {
                        uid = s.child("userId").getValue().toString();
                        List<String> rImages = new ArrayList<>();
                        for (DataSnapshot rvis : s.child("reviewImages").getChildren()) {
                            rImages.add(rvis.getValue().toString());
                        }
                        rating = Double.parseDouble(String.valueOf(s.child("rating").getValue()));
                        total += rating;
                        content = s.child("content").getValue().toString();
                        date = s.child("date").getValue().toString();
                        score1 = (long) s.child("score1").getValue();
                        score2 = (long) s.child("score2").getValue();
                        score3 = (long) s.child("score3").getValue();
                        score4 = (long) s.child("score4").getValue();
                        reviewList.add(new MyReview(uid, rImages, rating, date, content, score1, score2, score3, score4));
                        Log.d("yyj","추가됨 : " + reviewList.size());
                    }
                }
                Collections.sort(reviewList, compareNew());
                adapter.notifyDataSetChanged();
                cnt.setText(String.valueOf(adapter.getItemCount()));
                rv.setText(String.format("%.1f", total / Double.parseDouble(cnt.getText().toString())));
                total = 0.0;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public Comparator<MyReview> compareNew() {
        Comparator<MyReview> Desc = new Comparator<MyReview>() {
            @Override
            public int compare(MyReview o1, MyReview o2) {
                SimpleDateFormat transFormat = new SimpleDateFormat("yy/MM/dd");
                try {
                    int ret = 0;
                    Date d1 = transFormat.parse(o1.getDate());
                    Date d2 = transFormat.parse(o2.getDate());
                    if(d1.getTime() < d2.getTime()) {
                        ret = 1;
                    }
                    else if (d1.getTime() == d2.getTime())
                        ret = 0;
                    else
                        ret = -1;
                    return ret ;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        } ;
        return Desc;
    }

    public Comparator<MyReview> compareHighScore() {
        Comparator<MyReview> Desc = new Comparator<MyReview>() {
            @Override
            public int compare(MyReview o1, MyReview o2) {
                int ret = 0;
                if (o1.getRating() > o2.getRating())
                    ret = -1;
                else if (o1.getRating() == o2.getRating())
                    ret = 0;
                else
                    ret = 1;
                return ret;
            }
        } ;
        return Desc;
    }

    public Comparator<MyReview> compareLowScore() {
        Comparator<MyReview> Desc = new Comparator<MyReview>() {
            @Override
            public int compare(MyReview o1, MyReview o2) {
                int ret = 0;
                if (o1.getRating() < o2.getRating())
                    ret = -1;
                else if (o1.getRating() == o2.getRating())
                    ret = 0;
                else
                    ret = 1;
                return ret;
            }
        } ;
        return Desc;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.y_review_write:
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(ReviewList.this, ReviewForm.class);
                    intent.putExtra("uid", user.getUid());
                    intent.putExtra("placeId", pid);
                    intent.putExtra("placeName", pname);
                    startActivityForResult(intent, 100);
                } else {
                    Toast.makeText(getApplicationContext(), "리뷰는 로그인 후 작성할 수 있습니다.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.y_add_course:
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
                break;
            case R.id.y_filter:
                final List<String> ListItems = new ArrayList<>();
                ListItems.add("최신순");
                ListItems.add("별점 높은순");
                ListItems.add("별점 낮은순");
                final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

                final List SelectedItems  = new ArrayList();

                SelectedItems.add(selected);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //builder.setTitle("");
                builder.setSingleChoiceItems(items, selected,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SelectedItems.clear();
                                SelectedItems.add(which);
                                selected = (int) SelectedItems.get(0);
                                if (selected == 0) {
                                    Collections.sort(reviewList, compareNew());
                                    adapter.notifyDataSetChanged();
                                    adapter = new ReviewListAdapter(ReviewList.this, reviewList);
                                    listView.setAdapter(adapter);
                                }
                                else if (selected == 1) {
                                    Collections.sort(reviewList, compareHighScore());
                                    adapter.notifyDataSetChanged();
                                    adapter = new ReviewListAdapter(ReviewList.this, reviewList);
                                    listView.setAdapter(adapter);
                                }
                                else {
                                    Collections.sort(reviewList, compareLowScore());
                                    adapter.notifyDataSetChanged();
                                    adapter = new ReviewListAdapter(ReviewList.this, reviewList);
                                    listView.setAdapter(adapter);
                                }
                                dialog.dismiss(); // 누르면 바로 닫히는 형태
                            }
                        });
                builder.show();
                break;
        }
    }
}
