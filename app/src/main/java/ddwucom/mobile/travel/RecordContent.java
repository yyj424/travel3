package ddwucom.mobile.travel;

public class RecordContent {
    String uid;
    String location;
    String imageFolderName;
    String content;

    public RecordContent() {
        this.uid = null;
        this.location = null;
        this.imageFolderName = null;
        this.content = null;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public void setImageFolderName(String imageFolderName) {
        this.imageFolderName = imageFolderName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
