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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginForm extends AppCompatActivity {
    EditText etEmail, etPW;
    TextView findID, findPW;
    Button btnLogin, btnJoin;
    FirebaseAuth firebaseAuth;
    final String TAG = "sera";
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginform_main);

        //로그인 화면에서의 '아이디 찾기' '비밀번호 재설정'
       findID = (TextView) findViewById(R.id.findID);
        findPW = (TextView) findViewById(R.id.findPW);

        //상단바 '로그인' '회원가입'
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnJoin = (Button) findViewById(R.id.btnJoin);

        //계정가져오기 & 데이터 베이스 가져오기
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();

        if(firebaseAuth.getCurrentUser() != null){
            //이미 로그인 되었다면 이 액티비티를 종료함
            finish();
            //그리고 profile 액티비티를 연다.
            startActivity(new Intent(getApplicationContext(),  HomeActivity.class)); //추가해 줄 ProfileActivity
        }
        etEmail = (EditText) findViewById(R.id.etEmailForm);
        etPW = (EditText) findViewById(R.id.etPwForm);

    }
    private void userLogin(){ //login
        String email = etEmail.getText().toString().trim();
        String password = etPW.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "email을 입력해주세요",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show();
            return;
        }
        //Logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"로그인 성공", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void onClick (View v){
        switch (v.getId()) {
            case R.id.etPwForm:
                etPW.setHint("");
                break;
            case R.id.etEmailForm:
                etEmail.setHint("");
                break;
            case R.id.findID:
                Intent intent2 = new Intent(this, FindIdActivity.class);
                startActivity(intent2);
                break;
            case R.id.findPW:
                Intent intent3 = new Intent(this, FindPwActivity.class);
                startActivity(intent3);
                break;
            case R.id.btnLogin:
                 userLogin();
                 break;
            case R.id.btnJoin:
                Intent intent1 = new Intent(LoginForm.this, JoinActivity.class);
                startActivity(intent1);
                break;
        }
    }
}