package ddwucom.mobile.travel;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnMultiSelectedListener;

public class ReviewForm extends AppCompatActivity {
    TextView rating;
    EditText reviewContent;
    RecyclerView listview;
    ProgressBar category1;
    ProgressBar category2;
    ProgressBar category3;
    ProgressBar category4;
    private ReviewImageAdapter reviewImageAdapter;
    private ArrayList<Uri> ImageList = null;
    private RatingBar rb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_write_form);

        rating = findViewById(R.id.y_rating);
        reviewContent = findViewById(R.id.y_reivewContent);
        listview = findViewById(R.id.y_RimageList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listview.setLayoutManager(layoutManager);
        ImageList = new ArrayList();
        category1 = findViewById(R.id.ctg1);
        category2 = findViewById(R.id.ctg2);
        category3 = findViewById(R.id.ctg3);
        category4 = findViewById(R.id.ctg4);
        rb = findViewById(R.id.ratingBar);
        final float[] r = {0};

        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                r[0] = rating;
            }
        });
        rating.setText((int) r[0]);
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.y_addPhoto:
                TedImagePicker.with(this)
                        .startMultiImage(new OnMultiSelectedListener() {
                            @Override
                            public void onSelected(List<? extends Uri> list) {
                                ImageList.addAll(list);
                                reviewImageAdapter = new ReviewImageAdapter(ReviewForm.this, ImageList);
                                listview.setAdapter(reviewImageAdapter);
                            }
                        });
                break;
        }
    }
}
