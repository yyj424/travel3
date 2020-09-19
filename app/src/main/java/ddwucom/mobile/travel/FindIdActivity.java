package ddwucom.mobile.travel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindIdActivity extends AppCompatActivity {
    EditText etEmail;
    ImageView btnFindID;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        etEmail = (EditText)findViewById(R.id.findID_etEmail);
        btnFindID = (ImageView)findViewById(R.id.findID_imgNext);

        firebaseAuth = FirebaseAuth.getInstance();

       btnFindID.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               String email = etEmail.getText().toString();
               firebaseAuth.sendPasswordResetEmail(email)
                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if(task.isSuccessful()){
                                   Toast.makeText(FindIdActivity.this, "이메일을 보냈습니다.",Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent intent = new Intent(FindIdActivity.this, LoginForm.class);
                                    startActivity(intent);
                               }
                               else{
                                   Toast.makeText(FindIdActivity.this, "메일 보내기 실패\n 메일을 확인해 주세요!", Toast.LENGTH_SHORT).show();
                               }
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
        }
    }
}
