package com.quickbites.service;

import java.util.List;

import com.quickbites.Exception.ReviewException;
import com.quickbites.dto.ReviewRequest;
import com.quickbites.entities.Review;
import com.quickbites.entities.User;


public interface ReviewService {
	
    public Review submitReview(ReviewRequest review,User user);
    public void deleteReview(Long reviewId) throws ReviewException;
    public double calculateAverageRating(List<Review> reviews);
}
