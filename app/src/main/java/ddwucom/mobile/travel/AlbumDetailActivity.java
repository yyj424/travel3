package ddwucom.mobile.travel;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

    ArrayList<String> imageList;
    int pos;
    int downPos;
    ViewPager viewPager;
    Toolbar toolbar;
    SectionsPagerAdapter sectionsPagerAdapter;
    final static String EXT_FILE_NAME = "extfile.txt";

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
                downPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AlbumDetailActivity.this);
                builder.setTitle("파일을 저장하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ImageAsyncTask imgTask = new ImageAsyncTask();
                                imgTask.execute(imageList.get(downPos));
                            }
                        });
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.show();
                return false;
            }
        });
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
                FileOutputStream outStream = null;
                File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                        "travel3");
                if (!file.mkdirs()) { // 만들어져있으면 make 못함
                    Log.d(TAG, "directory not created");
                }
                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                Log.d(TAG,"filename" + fileName);
                File outFile = new File(file.getPath(), fileName);
                try {
                    outStream = new FileOutputStream(outFile);
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


