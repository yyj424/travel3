package ddwucom.mobile.travel;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFriends extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    ArrayList<String> nicknameList;
    ArrayList<String> arr;

    MyFriendsAdapter myAdapter;
    ListView lvFriends;
    Button btn_addRoom;
    EditText etSearchFriends;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_friends);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();

        nicknameList = new ArrayList<>();
        nicknameList.add("sera");
        nicknameList.add("yyj");
        nicknameList.add("hiereit");
        nicknameList.add("travel3Good");
        //String[] nameList = getResources().getStringArray(R.array.nameList);
        btn_addRoom = findViewById(R.id.btn_make_room);
        etSearchFriends = findViewById(R.id.et_search);
        //lvFriends.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //Log.d("sera", "out of for-each" + arr.toString());

//        myAdapter = new MyFriendsAdapter(SearchFriends.this, R.layout.search_friends_adpter, arr);
//        //myAdapter = new MyFriendsAdapter(SearchFriends.this, R.layout.search_friends_adpter, nameList);
//        lvFriends = findViewById(R.id.lv_friends);
//        lvFriends.setAdapter(myAdapter);
//        Query query = dbRef.child("user_list").child("nickname");
//        query.



    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_search:
                dbRef = database.getReference("user_list");
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        arr = new ArrayList<>();
                        Log.d("sera", "CLick22!!");
                        //String etNickname = etID.getText().toString();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Log.d("sera", "ValueEventListener : " + snapshot.child("nickname").getValue());
                            if (etSearchFriends.equals(snapshot.child("nickname").getValue().toString())) {
                                arr.add(snapshot.child("nickname").getValue().toString());
                                Log.d("sera", arr.toString());
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                myAdapter = new MyFriendsAdapter(SearchFriends.this, R.layout.search_friends_adpter, arr);
                //myAdapter = new MyFriendsAdapter(SearchFriends.this, R.layout.search_friends_adpter, nameList);
                lvFriends = findViewById(R.id.lv_friends);
                lvFriends.setAdapter(myAdapter);
                break;
        }

    }
}
