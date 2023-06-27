package com.projet3.safertogether.services.impl;


import com.projet3.safertogether.dto.FavoritesDto;

import com.projet3.safertogether.models.Favorites;
import com.projet3.safertogether.models.User;
import com.projet3.safertogether.repositories.FavoritesRepository;
import com.projet3.safertogether.services.FavoritesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class FavoritesServiceImpl implements FavoritesService {

    private final FavoritesRepository favoritesRepository;

    private final UserServiceImpl userService;

    @Override
    public Favorites creer(FavoritesDto favoritesDto) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'creer'");
        User user = userService.findById2(favoritesDto.getUser());
        User userFavorites = userService.findById2(favoritesDto.getFavoriteUser());
        Favorites favorites = favoritesDto.toEntity(favoritesDto,user,userFavorites);

        return favoritesRepository.save(favorites);

    }


    public List<FavoritesDto> findFavoritesByUser(Integer userId) {
        List<Favorites> favorites = favoritesRepository.findByUser_Id(userId);
        return favorites.stream().map(FavoritesDto::fromEntity).collect(Collectors.toList());
    }





    public List<FavoritesDto> findAllFavorites() {
        List<Favorites> favoritesList = favoritesRepository.findAll();
        return favoritesList.stream()
                .map(FavoritesDto::fromEntity)
                .collect(Collectors.toList());
    }





    @Override
    public List<Favorites> lire() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'lire'");
        return favoritesRepository.findAll();
    }

    @Override
    public Favorites modifier(Integer id, FavoritesDto favoritesDto) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'modifier'");
        return favoritesRepository.findById(id)
                .map(f-> {

                    f.setFavoriteUser(userService.findById2(favoritesDto.getFavoriteUser()));
                    f.setUser(userService.findById2(favoritesDto.getUser()));
                    return favoritesRepository.save(f);

                }).orElseThrow(() -> new RuntimeException(" echec et mat"));
    }

    @Override
    public String supprimer(Integer id) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'supprimer'");
        favoritesRepository.deleteById(id);
        return "favoris supprimé";
    }

}
