package ddwucom.mobile.travel;

public class Record {
    String uid;
    String recordFolder;
    String recordTitle;
    String recordDate;
    String imageUri;

    public Record() {
        this.uid = null;
        this.recordFolder = null;
        this.recordTitle = null;
        this.recordDate = null;
        this.imageUri = null;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setRecordFolder(String recordFolder) {
        this.recordFolder = recordFolder;
    }

    public void setRecordTitle(String recordTitle) {
        this.recordTitle = recordTitle;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
