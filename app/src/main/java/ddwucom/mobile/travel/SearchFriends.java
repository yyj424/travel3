package ddwucom.mobile.travel;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

    ArrayAdapter<String> adapter;
    ArrayList<String> nicknameList;

    ListView lvFriends;
    Button btn_addRoom;
    EditText etSearchFriends;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_friends);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
//        nicknameList = new ArrayList<>();
        //nicknameList = getNicknameArray();
        Log.d("sera", nicknameList.toString());

        btn_addRoom = findViewById(R.id.btn_make_room);
        etSearchFriends = findViewById(R.id.et_search);

        dbRef  = database.getReference("user_list");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nicknameList = new ArrayList<>();
                Log.d("sera", "CLick22!!");
                //String etNickname = etID.getText().toString();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("sera", "ValueEventListener : " + snapshot.child("nickname").getValue());
                    //if(etNickname.equals(snapshot.child("nickname").getValue().toString())) {
                    // Toast.makeText(JoinActivity.this, "중복된 아이디 입니다. 다시입력하세요 ", Toast.LENGTH_SHORT).show();
                    nicknameList.add(snapshot.child("nickname").getValue().toString());
                }

                adapter = new ArrayAdapter<String>(SearchFriends.this, android.R.layout.simple_list_item_multiple_choice, nicknameList);
                lvFriends = findViewById(R.id.lv_friends);
                lvFriends.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                lvFriends.setAdapter(adapter);
                Log.d("sera", "for문 : " + nicknameList.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //데이터 가져오기






    }
    public ArrayList getNicknameArray(){
        final ArrayList<String> arr = new ArrayList<>();
//        nicknameList = new ArrayList<>();

        return arr;
    }
}
