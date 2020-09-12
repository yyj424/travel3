package ddwucom.mobile.travel;

public class RecordContent {
    String location;
    String imageResIds;
    String content;

    public RecordContent() {
        this.location = null;
        this.imageResIds = null;
        this.content = null;
    }

    public RecordContent(String location, String imageResIds, String content) {
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

    public String getImageResIds() {
        return imageResIds;
    }

    public void setImageResIds(String imageResIds) {
        this.imageResIds = imageResIds;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
