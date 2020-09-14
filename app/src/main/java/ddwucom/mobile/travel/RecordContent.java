package ddwucom.mobile.travel;

import java.util.List;

public class RecordContent {
    int _id;
    String location;
    List<String> imageResIds;
    String content;

    public RecordContent() {
        this.location = null;
        this.imageResIds = null;
        this.content = null;
    }

    public RecordContent(String location, List<String> imageResIds, String content) {
        this.location = location;
        this.imageResIds = imageResIds;
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getImageResIds() {
        return imageResIds;
    }

    public void setImageResIds(List<String> imageResIds) {
        this.imageResIds = imageResIds;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
