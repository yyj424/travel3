package ddwucom.mobile.travel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    Button btnLogout;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnLogout = findViewById(R.id.btnLogout);

    }

    public void onClick(View v) { // 충돌 위험 있으니 push는 하지 마삼!!
        switch (v.getId()) { // 본인 필요한 부분만 주석 풀어서 쓰세욥.
//            case R.id.btn_plan:
//                Intent intent = new Intent(this, );
//                startActivity(intent);
//            case R.id.btn_record:
//                Intent intent = new Intent(this, RecordListActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.btn_room:
//                Intent intent = new Intent(this, );
//                startActivity(intent);
//                break;
//            case R.id.btn_course:
//                Intent intent = new Intent(this, );
//                startActivity(intent);
//                break;
//            case R.id.btn_map:
//                Intent intent = new Intent(this, );
//                startActivity(intent);
//                break;
            case R.id.btnLogout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(this, LoginForm.class));
                break;
        }
    }
}
