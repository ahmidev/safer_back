package com.projet3.safertogether.repositories;


import com.projet3.safertogether.models.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritesRepository extends JpaRepository<Favorites, Integer> {


    List<Favorites> findByUser_Id(Integer userId);

}

