package ddwucom.mobile.travel;

import java.util.ArrayList;
import java.util.List;

public class RecordContent {
    String location;
    String content;
    List<String> images;

    public RecordContent() {
        this.location = null;
        this.content = null;
        this.images = new ArrayList<>();
    }

    public RecordContent(String location, String content, List<String> images) {
        this.location = location;
        this.content = content;
        this.images = images;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
