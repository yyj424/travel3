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
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    //user의 정보 저장할 객체 선언
    UserInfo userInfo;
    List userList;

    static boolean isDoubleID = false, isSame = false, isChecked = false; //isSame : 비밀번호 일치 여부에 사용되는 변수
                                                                          //isChecked : 중복체크했는지.
    static boolean isBlank= true; //필수요소가 blank인지 확인하는 변수

    EditText etName, etID, etEmail, etPhone, etPw, etPwCheck;
    Button btnOk;
    TextView errMSG;
    String email, pw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();

        userList = new ArrayList<>();

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

        //nickname입력 EditText
        etID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id;
                id = etID.getText().toString();
                if(id.equals("입력 후 중복확인 해주세요.")){
                    etID.setText("");
                }
            }
        });

        //회원가입 완료 버튼 클릭 리스너
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EditText에서 정보가져오기
                String id, email, pw, pw_chk;
                id = etID.getText().toString();
                email = etEmail.getText().toString();
                pw = etPw.getText().toString();
                pw_chk = etPwCheck.getText().toString();

                Log.d("sera", "pw : "+pw + "   pw_chk : " + pw_chk );

                if(email.equals("") && (pw.equals("") || pw_chk.equals(""))){
                    // 이메일과 비밀번호나 비밀번호 확인이 공백인 경우
                    Log.d("sera", "이메일과 비밀번호나 비밀번호 확인이 공백인 경우" );
                    Toast.makeText(JoinActivity.this, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
//                    if(email.equals("")) {
//                        Toast.makeText(JoinActivity.this, "계정을 입력하세요.", Toast.LENGTH_LONG).show();
//                    }else if(pw.equals("")){
//                        Toast.makeText(JoinActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
//                    }else if(pw.equals("")){
//                        Toast.makeText(JoinActivity.this, "비밀번호 확인해주세요", Toast.LENGTH_LONG).show();
//                    }
                }

                if(pw_chk.equals(pw)){
                    Log.d("sera", "isSame = true;" );
                    isSame = true;
                    Log.d("sera", "isSame" + isSame);
                }else{
                    Toast.makeText(JoinActivity.this, "비밀번호가 일치하지 않습니다. 확인해주세요", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!id.equals("") && !pw.equals("") && !pw_chk.equals("")) { //*******validation필요
                    // 이메일과 비밀번호가 공백이 아닌 경우 >> 회원가입 진행
                    Log.d("sera", "이메일과 비밀번호가 공백이 아닌 경우" );
                   isBlank = false;
                    Log.d("sera", "isBlank" + isBlank);
                   // signUp();
                }
                Log.d("sera", "isBlank : " + isBlank + "isSame : " + isSame + "isDoubleID : "+ isDoubleID + "isChecked : " + isChecked);
                if (isSame && !isBlank && !isDoubleID && isChecked) { //*******validation필요
                    // 이메일과 비밀번호가 공백이 아닌 경우 >> 회원가입 진행
                    Log.d("sera", "validation" );
                    signUp();
                }
            }
        });
    }
    public void signUp(){ /*회원가입 정보 전달*/
        Log.d("sera", "signUp()" );
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
    }
    // 회원가입 -> Firebase authentification에 전달
    public void createUser(final String email, String password) {
        Log.d("sera", "create_USer Method" );
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("sera", "task Successful" );
//                            if(!isDoubleID){
//                                Toast.makeText(JoinActivity.this, "아이디 중복확인 해주세요.", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
                            // 회원가입 성공
                            userInfo = new UserInfo();

                            Log.d("sera", "uid log로 보기 " + task.getResult().getUser().getUid());
                            // 객체에 이미지 외의 데이터 저장
                            String nickname = etID.getText().toString();
                            String phone = etPhone.getText().toString();

                            userInfo.setUid(task.getResult().getUser().getUid());
                            userInfo.setNickname(nickname);
                            userInfo.setPhone(phone);
                            userInfo.setEmail(email);

                            // db에 유저정보 저장
                            dbRef = database.getReference();
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

                            }
                        else {// 회원가입 실패
                            Log.d("sera", "not Succesful" );
                                if(!task.isSuccessful()){
                                    Log.d("sera", "회원가입 실패 - 시스템 회원가입 오류");
                                    errMSG.setText("회원가입 실패 - 시스템 회원가입 오류");
                                    Toast.makeText(JoinActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                else if(!isDoubleID){
                                    Log.d("sera", "회원가입 실패 - 중복확인");
                                    errMSG.setText("회원가입 실패 - 중복확인");
                                    Toast.makeText(JoinActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                        }
                    }
                });
    }
    public void checkId(){ //중복체크 버튼 누르면
        //userList의 DB내용 가져오기...
        Log.d("sera", "중복 CLick11!!");
        isChecked = true;
        dbRef  = database.getReference("user_list");

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("sera", "CLick22!!");
                String etNickname = etID.getText().toString();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("sera", "ValueEventListener : " + snapshot.child("nickname").getValue());
                    if(etNickname.equals(snapshot.child("nickname").getValue().toString())) {
                        Toast.makeText(JoinActivity.this, "중복된 아이디 입니다. 다시입력하세요 ", Toast.LENGTH_SHORT).show();
                        isDoubleID = true;
                        return;
                    }
                }
                //체크를 했고, 중복된 아이디가 아니다.
                if(!isDoubleID && isChecked){
                    Log.d("sera", "체크를 했고, 중복된 아이디가 아니다. isChecked: " + isChecked + "isDoubled : "+ isDoubleID);
                    isDoubleID = false;
                }
                Toast.makeText(JoinActivity.this, "사용가능한 아이디 입니다.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public static final Pattern VALID_PASSWOLD_REGEX_ALPHA_NUM
            = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{6,16}$"); // 6자리 ~ 16자리까지 가능

    public static boolean validatePassword(String pwStr) { //비번유효성 검사
        Matcher matcher = VALID_PASSWOLD_REGEX_ALPHA_NUM.matcher(pwStr);
        return matcher.matches();
    }
    public void check_validation(String email, String password) {
        Log.d("sera", "check_validation" );

        if(!validatePassword(password))
           Toast.makeText(JoinActivity.this, "비밀번호를 다시 설정해 주세요.", Toast.LENGTH_SHORT).show();
       else{
           createUser(email, password);
       }
    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.join_imgBack:
                finish();
                break;
            case R.id.btnDoubleCheck:
                checkId();
                break;
        }
    }
}