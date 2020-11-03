package ddwucom.mobile.travel;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class AlbumDetailActivity extends AppCompatActivity {
    private static final String TAG = "AlbumDetailActivity";

    ArrayList<String> imageList;
    int pos;
    ViewPager viewPager;
    Toolbar toolbar;
    SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        imageList = getIntent().getStringArrayListExtra("imageList");
        Log.d(TAG, String.valueOf(imageList.size()));
        pos = getIntent().getIntExtra("pos", 0);

        // !!!!앨범명 가져오는거 구현해야됨
        setTitle("기본");

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), imageList);

        viewPager = findViewById(R.id.container);
        viewPager.setPageTransformer(true, new DepthPageTransformer());

        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setCurrentItem(pos);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


}


