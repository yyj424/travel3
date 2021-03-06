package ddwucom.mobile.travel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FindIdActivity extends AppCompatActivity {
    EditText etNick;
    ImageView btnFindID;
    TextView checkNick;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    final String TAG = "sera";
    static String email;
    static boolean checkID = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        etNick = findViewById(R.id.findID_etNick);
        btnFindID = findViewById(R.id.findID_imgNext);
        checkNick = findViewById(R.id.checkNick);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        //이메일 체크 클릭 리스너
        checkNick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dbRef  = database.getReference("user_list");
                Log.d(TAG, "CLick11!!");

                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "CLick22!!");
                        int is_in = 0;
                        final String nickname = etNick.getText().toString();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Log.d(TAG, "CLick!!");
                            Log.d(TAG, "ValueEventListener : " + snapshot.child("email").getValue());
                            if(nickname.equals(snapshot.child("nickname").getValue().toString())) {
                                Log.d(TAG, snapshot.child("email").getValue().toString());
                                is_in = 1;
                                email = snapshot.child("email").getValue().toString();
                                Log.d(TAG, email);
                                break;
                            }
                        }
                        if(is_in != 1){
//                            Toast.makeText(FindIdActivity.this, "해당 닉네임이 없습니다. 다시입력하세요 ", Toast.LENGTH_SHORT).show();
                        }
                        else{

                            Toast.makeText(FindIdActivity.this, "존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                            checkID = true;

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
    public void onClick(View v){
        switch(v.getId()) {
            case R.id.findID_signUp:
                Intent intent1 = new Intent(this, JoinActivity.class);
                startActivity(intent1);
                break;
            case R.id.findID_Login:
                Intent intent2 = new Intent(this, LoginForm.class);
                startActivity(intent2);
                break;
            case R.id.findID_imgNext:
                if (!checkID) {
                    Toast.makeText(FindIdActivity.this, "닉네임 존재 여부를 확인해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(FindIdActivity.this, "아이디는 " + email + "입니다", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getApplicationContext(),  LoginForm.class));
                break;
            case R.id.findID_etNick:
                etNick.setText("");
                break;
        }
    }
}
