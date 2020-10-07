package ddwucom.mobile.travel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FoundIDActivity extends AppCompatActivity {
    TextView reMessage, msg_email;

    private static final String TAG = "sera";
    static String checkEmail;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_id);

        reMessage = findViewById(R.id.reMessage);
        msg_email = findViewById(R.id.msg_email);

        checkEmail = getIntent().getStringExtra("emailForCheck");
        msg_email.setText(checkEmail);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.foundID_imgBack:
                finish();
                Intent intent = new Intent(this, LoginForm.class);
                startActivity(intent);
                break;
            case R.id.reMessage:
                //final String email = etEmail.getText().toString();
                dbRef = database.getReference("user_list");

                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "CLick22!!");
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Log.d(TAG, "ValueEventListener : " + snapshot.child("email").getValue());
                            if (checkEmail.equals(snapshot.child("email").getValue().toString())) {
                                firebaseAuth.sendPasswordResetEmail(checkEmail)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(FoundIDActivity.this, "메일을 보냈습니다.\n메일을 통해 비밀번호를 재설정해주세요.", Toast.LENGTH_SHORT).show();

                                                } else {
                                                    Toast.makeText(FoundIDActivity.this, "재전송 실패", Toast.LENGTH_SHORT).show();

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
        }
    }
}