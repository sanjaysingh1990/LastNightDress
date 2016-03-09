package com.eowise.recyclerview.stickyheaders.samples.data;

/**
 * Created by sanju on 18/2/16.
 */
public class ReviewsData {
    private int rated_value;
    private String reviewbyuname;
    private String reviewmessage;
    private String reviewreplied;
    private String profilepic;
    private String reviewid;

    public int getRated_value() {
        return rated_value;
    }

    public void setRated_value(int rated_value) {
        this.rated_value = rated_value;
    }

    public String getReviewbyuname() {
        return reviewbyuname;
    }

    public void setReviewbyuname(String reviewbyuname) {
        this.reviewbyuname = reviewbyuname;
    }

    public String getReviewmessage() {
        return reviewmessage;
    }

    public void setReviewmessage(String reviewmessage) {
        this.reviewmessage = reviewmessage;
    }

    public String getReviewreplied() {
        return reviewreplied;
    }

    public void setReviewreplied(String reviewreplied) {
        this.reviewreplied = reviewreplied;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getReviewid() {
        return reviewid;
    }

    public void setReviewid(String reviewid) {
        this.reviewid = reviewid;
    }


    public boolean isreplied() {
        return isreplied;
    }

    public void setIsreplied(boolean isreplied) {
        this.isreplied = isreplied;
    }

    private boolean isreplied;

}
