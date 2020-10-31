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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFriends extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    ArrayList<String> nicknameList;
   static ArrayList<String> arr;

    MyFriendsAdapter myAdapter;
    ListView lvFriends;
    Button btn_addRoom;
    Button btn_searchFriends;
    EditText etSearchFriends;

    static boolean isClicked = false;

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
        btn_searchFriends.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("sera", "버튼 리스너 Click!!1" );
                //Log.d("sera", "  "+  nicknameList.get(0).toString() + nicknameList.get(1).toString());
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //nicknameList = new ArrayList<>();
                        Log.d("sera", "CLick22!!");
                        //String etNickname = etID.getText().toString();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Log.d("sera", "ValueEventListener : " + snapshot.child("nickname").getValue());
                            if (etSearchFriends.equals(snapshot.child("nickname").getValue().toString())) {
                                nicknameList.add(snapshot.child("nickname").getValue().toString());
                                Log.d("sera", nicknameList.get(0).toString());
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("sera", "loadPost:onCancelled", error.toException());
                    }
                });


//                dbRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//
//                });
                Log.d("sera", "버튼 리스너 Click!!2" );
                myAdapter = new MyFriendsAdapter(SearchFriends.this, R.layout.search_friends_adpter, nicknameList);
                Log.d("sera", "버튼 리스너 Click!!3" );
                //myAdapter = new MyFriendsAdapter(SearchFriends.this, R.layout.search_friends_adpter, nameList);
                lvFriends = findViewById(R.id.lv_friends);
                Log.d("sera", "버튼 리스너 Click!!4" );
                lvFriends.setAdapter(myAdapter);
                Log.d("sera", "버튼 리스너 Click!!5" );

            }
        });
        Log.d("sera", "버튼 리스너 후.." );

    }
}
