package com.example.appAnime.model;
import java.io.Serializable;

public class Review implements Serializable {
    private int reviewId;
    private String title;
    private String review;
    private int rating;
    private Anime anime;
    private Usuario user;

    public Review() {
    }

    public Review(int reviewId, String title, String review, int rating, Anime anime, Usuario user) {
        this.reviewId = reviewId;
        this.title = title;
        this.review = review;
        this.rating = rating;
        this.anime = anime;
        this.user = user;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Anime getAnime() {
        return anime;
    }

    public void setAnime(Anime anime) {
        this.anime = anime;
    }

    public Usuario getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", title='" + title + '\'' +
                ", review='" + review + '\'' +
                ", rating=" + rating +
                ", anime=" + anime +
                '}';
    }
}