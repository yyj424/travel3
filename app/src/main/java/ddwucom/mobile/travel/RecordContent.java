package ddwucom.mobile.travel;

import java.util.List;

public class RecordContent {
    String location;
    List<String> images;
    String content;

    public RecordContent() {
        this.location = null;
        this.images = null;
        this.content = null;
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
