package ddwucom.mobile.travel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecordMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private FirebaseUser user;
    private String currentUid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_main);

        // DB
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("records");
        //database.getReference("records").child(recordKey).child("contents").push();

        // currentUser
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUid = user.getUid();
        }
//        else {
//            Toast.makeText(this, "로그인이 필요합니다!", Toast.LENGTH_SHORT).show();
//        }

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_btn);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_record_calendar, menu);
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddRecord:
                Intent intent = new Intent(this, RecordDayActivity.class);
                //String recordKey = dbRef.child("records").push();

               // intent.putExtra("recordKey", recordKey);
                intent.putExtra("currentUid", currentUid);
                intent.putExtra("isNew", true);
                startActivity(intent);
                break;
        }
    }
}
