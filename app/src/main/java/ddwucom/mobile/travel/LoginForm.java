package ddwucom.mobile.travel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginForm extends AppCompatActivity {
    EditText etName, etPW;
    TextView findID, findPW;
    Button btnLogin, btnJoin;
    String loginId, loginPwd;
    FirebaseAuth firebaseAuth;
    final String TAG = "sera";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginform_main);

        findID = findViewById(R.id.findID);
        findPW = findViewById(R.id.findPW);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnJoin = (Button) findViewById(R.id.btnJoin);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            //이미 로그인 되었다면 이 액티비티를 종료함
            finish();
            //그리고 profile 액티비티를 연다.
            startActivity(new Intent(getApplicationContext(),  HomeActivity.class)); //추가해 줄 ProfileActivity
        }
        etName = (EditText) findViewById(R.id.etNameForm);
        etPW = (EditText) findViewById(R.id.etPwForm);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"userLogin으로 이동");
                userLogin();
            }
        });
        //Toast.makeText(LoginForm.this, etName.getText().toString() + "님 환영합니다.", Toast.LENGTH_SHORT).show();

//        Intent intent = new Intent(LoginForm.this, HomeActivity.class);
//        startActivity(intent);
//        finish();
    }
    private void userLogin(){
        String email = etName.getText().toString().trim();
        String password = etPW.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "email을 입력해주세요",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG,"firebase로 이동");
        //Logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG,"task안으로 이동");
                        if(task.isSuccessful()){
                            Log.d(TAG,"로그인 성공");
                            Toast.makeText(getApplicationContext(),"로그인 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginForm.this, HomeActivity.class);
                            startActivity(intent);
//                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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