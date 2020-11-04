package ddwucom.mobile.travel;

public class Album {
    String thumbnail;
    String albumName;
    int imageCnt;

    public Album(String thumbnail, String albumName, int imageCnt) {
        this.thumbnail = thumbnail;
        this.albumName = albumName;
        this.imageCnt = imageCnt;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getImageCnt() {
        return imageCnt;
    }

    public void setImageCnt(int imageCnt) {
        this.imageCnt = imageCnt;
    }
}
