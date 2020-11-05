package ddwucom.mobile.travel;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.viewpager.widget.ViewPager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class AlbumDetailActivity extends AppCompatActivity {
    private static final String TAG = "AlbumDetailActivity";
    final int PERMISSIONS_REQUEST_CODE = 1;
    ArrayList<String> imageList;
    int pos;
    int downPos;
    String albumName;
    ViewPager viewPager;
    Toolbar toolbar;
    SectionsPagerAdapter sectionsPagerAdapter;
    private GestureDetectorCompat mDetector;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        albumName = getIntent().getStringExtra("albumName");
        imageList = getIntent().getStringArrayListExtra("imageList");
        Log.d(TAG, String.valueOf(imageList.size()));
        pos = getIntent().getIntExtra("pos", 0);
        downPos = pos;

        setTitle(albumName);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), imageList);

        viewPager = findViewById(R.id.container);
        viewPager.setPageTransformer(true, new DepthPageTransformer());

        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setCurrentItem(pos);

        mDetector = new GestureDetectorCompat(this,new Gesture());

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mDetector.onTouchEvent(event);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                downPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }

    class Gesture extends GestureDetector.SimpleOnGestureListener{

        public void onLongPress(MotionEvent ev) {
            TextView dialogTitle = new TextView(AlbumDetailActivity.this);
            dialogTitle.setText("사진 저장");
            dialogTitle.setIncludeFontPadding(false);
            dialogTitle.setTypeface(ResourcesCompat.getFont(AlbumDetailActivity.this, R.font.tmoney_regular));
            dialogTitle.setGravity(Gravity.CENTER);
            dialogTitle.setPadding(10, 70, 10, 70);
            dialogTitle.setTextSize(20F);
            dialogTitle.setBackgroundResource(R.color.colorTop);
            dialogTitle.setTextColor(Color.DKGRAY);

            TextView dialogText = new TextView(AlbumDetailActivity.this);
            dialogText.setText("사진을 저장하시겠습니까?");
            dialogText.setIncludeFontPadding(false);
            dialogText.setTypeface(ResourcesCompat.getFont(AlbumDetailActivity.this, R.font.tmoney_regular));
            dialogText.setGravity(Gravity.CENTER);
            dialogText.setPadding(10, 30, 10, 0);
            dialogText.setTextSize(15F);

            FrameLayout container = new FrameLayout(AlbumDetailActivity.this);
            FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
            dialogText.setLayoutParams(params);
            container.addView(dialogText);

            AlertDialog alertDialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(AlbumDetailActivity.this, R.style.DialogTheme);
            builder.setCustomTitle(dialogTitle)
                    .setView(container)
                    .setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ImageAsyncTask imgTask = new ImageAsyncTask();
                                    imgTask.execute(imageList.get(downPos));
                                }
                            })
                    .setNegativeButton("아니오", null);
            alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getWindow().setLayout(800, 700);
        }
    }

    class ImageAsyncTask extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            String imgAddress = strings[0];
            Bitmap image = downloadImage(imgAddress);
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isExternalStorageWritable()) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AlbumDetailActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
                }
                String destPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
                final String folderPath = destPath+"/travel3";
                    File folder = new File(folderPath);
                    if (!folder.exists()) {
                        File wallpaperDirectory = new File(folderPath);
                        wallpaperDirectory.mkdirs();
                    }


                FileOutputStream outStream = null;

                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "travel3");
                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(file.getPath(), fileName);
                try {
                    outStream = new FileOutputStream(outFile);
                    Log.d(TAG, "outStream " + outStream.toString());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    outStream.flush();
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private boolean isExternalStorageWritable() {
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        }
    }

    protected Bitmap downloadImage(String address) {
        HttpURLConnection conn = null;
        InputStream stream = null;
        Bitmap result = null;

        try {
            URL url = new URL(address);
            conn = (HttpURLConnection)url.openConnection();
            stream = getNetworkConnection(conn);
            result = readStreamToBitmap(stream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try { stream.close(); }
                catch (IOException e) { e.printStackTrace(); }
            }
            if (conn != null) conn.disconnect();
        }

        return result;
    }

    private InputStream getNetworkConnection(HttpURLConnection conn) throws Exception {
        conn.setReadTimeout(3000);
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + conn.getResponseCode());
        }

        return conn.getInputStream();
    }

    /* InputStream을 전달받아 비트맵으로 변환 후 반환 */
    protected Bitmap readStreamToBitmap(InputStream stream) {
        return BitmapFactory.decodeStream(stream);
    }


}


