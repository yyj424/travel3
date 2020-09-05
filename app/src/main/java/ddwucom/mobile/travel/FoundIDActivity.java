package ddwucom.mobile.travel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FoundIDActivity extends AppCompatActivity {
    TextView hiddenID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_id);
    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.foundID_imgBack:
                Intent intent = new Intent(this, LoginForm.class);
                startActivity(intent);
                break;
        }
    }
}
