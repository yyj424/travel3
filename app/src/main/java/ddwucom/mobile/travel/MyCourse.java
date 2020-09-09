package ddwucom.mobile.travel;

public class MyCourse {
    private long _id;
    private String PlaceName;

    public MyCourse(long _id, String PlaceName)
    {
        this._id = _id;
        this.PlaceName = PlaceName;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public void setPlaceName(String placeName) {
        PlaceName = placeName;
    }
}
