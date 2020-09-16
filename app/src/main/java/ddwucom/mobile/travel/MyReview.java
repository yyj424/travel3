package ddwucom.mobile.travel;

import java.util.ArrayList;

public class MyReview {
    private long _id;
    private String rating;
    private ArrayList<String> Imagelist;
    private String category1;
    private String category2;
    private String category3;
    private String category4;

    public MyReview(long _id, String rating, ArrayList<String> imagelist, String category1, String category2, String category3, String category4) {
        this._id = _id;
        this.rating = rating;
        this.Imagelist = imagelist;//this가 빠져도 되나?
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
        this.category4 = category4;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public ArrayList<String> getImagelist() {
        return Imagelist;
    }

    public void setImagelist(ArrayList<String> imagelist) {
        Imagelist = imagelist;
    }

    public String getCategory1() {
        return category1;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public String getCategory3() {
        return category3;
    }

    public void setCategory3(String category3) {
        this.category3 = category3;
    }

    public String getCategory4() {
        return category4;
    }

    public void setCategory4(String category4) {
        this.category4 = category4;
    }
}
