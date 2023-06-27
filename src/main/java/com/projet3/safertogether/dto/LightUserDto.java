package com.projet3.safertogether.dto;


import com.projet3.safertogether.models.Geolocalisation;
import com.projet3.safertogether.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class LightUserDto {

  private Integer id;

  @NotNull(message = "Le prenom ne doit pas etre vide")
  @NotEmpty(message = "Le prenom ne doit pas etre vide")
  @NotBlank(message = "Le prenom ne doit pas etre vide")
  private String firstname;

  @NotNull(message = "Le nom ne doit pas etre vide")
  @NotEmpty(message = "Le nom ne doit pas etre vide")
  @NotBlank(message = "Le nom ne doit pas etre vide")
  private String lastname;


  private Geolocalisation geolocalisation;
  public static LightUserDto fromEntity(User user) {
    // null check
    return LightUserDto.builder()
        .id(user.getId())
        .firstname(user.getFirstname())
        .lastname(user.getLastname())
            .geolocalisation(user.getGeolocalisation())
        .build();
  }

  public static User toEntity(UserDto user) {
    // null check
    return User.builder()
        .id(user.getId())
        .firstname(user.getFirstname())
        .lastname(user.getLastname())
            .geolocalisation(user.getGeolocalisation())
        .build();
  }

}
