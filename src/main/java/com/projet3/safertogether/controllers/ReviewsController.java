package com.projet3.safertogether.controllers;


import com.projet3.safertogether.models.Reviews;
import com.projet3.safertogether.services.ReviewsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@AllArgsConstructor
public class ReviewsController {


    private final ReviewsService reviewsService;

    @PostMapping("/create")
    public Reviews create(@RequestBody Reviews reviews){
        return reviewsService.creer(reviews);
    }

    @GetMapping("/read")
    public List<Reviews> read(){
        return reviewsService.lire();

    }


    @PutMapping("/update/{id}") //modifier
    public Reviews update(@PathVariable Long id,@RequestBody Reviews reviews){
        return reviewsService.modifier(id, reviews);
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        return reviewsService.supprimer(id);
    }
}
