package ddwucom.mobile.travel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
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
    private ReviewImageAdapter reviewImageAdapter;
    private ArrayList<Uri> ImageList = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_write_form);

        final TextView ratingView = findViewById(R.id.y_rating);
        reviewContent = findViewById(R.id.y_reivewContent);
        listview = findViewById(R.id.y_RimageList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listview.setLayoutManager(layoutManager);
        ImageList = new ArrayList();
        seekbar1 = findViewById(R.id.seekBar1);
        seekbar2 = findViewById(R.id.seekBar2);
        seekbar3 = findViewById(R.id.seekBar3);
        seekbar4 = findViewById(R.id.seekBar4);
        sbView1 = findViewById(R.id.y_sbView1);
        sbView2 = findViewById(R.id.y_sbView2);
        sbView3 = findViewById(R.id.y_sbView3);
        sbView4 = findViewById(R.id.y_sbView4);
        RatingBar ratingBar = findViewById(R.id.ratingBar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingView.setText(String.valueOf(rating));
            }
        });

        seekBarListener(seekbar1, sbView1);
        seekBarListener(seekbar2, sbView2);
        seekBarListener(seekbar3, sbView3);
        seekBarListener(seekbar4, sbView4);
    }

    public void seekBarListener(SeekBar sb, final TextView tv) {
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv.setText(String.valueOf(seekBar.getProgress())+"점");
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
                                reviewImageAdapter = new ReviewImageAdapter(ReviewForm.this, ImageList);
                                listview.setAdapter(reviewImageAdapter);
                            }
                        });
                break;
            case R.id.y_reviewRegister:
                Intent intent = new Intent(ReviewForm.this, ReviewList.class);
                startActivity(intent);
                break;
        }
    }
}
