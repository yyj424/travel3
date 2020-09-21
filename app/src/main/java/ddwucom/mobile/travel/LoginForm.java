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
    EditText etEmail, etPW;
    TextView findID, findPW;
    Button btnLogin, btnJoin;
    FirebaseAuth firebaseAuth;
    final String TAG = "sera";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginform_main);

//        Intent intent = new Intent(this, LoadingActivity.class);
//        startActivity(intent);

        findID = (TextView) findViewById(R.id.findID);
        findPW = (TextView) findViewById(R.id.findPW);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnJoin = (Button) findViewById(R.id.btnJoin);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            //이미 로그인 되었다면 이 액티비티를 종료함
            finish();
            //그리고 profile 액티비티를 연다.
            startActivity(new Intent(getApplicationContext(),  HomeActivity.class)); //추가해 줄 ProfileActivity
        }
        etEmail = (EditText) findViewById(R.id.etEmailForm);
        etPW = (EditText) findViewById(R.id.etPwForm);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        findPW.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),  FindIdActivity.class));
            }
        });

    }
    private void userLogin(){
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
            case R.id.etPwForm:
                etPW.setText("");
                break;
            case R.id.etEmailForm:
                etEmail.setText("");
                break;
        }
    }
}