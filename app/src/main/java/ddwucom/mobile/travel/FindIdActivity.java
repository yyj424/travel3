package ddwucom.mobile.travel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FindIdActivity extends AppCompatActivity {
    EditText etEmail;
    ImageView btnFindID;
    TextView checkEmail;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    final String TAG = "sera";
    static String nickname;
    static boolean checkID = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        etEmail = (EditText)findViewById(R.id.findID_etEmail);
        btnFindID = (ImageView)findViewById(R.id.findID_imgNext);
        checkEmail = (TextView)findViewById(R.id.checkEmail);

        firebaseAuth = FirebaseAuth.getInstance();

        //이메일 체크 클릭 리스너
        checkEmail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dbRef  = database.getReference("user_list");
                Log.d(TAG, "CLick11!!");

                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "CLick22!!");
                        int is_in = 0;
                        String etNickname = etEmail.getText().toString();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            //Log.d(TAG, "CLick!!");
                            Log.d(TAG, "ValueEventListener : " + snapshot.child("email").getValue());
                            if(etNickname.equals(snapshot.child("email").getValue().toString())) {
                                is_in = 1;
                                nickname = snapshot.child("nickname").getValue().toString();
                                break;
                            }
                        }
                        if(is_in != 1){
                            Toast.makeText(FindIdActivity.this, "해당 이메일이 없습니다. 다시입력하세요 ", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(FindIdActivity.this, "존재하는 이메일입니다.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(FindIdActivity.this, "아이디는 " + nickname + "입니다", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getApplicationContext(),  LoginForm.class));
                break;


        }
    }
}
