package ddwucom.mobile.travel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JoinActivity extends AppCompatActivity {

    private DatabaseReference mPostReference;

    EditText etName, etID, etEmail, etPhone;

    String _id, name, email, phone;
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
    }
    public void postFirebaseDatabase(boolean add){
        mPostReference = FirebaseDatabase.getInstance().getReference();

        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;

        if(add){
            FirebasePost post = new FirebasePost(_id, name, phone, email);
            postValues = post.toMap();
        }
        childUpdates.put("/id_list/" + _id, postValues);
        mPostReference.updateChildren(childUpdates);
    }
    public void getFirebaseDatabase(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("getFirebaseDatabase", "key: " + dataSnapshot.getChildrenCount());

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    FirebasePost get = postSnapshot.getValue(FirebasePost.class);
                    String[] info = {get._id, get.name, get.phone, get.email};
                    String Result = setTextLength(info[0],10) + setTextLength(info[1],10) + setTextLength(info[2],10) + setTextLength(info[3],10);

                    arrayIndex.add(key);

                    Log.d("getFirebaseDatabase", "key: " + key);
                    Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2] + info[3]);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("getFirebaseDatabase","loadPost:onCancelled", databaseError.toException());
            }
        };
        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("id_list").orderByChild(sort);
        sortbyAge.addListenerForSingleValueEvent(postListener);
    }
    public String setTextLength(String text, int length){
        if(text.length()<length){
            int gap = length - text.length();
            for (int i=0; i<gap; i++){
                text = text + " ";
            }
        }
        return text;
    }
    public boolean IsExistID(){
        boolean IsExist = arrayIndex.contains(_id);
        return IsExist;
    }
    public void setInsertMode(){
        etID.setText("");
        etName.setText("");
        etPhone.setText("");
        etEmail.setText("");
//        join_btn.setEnabled(true);
//        btn_Update.setEnabled(false);
    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.join_imgBack:
                Intent intent = new Intent(this, LoginForm.class);
                startActivity(intent);
                break;
            case R.id.join_btnOk:
                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                builder.setTitle("회원가입 완료")
                        .setMessage("회원가입이 완료되었습니다.")
                        .setPositiveButton("확인", null)
                       .show();
                break;
            case R.id.btnDoubleCheck:
                _id = etID.getText().toString();
                name = etName.getText().toString();
                phone = etPhone.getText().toString();
                email = etEmail.getText().toString();

                if(!IsExistID()){
                    postFirebaseDatabase(true);
                    getFirebaseDatabase();
                    setInsertMode();
                }else{
                    Toast.makeText(JoinActivity.this, "이미 존재하는 ID 입니다. 다른 ID로 설정해주세요.", Toast.LENGTH_LONG).show();
                }
                etID.requestFocus();
                etID.setCursorVisible(true);
                break;
        }
    }
}
