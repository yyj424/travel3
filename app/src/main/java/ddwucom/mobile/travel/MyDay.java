package ddwucom.mobile.travel;

public class MyDay {
    int day;
    String place;
    String memo;

    public MyDay(String place, String memo){
        this.place = place;
        this.memo = memo;
    }

    public MyDay(int day, String place, String memo) {
        this.day = day;
        this.place = place;
        this.memo = memo;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
