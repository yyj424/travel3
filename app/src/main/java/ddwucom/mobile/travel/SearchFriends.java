package ddwucom.mobile.travel;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class SearchFriends extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    ArrayList<String> nicknameList;
    MyFriendsAdapter myAdapter;
    ListView lvFriends;

    Button btn_addRoom, btn_searchFriends;
    EditText etSearchFriends;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("sera", "setContentView");
        setContentView(R.layout.activity_search_friends);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();

        nicknameList = new ArrayList<>();
        Log.d("sera", "변수선언");
        btn_addRoom = findViewById(R.id.btn_make_room);
        btn_searchFriends = findViewById(R.id.btn_search);
        etSearchFriends = findViewById(R.id.et_search);
        //lvFriends.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        Log.d("sera", "위젯찾기");

        dbRef = database.getReference("user_list");
        Log.d("sera", "dbRef가져오기" + dbRef);

        Log.d("sera", "버튼 리스너 전.." );

        myAdapter = new MyFriendsAdapter(this, R.layout.search_friends_adpter, nicknameList);
        lvFriends = findViewById(R.id.lv_friends);
        lvFriends.setAdapter(myAdapter);

        etSearchFriends.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String searchFriends = etSearchFriends.getText().toString();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Map<String, Object> data = (Map) snapshot.getValue();
                            if (searchFriends.equals(data.get("nickname").toString())) {
                                nicknameList.add(data.get("nickname").toString());
                                myAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("sera", "loadPost:onCancelled", error.toException());
                    }
                });

                return true;
            }
        });

        btn_searchFriends.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("sera", "버튼 리스너 Click!!" );
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String searchFriends = etSearchFriends.getText().toString();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Map<String, Object> data = (Map) snapshot.getValue();
                            if (searchFriends.equals(data.get("nickname").toString())) {
                                nicknameList.add(data.get("nickname").toString());
                                myAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("sera", "loadPost:onCancelled", error.toException());
                    }
                });
            }
        });
        Log.d("sera", "버튼 리스너 후.." );

    }
}
