package ddwucom.mobile.travel;

import java.util.List;

public class RecordContent {
    String location;
    String imageFolderName;
    String content;
    List<String> images;

    public RecordContent() {
        this.location = null;
        this.imageFolderName = null;
        this.content = null;
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
