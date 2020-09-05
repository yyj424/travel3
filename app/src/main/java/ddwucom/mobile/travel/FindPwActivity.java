package ddwucom.mobile.travel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FindPwActivity extends AppCompatActivity {
    EditText etName, etEmail, etID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);
    }
    public void onClick (View v){
        switch(v.getId()){
            case R.id.findPW_imgNext:
                Intent intent = new Intent(this, ChangePWActivity.class);
                startActivity(intent);
                break;
            case R.id.findPW_signUp:
                Intent intent1 = new Intent(this, JoinActivity.class);
                startActivity(intent1);
                break;
            case R.id.findPW_login:
                Intent intent2 = new Intent(this, LoginForm.class);
                startActivity(intent2);
                break;
            case R.id.findPW_etName:
                etName = findViewById(R.id.findPW_etName);
                etName.setText("");
                break;
            case R.id.findPW_etID:
                etID = findViewById(R.id.findPW_etID);
                etID.setText("");
                break;
            case R.id.findPW_etEmail:
                etEmail = findViewById(R.id.findPW_etEmail);
                etEmail.setText("");
                break;
        }
    }
}
