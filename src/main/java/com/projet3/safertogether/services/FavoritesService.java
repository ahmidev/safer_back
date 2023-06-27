package com.projet3.safertogether.services;


import com.projet3.safertogether.dto.FavoritesDto;
import com.projet3.safertogether.models.Favorites;

import java.util.List;
import java.util.Optional;

public interface FavoritesService {


    Favorites creer(FavoritesDto favoritesDto);

    List<Favorites> lire();

    Favorites modifier(Integer id, FavoritesDto favoritesDto);

    String supprimer(Integer id);

    List<FavoritesDto> findAllFavorites();

    List<FavoritesDto> findFavoritesByUser(Integer userId);
}
