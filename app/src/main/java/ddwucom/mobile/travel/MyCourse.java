package ddwucom.mobile.travel;

import java.io.Serializable;

public class MyCourse implements Serializable {
    private long _id;
    private String PlaceName;
    private String memo;

    public MyCourse() {
    }

    public MyCourse(long _id, String placeName, String memo) {
        this._id = _id;
        this.PlaceName = placeName;
        this.memo = memo;
    }

    public long get_id() {
        return _id;
    }
    public String getPlaceName() {
        return PlaceName;
    }
    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }
}
