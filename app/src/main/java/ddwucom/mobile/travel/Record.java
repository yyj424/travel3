package ddwucom.mobile.travel;

import java.util.List;

public class Record {
    String recordTitle;
    String recordContent;
    String recordDate;
    int imageResId;
    List<RecordContent> contentsList;

    public Record(String recordTitle, String recordContent, String recordDate, int imageResId, List<RecordContent> contentsList) {
        this.recordTitle = recordTitle;
        this.recordContent = recordContent;
        this.recordDate = recordDate;
        this.imageResId = imageResId;
        this.contentsList = contentsList;
    }
}
