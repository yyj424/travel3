package ddwucom.mobile.travel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void onClick(View v) { // 충돌 위험 있으니 push는 하지 마삼!!
        Intent intent;
        switch (v.getId()) { // 본인 필요한 부분만 주석 풀어서 쓰세욥.
//            case R.id.btn_plan:
//                intent = new Intent(this, );
//                break;
//            case R.id.btn_record:
//                intent = new Intent(this, );
//                break;
//            case R.id.btn_map:
//                intent = new Intent(this, );
//                break;
//            case R.id.btn_course:
//                intent = new Intent(this, );
//                break;
//            case R.id.btn_friends:
//                intent = new Intent(this, );
//                break;
        }
        startActivity(intent);
    }
}
