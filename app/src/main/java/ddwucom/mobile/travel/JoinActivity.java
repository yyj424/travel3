package ddwucom.mobile.travel;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {
    private static final String TAG = "rg";
    private DatabaseReference mPostReference;
    FirebaseAuth firebaseAuth;

    EditText etName, etID, etEmail, etPhone, etPw, etPwCheck;
    Button btnOk;
    String _id, name, email, phone, pw;
    String sort = "_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        etName = (EditText)findViewById(R.id.join_etName);
        etID = (EditText)findViewById(R.id.join_etID);
        etEmail = (EditText)findViewById(R.id.join_etEmail);
        etPhone = (EditText)findViewById(R.id.join_etPhone);
        etPw = (EditText)findViewById(R.id.join_etPW);
        etPwCheck = (EditText)findViewById(R.id.join_etPwCheck);
        btnOk = (Button)findViewById(R.id.join_btnOk);

        /*Firebase 인증 객체 선언*/
        firebaseAuth = FirebaseAuth.getInstance(); // 인스턴스 초기화

        etName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                name = etName.getText().toString();
                if(name.equals("이름을 입력해주세요.")){
                    Log.d(TAG, "이름 입력");
                    etName.setText("");
                }
            }
        });
        etID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id;
                id = etID.getText().toString();
                if(id.equals("예) travel3Good")){
                    Log.d(TAG, "아이디 입력");
                    etID.setText("");
                }
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "버튼 누름");
                String id, email, pw, pw_chk;
                id = etID.getText().toString();
                email = etEmail.getText().toString();
                pw = etPw.getText().toString();
                pw_chk = etPwCheck.getText().toString();

                Log.d(TAG, "여기까지 오류없음 -1 if문전");
                if (!id.equals("") && !pw.equals("") && !pw_chk.equals("")) {
                    // 이메일과 비밀번호가 공백이 아닌 경우
                    Log.d(TAG, "if문 들어아왔음");
                    signUp();
                } else if(email.equals("") && (pw.equals("") || pw_chk.equals(""))){
                    // 이메일과 비밀번호가 공백인 경우
                    Log.d(TAG, "이메일과 비밀번호가 공백인 경우");
                    Toast.makeText(JoinActivity.this, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }else if(!pw_chk.equals(pw)){
                    Log.d(TAG, "비밀번호 불일치 경우");
                    Toast.makeText(JoinActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void signUp(){ /*회원가입 정보 전달*/
        etEmail = (EditText)findViewById(R.id.join_etEmail);
        etPw = (EditText)findViewById(R.id.join_etPwCheck);
        email = etEmail.getText().toString();
        pw = etPw.getText().toString();
        Log.d(TAG, "create user 전");
        createUser(email, pw);
    }
    // 회원가입 -> Firebase authentification에 전달
    public void createUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "onComplete 안");
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            Log.d(TAG, "회원가입 성공");
                            AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                            builder.setTitle("회원가입 완료")
                                    .setMessage("회원가입이 완료되었습니다.")
                                    .setPositiveButton("확인", null)
                                    .show();
                            Toast.makeText(JoinActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                        } else {
                            // 회원가입 실패
                            Log.d(TAG, "회원가입 실패");
                            Toast.makeText(JoinActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.join_imgBack:
                Intent intent = new Intent(this, LoginForm.class);
                startActivity(intent);
                break;
//            case R.id.join_btnOk:
//                break;
            case R.id.btnDoubleCheck:
//                _id = etID.getText().toString();
//                name = etName.getText().toString();
//                phone = etPhone.getText().toString();
//                email = etEmail.getText().toString();
//
//                etID.requestFocus();
//                etID.setCursorVisible(true);
                break;
        }
    }
}