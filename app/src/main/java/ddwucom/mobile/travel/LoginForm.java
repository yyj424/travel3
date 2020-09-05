package ddwucom.mobile.travel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginForm extends AppCompatActivity {
    EditText etName, etPW;
    TextView findID, findPW;
    Button btnLogin, btnJoin;
    String loginId, loginPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginform_main);

        etName = (EditText) findViewById(R.id.etNameForm);
        etPW = (EditText) findViewById(R.id.etPwForm);

        findID = findViewById(R.id.findID);
        findPW = findViewById(R.id.findPW);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnJoin = (Button) findViewById(R.id.btnJoin);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

        loginId = auto.getString("etName", null);
        loginPwd = auto.getString("etPW", null);

        if (loginId != null && loginPwd != null) {
            if (loginId.equals("aaa") && loginPwd.equals("1234")) {
                Toast.makeText(LoginForm.this, loginId + "님 자동로그인 입니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginForm.this, HomeActivity.class);
            }
        }
        SharedPreferences.Editor autoLogin = auto.edit();
        autoLogin.putString("etName", etName.getText().toString());
        autoLogin.putString("etPW", etPW.getText().toString());
        //꼭 commit()을 해줘야 값이 저장됩니다 ㅎㅎ
        autoLogin.commit();
        Toast.makeText(LoginForm.this, etName.getText().toString() + "님 환영합니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginForm.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClick (View v){
        switch (v.getId()) {
//            case R.id.btnLogin:
//                Intent intent1 = new Intent(this, HomeActivity.class);
//                startActivity(intent1);
//                break;
            case R.id.btnJoin:
                Intent intent2 = new Intent(this, JoinActivity.class);
                startActivity(intent2);
                break;
            case R.id.findID:
                Intent intent3 = new Intent(this, FindIdActivity.class);
                startActivity(intent3);
                break;
            case R.id.findPW:
                Intent intent4 = new Intent(this, FindPwActivity.class);
                startActivity(intent4);
                break;
            case R.id.etNameForm:
                etName.setText("");
                break;
        }
    }
}