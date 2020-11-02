package ddwucom.mobile.travel;

import com.google.firebase.database.Exclude;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Record {
    String key;
    String uid;
    String gid;
    String nickname;
    String recordFolder;
    String thumbnailImg;
    String recordTitle;
    String recordDate;

    public Record() {
        this.key = null;
        this.uid = null;
        this.nickname = null;
        this.recordFolder = null;
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

    public Record(String key, String nickname, String thumbnailImg, String recordTitle, String recordDate) {
        this.key = key;
        this.nickname = nickname;
        this.thumbnailImg = thumbnailImg;
        this.recordTitle = recordTitle;
        this.recordDate = recordDate;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public void setRecordFolder(String recordFolder) {
        this.recordFolder = recordFolder;
    }

    public void setThumbnailImg(String thumbnailImg) {
        this.thumbnailImg = thumbnailImg;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setRecordTitle(String recordTitle) {
        this.recordTitle = recordTitle;
    }

    public void setRecordDate(String recordDate) {
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

    public String getUid() {
        return uid;
    }

    public String getRecordFolder() {
        return recordFolder;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("recordTitle", recordTitle);
        result.put("recordDate", recordDate);
        result.put("recordFolder", recordFolder);

        return result;
    }

    static class SortByDate implements Comparator<Record> {
        @Override
        public int compare(Record r1, Record r2) {
            return r2.getRecordDate().compareTo(r1.getRecordDate());
        }
    }
}
