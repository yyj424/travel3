package ddwucom.mobile.travel;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PlanList extends AppCompatActivity {

    private ListView listView;
    private PlanAdapter planAdapter;
    private ArrayList<MyPlan> PlanList = null;
    private ArrayList<MyPlan> sPlanList = null;
    SearchView searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_list);

        PlanList = new ArrayList<MyPlan>();
        sPlanList = new ArrayList<MyPlan>();

        PlanList.add( new MyPlan(1, "일산 여행", "2020.09.05", "2020.09.05"));
        PlanList.add( new MyPlan(2, "동덕여대", "2020.09.10", "2020.09.11"));
        sPlanList.addAll(PlanList);
        planAdapter = new PlanAdapter(this, R.layout.planlist_adapter_view, sPlanList);

        listView = (ListView)findViewById(R.id.y_planList);
        searchView = findViewById(R.id.y_searchView);

        listView.setAdapter(planAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent intent = new Intent(this, );
                //startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.y_addPlan:
                //Intent intent = new Intent(this, );
                //startActivity(intent);
                break;
            case R.id.y_removePlan:
                
                break;
        }
    }

    public void search(String charText) {
        sPlanList.clear();

        if (charText.length() == 0) {
            sPlanList.addAll(PlanList);
        }
        else
        {
            for(int i = 0; i < PlanList.size(); i++)
            {
                if (PlanList.get(i).getPlanName().toLowerCase().contains(charText))
                {
                    sPlanList.add(PlanList.get(i));
                }
            }
        }
        planAdapter.notifyDataSetChanged();
    }
}
