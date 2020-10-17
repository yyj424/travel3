package ddwucom.mobile.travel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    Button btnLogout;
    ImageView btnHome, btnGroup, btnCourse, btnMap;
    private FirebaseAuth firebaseAuth;
    boolean [] clicked = {true, false, false, false};
    int [] btn_names = {R.id.btn_home, R.id.btn_friends, R.id.btn_course, R.id.btn_map};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //btnLogout = findViewById(R.id.btnLogout);
        firebaseAuth = FirebaseAuth.getInstance();

        btnHome = findViewById(R.id.btn_home);
        btnGroup = findViewById(R.id.btn_friends);
        btnCourse = findViewById(R.id.btn_course);
        btnMap = findViewById(R.id.btn_map);

    }

    public void onClick(View v) { // 충돌 위험 있으니 push는 하지 마삼!!
//        Drawable tempImg, tempRes;
//        Bitmap tmpBitmap, tmpBitmapRes;
        switch (v.getId()) { // 본인 필요한 부분만 주석 풀어서 쓰세욥.
//
//            case R.id.btnLogout:
//
//                if(firebaseAuth.getCurrentUser() != null){
//                    //이미 로그인 되었다면 이 액티비티를 종료함
//                    firebaseAuth.signOut();
//                    finish();
//                    startActivity(new Intent(getApplicationContext(), LoginForm.class));
//                }
//                break;
            case R.id.btn_home:
                    btnHome.setImageResource(R.drawable.home_icon_yellow);
                    btnGroup.setImageResource(R.drawable.friends_icon_grey);
                    btnCourse.setImageResource(R.drawable.course_icon_grey);
                    btnMap.setImageResource(R.drawable.map_icon_grey);

//                tempImg = btnHome.getDrawable();
//                tempRes = HomeActivity.this.getResources().getDrawable(R.drawable.home_icon_yellow);
//                tmpBitmap = ((BitmapDrawable)tempImg).getBitmap();
//                tmpBitmapRes = ((BitmapDrawable)tempRes).getBitmap();
//
//                if(tmpBitmap.equals(tmpBitmapRes)) {
//                    btnHome.setImageResource(R.drawable.home_icon_grey);
//                    //로직 수행
//                }else{
//                    btnHome.setImageResource(R.drawable.home_icon_yellow);
//                    //로직 수행
//                }
                break;
            case R.id.btn_friends:
                btnHome.setImageResource(R.drawable.home_icon_grey);
                btnGroup.setImageResource(R.drawable.friends_icon_yellow);
                btnCourse.setImageResource(R.drawable.course_icon_grey);
                btnMap.setImageResource(R.drawable.map_icon_grey);
                break;
            case R.id.btn_course:
                btnHome.setImageResource(R.drawable.home_icon_grey);
                btnGroup.setImageResource(R.drawable.friends_icon_grey);
                btnCourse.setImageResource(R.drawable.course_icon_yellow);
                btnMap.setImageResource(R.drawable.map_icon_grey);
                break;
            case R.id.btn_map:
                btnHome.setImageResource(R.drawable.home_icon_grey);
                btnGroup.setImageResource(R.drawable.friends_icon_grey);
                btnCourse.setImageResource(R.drawable.course_icon_grey);
                btnMap.setImageResource(R.drawable.map_icon_yellow);
                break;
        }
    }

}
