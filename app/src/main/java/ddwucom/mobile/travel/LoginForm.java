package ddwucom.mobile.travel;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginForm extends AppCompatActivity {
    EditText etName, etEmail, etPW;
    TextView findID, findPW;
    Button btnLogin, btnJoin, btnGoogleLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginform_main);

        etName = findViewById(R.id.etNameForm);
        etEmail = findViewById(R.id.etEmailForm);
        etPW = findViewById(R.id.etPwForm);

        findID = findViewById(R.id.findID);
        findPW = findViewById(R.id.findPW);

        btnLogin = findViewById(R.id.btnLogin);
        btnGoogleLogin = findViewById(R.id.btnLoginGoogle);
        btnJoin = findViewById(R.id.btnJoin);
    }
}
