package ddwucom.mobile.travel;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class GroupListActivity extends Activity {
    private static final String TAG = "GroupListActivity";
    ImageView btnHome, btnGroup, btnCourse, btnMap;
    final int ADD_MEMBER_CODE = 100;
    private ListView lvGroup;
    private PlanAdapter planAdapter;
    private ArrayList<Group> groupList;
    private ArrayList<Group> sGroupList;

    LinearLayout addGroupLayout;
    EditText etSearchGroup;
    EditText etGroupStart;
    EditText etGroupEnd;
    EditText etGroupName;
    TextView tvMemberCnt;
    Calendar calendar;
    String dateFormat;
    SimpleDateFormat sdf;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference dbRefUser;
    FirebaseUser user;
    AlertDialog alertDialog;
    String currentUid;
    String currentNickname;
    String groupName;
    String startDate;
    String endDate;
    String selectedDate;
    ArrayList<String> addMembers;

    DatePickerDialog.OnDateSetListener recordDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            switch (selectedDate) {
                case "start":
                    etGroupStart.setText(sdf.format(calendar.getTime()));
                    break;
                case "end":
                    etGroupEnd.setText(sdf.format(calendar.getTime()));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        btnHome = findViewById(R.id.btn_home);
        btnGroup = findViewById(R.id.btn_friends);
        btnCourse = findViewById(R.id.btn_course);
        btnMap = findViewById(R.id.btn_map);

        addGroupLayout = (LinearLayout) View.inflate(this, R.layout.add_group_layout, null);

        groupList = new ArrayList<>();
        sGroupList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Groups");

        getUserInfo();

        etSearchGroup = findViewById(R.id.etSearchGroup);
        lvGroup = findViewById(R.id.lvGroup);

        planAdapter = new PlanAdapter(GroupListActivity.this, R.layout.planlist_adapter_view, true, sGroupList);
        lvGroup.setAdapter(planAdapter);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("goeun", snapshot.getValue().toString());
                ArrayList<String> members = (ArrayList<String>) snapshot.child("members").getValue();
                String key = snapshot.getKey();
                Log.d("goeun", key);
                if (members.contains(currentUid)) {
                    Group p = new Group();
                    groupName = snapshot.child("groupName").getValue().toString();
                    p.setGroupName(groupName);
                    startDate = snapshot.child("startDate").getValue().toString();
                    p.setStartDate(startDate);
                    endDate = snapshot.child("endDate").getValue().toString();
                    p.setEndDate(endDate);
                    p.setGid(key);
                    groupList.add(p);
                    planAdapter.notifyDataSetChanged();
                }
                if (groupList.size() > 0) {
                    sGroupList.clear();
                    sGroupList.addAll(groupList);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lvGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GroupListActivity.this, GroupMainActivity.class);
                Log.d(TAG, sGroupList.get(position).getStartDate());
                intent.putExtra("selectedGroup", sGroupList.get(position));
                intent.putExtra("currentUid", currentUid);
                intent.putExtra("currentNickname", currentNickname);
                Log.d(TAG, "2" +currentNickname);
                startActivity(intent);
            }
        });

        etSearchGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null) {search(s.toString());}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        calendar = Calendar.getInstance();
        dateFormat = "yyyy-MM-dd";
        sdf = new SimpleDateFormat(dateFormat, Locale.KOREA);
    }

    public void getUserInfo() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUid = user.getUid();
        }

        dbRefUser = firebaseDatabase.getReference("user_list");
        dbRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    UserInfo u = s.getValue(UserInfo.class);
                    Log.d(TAG, u.getUid() + " " + currentUid);
                    if (u.getUid().equals(currentUid)) {
                        currentNickname = u.getNickname();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGroupAdd:
                TextView dialogTitle = new TextView(this);
                dialogTitle.setText("그룹 추가하기");
                dialogTitle.setIncludeFontPadding(false);
                dialogTitle.setTypeface(ResourcesCompat.getFont(this, R.font.tmoney_regular));
                dialogTitle.setGravity(Gravity.CENTER);
                dialogTitle.setPadding(10, 70, 10, 70);
                dialogTitle.setTextSize(20F);
                dialogTitle.setBackgroundResource(R.color.colorTop);
                dialogTitle.setTextColor(Color.DKGRAY);

                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
                builder.setCustomTitle(dialogTitle)
                        .setView(addGroupLayout)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                etGroupName = addGroupLayout.findViewById(R.id.etGroupName);
                                String groupName = etGroupName.getText().toString();
                                String startDate = etGroupStart.getText().toString();
                                String endDate = etGroupEnd.getText().toString();
                                addMembers.add(currentUid);

                                if (groupName.matches("") || startDate.matches("") || endDate.matches("")) {
                                    Toast.makeText(GroupListActivity.this, "필수 항목을 입력하세요!", Toast.LENGTH_LONG).show();
                                    return;
                                } else {
                                    Group group = new Group(addMembers, groupName, startDate, endDate);
                                    String addKey = databaseReference.push().getKey();
                                    databaseReference.child(addKey).setValue(group);
                                    firebaseDatabase.getReference("group_album").child(addKey).child("기본").child("0").setValue("이미지 없음");
                                    group.setGid(addKey);
                                    Intent intent = new Intent(GroupListActivity.this, GroupMainActivity.class);
                                    intent.putExtra("selectedGroup", group);
                                    intent.putExtra("currentUid", currentUid);
                                    intent.putExtra("currentNickname", currentNickname);
                                    Log.d(TAG, "1"+currentNickname);
                                    startActivity(intent);
                                }
                            }
                        })
                        .setNegativeButton("CANCEL", null);
                alertDialog = builder.create();
                if (addGroupLayout.getParent() != null) {
                    ((ViewGroup) addGroupLayout.getParent()).removeView(addGroupLayout);
                }
                alertDialog.show();
                alertDialog.getWindow().setLayout(1200, 1100);

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                etGroupStart = alertDialog.findViewById(R.id.etGroupStart);
                etGroupEnd = alertDialog.findViewById(R.id.etGroupEnd);
                tvMemberCnt = alertDialog.findViewById(R.id.tvMemberCnt);

                imm.hideSoftInputFromWindow(etGroupStart.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(etGroupEnd.getWindowToken(), 0);

                etGroupStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(GroupListActivity.this, R.style.DialogTheme, recordDatePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                        selectedDate = "start";
                    }
                });

                etGroupEnd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(GroupListActivity.this, R.style.DialogTheme, recordDatePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                        selectedDate = "end";
                    }
                });

                TextView textView = alertDialog.findViewById(android.R.id.message);
                Typeface face = Typeface.createFromAsset(getAssets(), "fonts/tmoney_regular.ttf");
                textView.setTypeface(face);
                break;
            case R.id.btnAddMember:
                Intent intent = new Intent(this, SearchFriends.class);
                startActivityForResult(intent, ADD_MEMBER_CODE);
                break;
            case R.id.btn_home:
                Intent home = new Intent(GroupListActivity.this, HomeActivity.class);
                startActivity(home);
                break;
            case R.id.btn_friends:
                btnHome.setImageResource(R.drawable.home_icon_grey);
                btnGroup.setImageResource(R.drawable.friends_icon_yellow);
                btnCourse.setImageResource(R.drawable.course_icon_grey);
                btnMap.setImageResource(R.drawable.map_icon_grey);
                Intent list = new Intent(GroupListActivity.this, GroupListActivity.class);
                startActivity(list);
                break;
            case R.id.btn_map:
                Intent map = new Intent(GroupListActivity.this, OnlyMap.class);
                startActivity(map);
                break;
        }
    }

    public void search(String charText) {
        sGroupList.clear();

        if (charText.length() == 0) {
            sGroupList.addAll(groupList);
        }
        else
        {
            for(int i = 0; i < groupList.size(); i++)
            {
                if (groupList.get(i).getGroupName().toLowerCase().contains(charText))
                {
                    sGroupList.add(groupList.get(i));
                }
            }
        }
        planAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_MEMBER_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    addMembers = data.getStringArrayListExtra("members");
                    Log.d(TAG, String.valueOf(addMembers.size()));
                    int memberCnt = addMembers.size();
                    tvMemberCnt.setText("본인 외 " + memberCnt + "명의 멤버가 추가되었습니다.");
                    break;
                case RESULT_CANCELED:
                    break;
            }
        }
    }
}
