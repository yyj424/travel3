package ddwucom.mobile.travel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PlanList extends AppCompatActivity {

    private ListView listView;
    private PlanAdapter planAdapter;
    private ArrayList<MyPlan> myPlanList = null;
    EditText search;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_list);

        listView = (ListView)findViewById(R.id.y_planList);
        myPlanList = new ArrayList();
        planAdapter = new PlanAdapter(this, R.layout.planlist_adapter_view, myPlanList);
        search = findViewById(R.id.y_searchList);
        String planName = search.getText().toString();

        listView.setAdapter(planAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyPlan plan = myPlanList.get(position);
               // Intent intent = new Intent(this,);
            }
        });

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.y_addPlan:
                break;
            case R.id.y_removePlan:
                break;
        }
    }
}
