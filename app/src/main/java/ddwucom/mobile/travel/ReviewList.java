package ddwucom.mobile.travel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReviewList extends AppCompatActivity {

    private ArrayList<MyReview> reviewList;
    private ReviewListAdapter adapter;
    private ListView listView;
    String pid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_list);

        Intent intent = getIntent();
        pid = intent.getStringExtra("placeId");
        reviewList = new ArrayList<>();

//        reviewList.add(new MyReview(1, "user1", (float) 3.5, "20/10/14", "리 뷰 내 용1", 10, 20, 30, 40));
//        reviewList.add(new MyReview(2, "user2", (float) 5.0, "20/10/15", "리 뷰 내 용2", 11, 22, 33, 44));
//        reviewList.add(new MyReview(3, "user3", (float) 2.5, "20/10/16", "리 뷰 내 용3", 01, 02, 03, 04));

        adapter = new ReviewListAdapter(this, R.layout.reviewlist_adapter_view, reviewList);

        listView = findViewById(R.id.y_review_list);
        listView.setAdapter(adapter);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.y_review_write:
                Intent intent = new Intent(ReviewList.this, ReviewForm.class);
                intent.putExtra("placeId", pid);
                startActivity(intent);
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
