package ddwucom.mobile.travel;
import java.util.Map;

public class RecordContent {
    String location;
    String content;

    public RecordContent() {
        this.location = null;
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
}
