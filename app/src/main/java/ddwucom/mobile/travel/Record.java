package ddwucom.mobile.travel;

public class Record {
    String key;
    String thumbnailImg;
    String recordTitle;
    String recordDate;

    public Record() {
        this.key = null;
        this.thumbnailImg = null;
        this.recordTitle = null;
        this.recordDate = null;
    }

    public Record(String key, String thumbnailImg, String recordTitle, String recordDate) {
        this.key = key;
        this.thumbnailImg = thumbnailImg;
        this.recordTitle = recordTitle;
        this.recordDate = recordDate;
    }

    public String getThumbnailImg() {
        return thumbnailImg;
    }

    public String getRecordTitle() {
        return recordTitle;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public String getKey() {
        return key;
    }
}
