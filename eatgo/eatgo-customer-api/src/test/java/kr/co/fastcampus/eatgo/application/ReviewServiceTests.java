package kr.co.fastcampus.eatgo.application;

import kr.co.fastcampus.eatgo.domain.Review;
import kr.co.fastcampus.eatgo.domain.ReviewRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class ReviewServiceTests {

    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        reviewService=new ReviewService(reviewRepository);
    }

    @Test
    public void addReview(){
        Review review=Review.builder().name("JOKER").score(3).description("delicious").build();
        reviewService.addReview(1004L, "JOKER",3,"delicious");
        verify(reviewRepository).save(any());
    }
}