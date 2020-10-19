package ddwucom.mobile.travel;

import java.util.List;

public class MyReview {
    private String pid;
    private String userId;
    List<String> reviewImages;
    private double rating;
    private String date;
    private String content;

    private long score1;
    private long score2;
    private long score3;
    private long score4;

    public MyReview() {
        this.userId = "";
        this.reviewImages = null;
        this.rating = 0.0;
        this.date = "";
        this.content = "";
        this.score1 = 0;
        this.score2 = 0;
        this.score3 = 0;
        this.score4 = 0;
    }


    public MyReview(String userId, List<String> reviewImages, double rating, String date, String content, long score1, long score2, long score3, long score4) {
        this.userId = userId;
        this.reviewImages = reviewImages;
        this.rating = rating;
        this.date = date;
        this.content = content;
        this.score1 = score1;
        this.score2 = score2;
        this.score3 = score3;
        this.score4 = score4;
    }

    public List<String> getReviewImages() {
        return reviewImages;
    }

    public void setReviewImages(List<String> reviewImages) {
        this.reviewImages = reviewImages;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public long getScore1() {
        return score1;
    }

    public void setScore1(long score1) {
        this.score1 = score1;
    }

    public long getScore2() {
        return score2;
    }

    public void setScore2(long score2) {
        this.score2 = score2;
    }

    public long getScore3() {
        return score3;
    }

    public void setScore3(long score3) {
        this.score3 = score3;
    }

    public long getScore4() {
        return score4;
    }

    public void setScore4(long score4) {
        this.score4 = score4;
    }
}
