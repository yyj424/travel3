package ddwucom.mobile.travel;

import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.util.List;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnMultiSelectedListener;
import gun0912.tedimagepicker.databinding.ItemAlbumBinding;

public class ReviewForm extends AppCompatActivity {
    EditText rating;
    EditText reviewContent;
    ImageView image;
    ProgressBar category1;
    ProgressBar category2;
    ProgressBar category3;
    ProgressBar category4;
    private List<Uri> selectedUriList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_write_form);

        rating = findViewById(R.id.y_rating);
        reviewContent = findViewById(R.id.y_reivewContent);
        image = findViewById(R.id.y_reviewPhoto);
        category1 = findViewById(R.id.ctg1);
        category2 = findViewById(R.id.ctg2);
        category3 = findViewById(R.id.ctg3);
        category4 = findViewById(R.id.ctg4);

        TedImagePicker.with(this)
                .startMultiImage(new OnMultiSelectedListener() {
                    @Override
                    public void onSelected(List<? extends Uri> list) {
                        //showMultiImage(list);
                    }
                });
//        TedImagePicker.with(this)
//                .startMultiImage(uriList -> {
//                    showMultiImage(uriList);
//                });
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.y_addPhoto:
                //Intent intent = new Intent(this, );
                //startActivity(intent);
                break;
            case R.id.y_removePhoto:

                break;
        }
    }
}
