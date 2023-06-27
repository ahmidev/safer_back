package com.projet3.safertogether.services.impl;



import com.projet3.safertogether.models.Reviews;
import com.projet3.safertogether.repositories.ReviewsRepository;
import com.projet3.safertogether.services.ReviewsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewsServiceImpl implements ReviewsService {


    private final ReviewsRepository reviewsRepository;

    @Override
    public Reviews creer(Reviews reviews) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'creer'");
        return reviewsRepository.save(reviews);

    }

    @Override
    public List<Reviews> lire() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'lire'");
        return reviewsRepository.findAll();
    }

    @Override
    public Reviews modifier(Long id, Reviews reviews) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'modifier'");
        return reviewsRepository.findById(id)
                .map(r-> {
                    r.setCreatedDate(reviews.getCreatedDate());
                    r.setReviewed(reviews.getReviewed());
                    r.setReviewer(reviews.getReviewer());
                    r.setRating(reviews.getRating());
                    return reviewsRepository.save(r);

                }).orElseThrow(() -> new RuntimeException(" echec et mat"));

    }

    @Override
    public String supprimer(Long id) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'supprimer'");
        reviewsRepository.deleteById(id);
        return " review supprimé";
    }
}
