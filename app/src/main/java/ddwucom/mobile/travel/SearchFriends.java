package ddwucom.mobile.travel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFriends extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference dbUser;
    private FirebaseUser user;
    private String currentUid;

    ArrayList<UserInfo> memberList;
    ArrayList<String> members;
    MyFriendsAdapter myAdapter;
    ListView lvFriends;
    Button btn_addRoom;
    ImageButton btnAddMember;
    EditText etSearchFriends;
    TextView tvSearchResult;
    ConstraintLayout clSearchResult;
    UserInfo searchUser;

    int selectedPos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);

        database = FirebaseDatabase.getInstance();
        dbUser = database.getReference("user_list");

        user = FirebaseAuth.getInstance().getCurrentUser();
        currentUid = user.getUid();

        members = new ArrayList<>();
        memberList = new ArrayList<>();
        btn_addRoom = findViewById(R.id.btnComAddFriends);
        etSearchFriends = findViewById(R.id.et_search);
        tvSearchResult = findViewById(R.id.tvSearchResult);
        clSearchResult = findViewById(R.id.clSearchResult);
        btnAddMember = findViewById(R.id.btnAddMember);

        myAdapter = new MyFriendsAdapter(this, R.layout.search_friends_adpter, memberList);
        lvFriends = findViewById(R.id.lv_friends);
        lvFriends.setAdapter(myAdapter);

        etSearchFriends.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String searchFriends = etSearchFriends.getText().toString();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            searchUser = snapshot.getValue(UserInfo.class);
                            if (searchUser.getUid().equals(currentUid)) {
                                tvSearchResult.setText("자신은 추가할 수 없습니다!");
                            }
                            Log.d("goeun", "s " + searchFriends + "r " + searchUser.getNickname());
                            if (searchFriends.equals(searchUser.getNickname())) {
                                Log.d("goeun", "true");
                                btnAddMember.setVisibility(View.VISIBLE);
                                Log.d("goeun", String.valueOf(clSearchResult.getVisibility()));
                                tvSearchResult.setText(searchUser.getNickname());
                                break;
                            }
                            else {
                                btnAddMember.setVisibility(View.INVISIBLE);
                                tvSearchResult.setText("검색 결과가 없습니다.");
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

        lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPos = position;
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddMember:
                if (!isInList()) {
                    memberList.add(searchUser);
                    members.add(searchUser.getUid());
                    btnAddMember.setVisibility(View.INVISIBLE);
                    tvSearchResult.setText("");
                    myAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(this, "이미 추가된 친구입니다!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnDeleteMember:
                memberList.remove(selectedPos);
                members.remove(selectedPos);
                myAdapter.notifyDataSetChanged();
                break;
            case R.id.btnComAddFriends:
                Intent resultIntent = new Intent();
                resultIntent.putStringArrayListExtra("members", members);
                setResult(RESULT_OK, resultIntent);
                finish();
                break;
        }
    }

    public boolean isInList() {
        for (UserInfo u:memberList) {
            if (u.getNickname().equals(searchUser.getNickname())) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }
}
