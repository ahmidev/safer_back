package com.projet3.safertogether.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.projet3.safertogether.models.Favorites;
import com.projet3.safertogether.models.User;
import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FavoritesDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer user;

    private Integer favoriteUser;

    public  static FavoritesDto fromEntity(Favorites favorites){
        return FavoritesDto.builder()
                .id(favorites.getId())
                .user(favorites.getUser().getId())
                .favoriteUser(favorites.getFavoriteUser().getId())
                .build();
    }

    public  static Favorites toEntity(FavoritesDto favoritesDto, User user, User favoriteUser){
        return Favorites.builder()
                .id(favoritesDto.getId())
                .user(user)
                .favoriteUser(favoriteUser)
                .build();
    }



}
