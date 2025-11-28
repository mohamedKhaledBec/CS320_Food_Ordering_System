package FOS_CORE;

public class Rating {
    private int ratingValue;
    private String reviewText;

    public Rating(){ }

    public Rating(int ratingValue, String reviewText){
        this.ratingValue = ratingValue;
        this.reviewText = reviewText;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
