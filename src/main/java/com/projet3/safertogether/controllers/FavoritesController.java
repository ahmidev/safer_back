package com.projet3.safertogether.controllers;


import com.projet3.safertogether.dto.FavoritesDto;
import com.projet3.safertogether.models.Favorites;
import com.projet3.safertogether.services.FavoritesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/fav")
@AllArgsConstructor
public class FavoritesController {


    private final FavoritesService favoritesService;

    @PostMapping("/create")
    public Favorites create(@RequestBody FavoritesDto favoritesDto){
        return favoritesService.creer(favoritesDto);
    }

    @GetMapping("/read")
    public List<Favorites> read(){
        return favoritesService.lire();

    }
    @GetMapping("/readAll")
    public ResponseEntity<List<FavoritesDto>> getAllFavorites() {
        List<FavoritesDto> favoritesDtoList = favoritesService.findAllFavorites();
        return ResponseEntity.ok(favoritesDtoList);
    }


    @GetMapping("/{user}")
    public ResponseEntity<List<FavoritesDto>> findFavoriteByUser(
            @PathVariable("user") Integer userId) {
        List<FavoritesDto> favoriteDtoList = favoritesService.findFavoritesByUser(userId);
        return ResponseEntity.ok(favoriteDtoList);
    }



    @PutMapping("/update/{id}") //modifier
    public Favorites update(@PathVariable Integer id,@RequestBody FavoritesDto favoritesDto){
        return favoritesService.modifier(id, favoritesDto);
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        return favoritesService.supprimer(id);
    }
}
