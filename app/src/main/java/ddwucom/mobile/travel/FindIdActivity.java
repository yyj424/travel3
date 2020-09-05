package ddwucom.mobile.travel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FindIdActivity extends AppCompatActivity {
    EditText etName, etEmail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);
    }
    public void onClick(View v){
        switch(v.getId()) {
            case R.id.findID_imgNext:
                /*
                * String a = etName.getText();
                * String b = etEmail.getText();
                * a와 b 데이터 저장 후 >> 다음 페이징 ID 보여주기
                *
                * */
                Intent intent = new Intent(this,FoundIDActivity.class);
                startActivity(intent);
                break;
            case R.id.findID_signUp:
                Intent intent1 = new Intent(this, JoinActivity.class);
                startActivity(intent1);
                break;
            case R.id.findID_Login:
                Intent intent2 = new Intent(this, LoginForm.class);
                startActivity(intent2);
                break;
            case R.id.findID_etName:
                etName = findViewById(R.id.findID_etName);
                etName.setText("");
                break;
            case R.id.findID_etEmail:
                etEmail = findViewById(R.id.findID_etEmail);
                etName.setText("");
                break;
        }
    }
}
