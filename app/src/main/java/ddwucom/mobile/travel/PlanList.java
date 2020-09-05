package ddwucom.mobile.travel;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    private ArrayList<MyPlan> PlanList = null;
    private ArrayList<MyPlan> myPlanList = null;
    EditText search;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_list);

        PlanList = new ArrayList();

        //PlanList.add( new MyPlan(1, "일산 여행", "2020.09.05", "2020.09.05"));
        myPlanList.addAll(PlanList);

        planAdapter = new PlanAdapter(this, R.layout.planlist_adapter_view, PlanList);

        listView = (ListView)findViewById(R.id.y_planList);
        search = findViewById(R.id.y_searchList);

        listView.setAdapter(planAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent intent = new Intent(this, );
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String searchPlan = search.getText().toString();
                search(searchPlan);
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

    public void search(String charText) {
        PlanList.clear();

        if (charText.length() == 0) {
            PlanList.addAll(myPlanList);
        }
        else
        {
            for(int i = 0;i < myPlanList.size(); i++)
            {
                if (myPlanList.get(i).getPlanName().toLowerCase().contains(charText))
                {
                    myPlanList.add(myPlanList.get(i));
                }
            }
        }
        planAdapter.notifyDataSetChanged();
    }
}
