package ddwucom.mobile.travel;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FirebasePost {
    public String _id;
    public String name;
    public String phone;
    public String email;

    public FirebasePost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }
    public FirebasePost(String _id, String name, String phone, String email) {
        this._id = _id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("_id", _id);
        result.put("name", name);
        result.put("phone", phone);
        result.put("email", email);
        return result;
    }
}
