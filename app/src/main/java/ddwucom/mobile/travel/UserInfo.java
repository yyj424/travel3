package ddwucom.mobile.travel;

public class UserInfo {
    String uid;
    String nickname;
    String phone;
    String email;

    public UserInfo() {
        this.uid = null;
        this.nickname = null;
        this.phone = null;
        this.email = null;
    }

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
