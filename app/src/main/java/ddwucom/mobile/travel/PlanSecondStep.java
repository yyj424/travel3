package ddwucom.mobile.travel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PlanSecondStep extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_2);

        final Spinner country_spinner = findViewById(R.id.country_spinner);
        final Spinner widePlace_spinner = findViewById(R.id.widePlace_spinner);
        final Spinner narrowPlace_spinner = findViewById(R.id.narrowPlace_spinner);

        country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                final String country = parent.getSelectedItem().toString();//parent아니면 country_spinner로 바꾸기
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.y_nextStep:
                Intent intent = new Intent(PlanSecondStep.this, Map.class);
                startActivity(intent);
                break;
        }
    }
}
