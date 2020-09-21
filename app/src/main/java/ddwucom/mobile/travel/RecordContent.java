package ddwucom.mobile.travel;

public class RecordContent {
    String location;
    String imageFolderName;
    String content;

    public RecordContent() {
        this.location = null;
        this.imageFolderName = null;
        this.content = null;
    }

    public RecordContent(String location, String imageResIds, String content) {
        this.location = location;
        this.imageFolderName = imageResIds;
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageFolderName() {
        return imageFolderName;
    }

    public void setImageFolderName(String imageResIds) {
        this.imageFolderName = imageResIds;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
