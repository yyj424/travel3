package ddwucom.mobile.travel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class PlanSecondStep extends AppCompatActivity {
    int pos;
    String string;
    Spinner country_spinner;
    Spinner widePlace_spinner;
    Spinner narrowPlace_spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_2);

        country_spinner = findViewById(R.id.country_spinner);
        widePlace_spinner = findViewById(R.id.widePlace_spinner);
        narrowPlace_spinner = findViewById(R.id.narrowPlace_spinner);
        List<String> country = Arrays.asList(getResources().getStringArray(R.array.country_array));
        List<String> widePlace = Arrays.asList(getResources().getStringArray(R.array.widePlace_array));
        List<String> narrowPlace = Arrays.asList(getResources().getStringArray(R.array.narrowPlace_array));

        spinnerClick(country_spinner, country);
        spinnerClick(widePlace_spinner, widePlace);
        spinnerClick(narrowPlace_spinner, narrowPlace);
    }

    public void spinnerClick(Spinner spinner, List list){
        SpinnerAdapter adapter = new SpinnerAdapter(PlanSecondStep.this, list);
        spinner.setAdapter(adapter);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.y_nextStep:
                if (country_spinner.getSelectedItemPosition() == 0 || widePlace_spinner.getSelectedItemPosition() == 0 || narrowPlace_spinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(PlanSecondStep.this, "여행지를 선택하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(PlanSecondStep.this, MapActivity.class);
                startActivity(intent);
                break;
        }
    }
}
