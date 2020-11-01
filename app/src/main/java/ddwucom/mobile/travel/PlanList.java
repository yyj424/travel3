package ddwucom.mobile.travel;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PlanList extends AppCompatActivity {

    private ListView listView;
    private PlanAdapter planAdapter;
    private ArrayList<MyPlan> PlanList = null;
    private ArrayList<MyPlan> sPlanList = null;
    SearchView searchView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String uid;
    String planName;
    String start;
    String end;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_list);

        PlanList = new ArrayList<MyPlan>();
        sPlanList = new ArrayList<MyPlan>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        searchView = findViewById(R.id.y_searchView);
        listView = findViewById(R.id.y_planList);

        databaseReference = firebaseDatabase.getReference("plan_list");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    if (s.child("uid").getValue().toString().equals(uid)) {
                        planName = s.child("planName").getValue().toString();
                        start = s.child("startDate").getValue().toString();
                        end = s.child("endDate").getValue().toString();
                        PlanList.add(new MyPlan(planName, start, end));
                    }
                }
                sPlanList.addAll(PlanList);
                planAdapter = new PlanAdapter(PlanList.this, R.layout.planlist_adapter_view, sPlanList);
                listView.setAdapter(planAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PlanList.this, PlanLastStep.class);
                intent.putExtra("pname", planName);
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null) {search(newText);}
                return false;
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.y_addPlan:
                Intent intent = new Intent(PlanList.this, PlanFirstStep.class);
                startActivity(intent);
                finish();
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
