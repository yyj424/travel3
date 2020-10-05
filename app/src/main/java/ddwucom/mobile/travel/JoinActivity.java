package ddwucom.mobile.travel;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {
    private static final String TAG = "sera";
    private DatabaseReference mPostReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private FirebaseUser user;

    UserInfo userInfo;
    List userList;
    static boolean isDoubleID = false;

    EditText etName, etID, etEmail, etPhone, etPw, etPwCheck;
    Button btnOk;
    TextView errMSG;
    String _id, name, email, phone, pw, currentUid;
    String sort = "_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();

        userList = new ArrayList<>();

        etName = (EditText)findViewById(R.id.join_etName);
        etID = (EditText)findViewById(R.id.join_etID);
        etEmail = (EditText)findViewById(R.id.join_etEmail);
        etPhone = (EditText)findViewById(R.id.join_etPhone);
        etPw = (EditText)findViewById(R.id.join_etPW);
        etPwCheck = (EditText)findViewById(R.id.join_etPwCheck);
        btnOk = (Button)findViewById(R.id.join_btnOk);
        errMSG = (TextView)findViewById(R.id.errorMessage);

        /*Firebase 인증 객체 선언*/
        firebaseAuth = FirebaseAuth.getInstance(); // 인스턴스 초기화
        errMSG.setText("");
        etName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                name = etName.getText().toString();
                if(name.equals("이름을 입력해주세요.")){
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
                    etID.setText("");
                }
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id, email, pw, pw_chk;
                id = etID.getText().toString();
                email = etEmail.getText().toString();
                pw = etPw.getText().toString();
                pw_chk = etPwCheck.getText().toString();

                if (!id.equals("") && !pw.equals("") && !pw_chk.equals("")) {
                    // 이메일과 비밀번호가 공백이 아닌 경우
                    signUp();
                } else if(email.equals("") && (pw.equals("") || pw_chk.equals(""))){
                    // 이메일과 비밀번호가 공백인 경우
                    Toast.makeText(JoinActivity.this, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }else if(!pw_chk.equals(pw)){
                    Toast.makeText(JoinActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void signUp(){ /*회원가입 정보 전달*/
//        etEmail = (EditText)findViewById(R.id.join_etEmail);
//        etPw = (EditText)findViewById(R.id.join_etPwCheck);
        email = etEmail.getText().toString();
        pw = etPw.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pw)){
            Toast.makeText(this, "Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }
        check_validation(email, pw);
//        if(check_validation(email, pw) == 0)
//            Toast.makeText(this, "비밀번호를 재설정해주세요.", Toast.LENGTH_SHORT).show();
    }
    // 회원가입 -> Firebase authentification에 전달
    public void createUser(final String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && !isDoubleID) {
                            // 회원가입 성공
                            userInfo = new UserInfo();

                            Log.d(TAG, "uid log로 보기 " + task.getResult().getUser().getUid());
                            // 객체에 이미지 외의 데이터 저장
                            String nickname = etID.getText().toString();
                            String phone = etPhone.getText().toString();
//                            String uid = .toString();

                            userInfo.setUid(task.getResult().getUser().getUid());
                            userInfo.setNickname(nickname);
                            userInfo.setPhone(phone);
                            userInfo.setEmail(email);

                            // db에 유저정보 저장
                            dbRef.child("user_list").push().setValue(userInfo);
                            AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                            builder.setTitle("회원가입 완료")
                                    .setMessage(email + " 회원님 환영합니다.")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .show();

                            } else {// 회원가입 실패
                                if(!task.isSuccessful()){
                                    Log.d(TAG, "회원가입 실패 - 시스템 회원가입 오류");
                                    errMSG.setText(R.string.error);
                                    Toast.makeText(JoinActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                                }
                                else if(!isDoubleID){
                                    Log.d(TAG, "회원가입 실패 - 중복확인");
                                    errMSG.setText(R.string.error);
                                    Toast.makeText(JoinActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                                }
                        }
                    }
                });
    }
    public void checkId(){
        dbRef  = database.getReference("user_list");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "CLick22!!");
                String etNickname = etID.getText().toString();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "ValueEventListener : " + snapshot.child("nickname").getValue());
                    if(etNickname.equals(snapshot.child("nickname").getValue().toString())) {
                        Toast.makeText(JoinActivity.this, "중복된 아이디 입니다. 다시입력하세요 ", Toast.LENGTH_SHORT).show();
                        isDoubleID = true;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return;
    }
    public void check_validation(String email, String password) {
        // 비밀번호 유효성 검사식1 : 숫자, 특수문자가 포함되어야 한다.
//        String val_symbol = "([0-9].*[!,@,#,^,&,*,(,)])|([!,@,#,^,&,*,(,)].*[0-9])";
//        // 비밀번호 유효성 검사식2 : 영문자 대소문자가 적어도 하나씩은 포함되어야 한다.
//        String val_alpha = "([a-z].*[A-Z])|([A-Z].*[a-z])";
//        // 정규표현식 컴파일
//        Pattern pattern_symbol = Pattern.compile(val_symbol);
//        Pattern pattern_alpha = Pattern.compile(val_alpha);
//
//        Matcher matcher_symbol = pattern_symbol.matcher(password);
//        Matcher matcher_alpha = pattern_alpha.matcher(password);

//        if (matcher_symbol.find() && matcher_alpha.find()) {
            // email과 password로 회원가입 진행
            createUser(email, password);
//            return 1;
//        }else {
//            Toast.makeText(this, "비밀번호로 부적절합니다", Toast.LENGTH_SHORT).show();
//            return 0;
//        }
    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.join_imgBack:
                finish();
                break;
//            case R.id.join_btnOk:
//                break;
            case R.id.btnDoubleCheck:
                checkId();
                break;
        }
    }
}