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
    private static final String TAG = "RegisterActivity";
    private DatabaseReference mPostReference;
    private FirebaseAuth firebaseAuth;

    EditText etName, etID, etEmail, etPhone, etPw, etPwCheck;
    Button btnOk;
    String _id, name, email, phone, pw;
    String sort = "_id";

    static ArrayList<String> arrayIndex =  new ArrayList<String>();

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

    }
    public void signUp(){ /*회원가입 정보 전달*/
        etEmail = (EditText)findViewById(R.id.join_etEmail);
        etPw = (EditText)findViewById(R.id.join_etPwCheck);
        email = etEmail.getText().toString();
        pw = etPw.getText().toString();

        createUser(email, pw);
    }
    // 회원가입 -> Firebase authentification에 전달
    public void createUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                            builder.setTitle("회원가입 완료")
                                    .setMessage("회원가입이 완료되었습니다.")
                                    .setPositiveButton("확인", null)
                                    .show();
                            Toast.makeText(JoinActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                        } else {
                            // 회원가입 실패
                            Toast.makeText(JoinActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
//    public void postFirebaseDatabase(boolean add){
//        mPostReference = FirebaseDatabase.getInstance().getReference();
//
//        Map<String, Object> childUpdates = new HashMap<>();
//        Map<String, Object> postValues = null;
//
//        if(add){
//            FirebasePost post = new FirebasePost(_id, name, phone, email);
//            postValues = post.toMap();
//        }
//        childUpdates.put("/id_list/" + _id, postValues);
//        mPostReference.updateChildren(childUpdates);
//    }
//    public void getFirebaseDatabase(){
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.e("getFirebaseDatabase", "key: " + dataSnapshot.getChildrenCount());
//
//                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//                    String key = postSnapshot.getKey();
//                    FirebasePost get = postSnapshot.getValue(FirebasePost.class);
//                    String[] info = {get._id, get.name, get.phone, get.email};
//                    String Result = setTextLength(info[0],10) + setTextLength(info[1],10) + setTextLength(info[2],10) + setTextLength(info[3],10);
//
//                    arrayIndex.add(key);
//
//                    Log.d("getFirebaseDatabase", "key: " + key);
//                    Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2] + info[3]);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.w("getFirebaseDatabase","loadPost:onCancelled", databaseError.toException());
//            }
//        };
//        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("id_list").orderByChild(sort);
//        sortbyAge.addListenerForSingleValueEvent(postListener);
//    }
//    public String setTextLength(String text, int length){
//        if(text.length()<length){
//            int gap = length - text.length();
//            for (int i=0; i<gap; i++){
//                text = text + " ";
//            }
//        }
//        return text;
//    }
//    public boolean IsExistID(){
//        boolean IsExist = arrayIndex.contains(_id);
//        return IsExist;
//    }
//    public void setInsertMode(){
//        etID.setText("");
//        etName.setText("");
//        etPhone.setText("");
//        etEmail.setText("");
////        join_btn.setEnabled(true);
////        btn_Update.setEnabled(false);
//    }
//    public void hide_keyboard(View view){
////        InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
////        imm.hideSoftInputFromWindow(etEmail.getWindowToken(), 0);
//    }
//
//    public void button_go_register_apply(View v){
//        String email = this.etEmail.getText().toString();
//        String pw1 = this.etPw.getText().toString();
//        String pw2 = this.etPwCheck.getText().toString();
//
//        String emailPattern = "^[a-zA-Z0-9]+@[a-zA-Z0-9.]+$"; // 이메일 형식 패턴
//        if(!Pattern.matches(emailPattern , email)){
//            Toast.makeText(JoinActivity.this , "이메일 형식을 확인해수제요" , Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        StringBuffer strbuP1 = new StringBuffer(pw1);
//        StringBuffer strbuP2 = new StringBuffer(pw2);
//
//        if(strbuP1.length() < 8 || strbuP2 .length() < 8){ // 최소 비밀번호 사이즈를 맞추기 위해서
//            Toast.makeText(this, "비밀번호는 최소 8자리 이상입니다", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if(pw1.equals("") || pw2.equals("")){
//            Toast.makeText(JoinActivity.this , "비밀번호를 입력해주세요" , Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        if(pw1.equals(pw2)){
//
//            mAuth.createUserWithEmailAndPassword(email, pw1)
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                Toast.makeText(JoinActivity.this, "이메일 등록에 성공했습니다", Toast.LENGTH_SHORT).show();
//                                FirebaseUser user = mAuth.getCurrentUser();
//                                Toast.makeText(JoinActivity.this, "당신의 아이디는" + user.getEmail() + "입니다", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(JoinActivity.this, "이메일 등록에 실패앴습니다", Toast.LENGTH_SHORT).show();
//                            }
//                            //hide_keyboard(view);
//                        }
//                    });
//        }else{
//            Toast.makeText(this, "비밀번호가 서로 다릅니다 확인해주세요", Toast.LENGTH_SHORT).show();
//        }
//    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.join_imgBack:
                Intent intent = new Intent(this, LoginForm.class);
                startActivity(intent);
                break;
            case R.id.join_btnOk:
                if (!etEmail.getText().toString().equals("") && !etPw.getText().toString().equals("") && !etPwCheck.getText().toString().equals("")) {
                    // 이메일과 비밀번호가 공백이 아닌 경우
                    signUp();
                } else {
                    // 이메일과 비밀번호가 공백인 경우
                    Toast.makeText(JoinActivity.this, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }
                break;


            case R.id.btnDoubleCheck:
                _id = etID.getText().toString();
                name = etName.getText().toString();
                phone = etPhone.getText().toString();
                email = etEmail.getText().toString();

//                if(!IsExistID()){
//                    postFirebaseDatabase(true);
//                    getFirebaseDatabase();
//                    setInsertMode();
//                }else{
//                    Toast.makeText(JoinActivity.this, "이미 존재하는 ID 입니다. 다른 ID로 설정해주세요.", Toast.LENGTH_LONG).show();
//                }
                etID.requestFocus();
                etID.setCursorVisible(true);
                break;
        }
    }
}
