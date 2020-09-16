package ddwucom.mobile.travel;

import java.io.Serializable;
import java.util.List;

public class Record implements Serializable {
    long _id;
    String recordTitle;
    String date;
    int imageResId;
    List<RecordContent> contentsList;

    public Record(String recordTitle, String date, int imageResId, List<RecordContent> contentsList) {
        this.recordTitle = recordTitle;
        this.date = date;
        this.imageResId = imageResId;
        this.contentsList = contentsList;
    }

    public Record(long _id, String recordTitle, String date, int imageResId, List<RecordContent> contentsList) {
        this._id = _id;
        this.recordTitle = recordTitle;
        this.date = date;
        this.imageResId = imageResId;
        this.contentsList = contentsList;
    }

    public long get_id() {
        return _id;
    }

    public String getRecordTitle() {
        return recordTitle;
    }

    public void setRecordTitle(String recordTitle) {
        this.recordTitle = recordTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public List<RecordContent> getContentsList() {
        return contentsList;
    }

    public void setContentsList(List<RecordContent> contentsList) {
        this.contentsList = contentsList;
    }
}
