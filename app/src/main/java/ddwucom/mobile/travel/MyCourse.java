package ddwucom.mobile.travel;

import java.io.Serializable;

public class MyCourse implements Serializable {
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
