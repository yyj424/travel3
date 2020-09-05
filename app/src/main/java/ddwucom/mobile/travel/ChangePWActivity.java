package ddwucom.mobile.travel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePWActivity extends AppCompatActivity {
    EditText etNewPw, etCheckPw;
    Button btnCheck;
    TextView warnMSG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        warnMSG.setVisibility(View.INVISIBLE);
        setContentView(R.layout.activity_change_pw);

        warnMSG = findViewById(R.id.warnMsg);

        etNewPw = findViewById(R.id.etNewPW);
        etCheckPw = findViewById(R.id.etNewPW_check);

        btnCheck = findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent;
                AlertDialog.Builder builder;
                if(etNewPw.getText().toString().equals(etCheckPw.getText().toString()))
                    intent = new Intent(ChangePWActivity.this, LoginForm.class);
                else {
                    warnMSG.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    public void onClick(View v){
        warnMSG.setVisibility(View.INVISIBLE);
        switch (v.getId()){
            case R.id.etNewPW:
                break;
            case R.id.etNewPW_check:
                break;
        }
    }
}
