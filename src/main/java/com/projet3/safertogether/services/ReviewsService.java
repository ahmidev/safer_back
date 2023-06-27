package com.projet3.safertogether.services;


import com.projet3.safertogether.models.Reviews;

import java.util.List;

public interface ReviewsService {

    Reviews creer(Reviews reviews);

    List<Reviews> lire();



    Reviews modifier(Long id, Reviews reviews);

    String supprimer(Long id);
}
