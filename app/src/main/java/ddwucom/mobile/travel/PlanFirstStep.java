package ddwucom.mobile.travel;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PlanFirstStep extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_1);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.y_alone:
                //Intent intent = new Intent(this, );
                //startActivity(intent);
                break;
            case R.id.y_together:
                //Intent intent = new Intent(this, );
                //startActivity(intent);
                break;
        }
    }
}
