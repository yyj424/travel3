package ddwucom.mobile.travel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class FindPwActivity extends AppCompatActivity {
    EditText etEmail;
    TextView checkEmail;

    private static final String TAG = "sera";
    final int _CHECK = 1004;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        etEmail = findViewById(R.id.findPW_etEmail);
        checkEmail = findViewById(R.id.findPW_chEmail); //>>이메일 확인 글자..

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

    }
    public void onClick (View v){ //화살표 눌렀을 때
        switch(v.getId()){
            case R.id.findPW_imgNext:
                final String email = etEmail.getText().toString();
                dbRef  = database.getReference("user_list");

                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "CLick22!!");
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Log.d(TAG, "ValueEventListener : " + snapshot.child("email").getValue());
                            if (email.equals(snapshot.child("email").getValue().toString())) {
                                Toast.makeText(FindPwActivity.this, "존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                                firebaseAuth.sendPasswordResetEmail(email)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    finish();

                                                    //다음 페이지에서 출력할 email intent에 넣어서 보내기.
                                                    Intent intent = new Intent(FindPwActivity.this, FoundIDActivity.class);
                                                    intent.putExtra("emailForCheck", email);
                                                    startActivityForResult(intent, _CHECK);
                                                }
                                                else{
                                                    Toast.makeText(FindPwActivity.this, "메일 보내기 실패\n 메일주소를 확인해 주세요!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
            case R.id.findPW_signUp:
                Intent intent1 = new Intent(this, JoinActivity.class);
                startActivity(intent1);
                break;
            case R.id.findPW_login:
                Intent intent2 = new Intent(this, LoginForm.class);
                startActivity(intent2);
                break;
            case R.id.findPW_etEmail:
                etEmail.setText("");
                break;
        }
    }
}
