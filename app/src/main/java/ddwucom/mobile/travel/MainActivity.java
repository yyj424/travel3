package ddwucom.mobile.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(this, LoadingActivity.class);
//        startActivity(intent);


        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, LoginForm.class);
                startActivity(intent1);
            }
        });

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main:
                Toast.makeText(this,"This is main", Toast.LENGTH_LONG).show();
        }
    }
}