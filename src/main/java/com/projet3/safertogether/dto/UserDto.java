package com.projet3.safertogether.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.projet3.safertogether.models.Favorites;
import com.projet3.safertogether.models.Geolocalisation;
import com.projet3.safertogether.models.Role;
import com.projet3.safertogether.models.User;
import jdk.jfr.DataAmount;
import lombok.*;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

  @Id
  @GeneratedValue
  private Integer id;

  @NotNull(message = "Le prenom ne doit pas etre vide")
  @NotEmpty(message = "Le prenom ne doit pas etre vide")
  @NotBlank(message = "Le prenom ne doit pas etre vide")
  private String firstname;

  @NotNull(message = "Le nom ne doit pas etre vide")
  @NotEmpty(message = "Le nom ne doit pas etre vide")
  @NotBlank(message = "Le nom ne doit pas etre vide")
  private String lastname;

  @NotNull(message = "L'email ne doit pas etre vide")
  @NotEmpty(message = "L'email ne doit pas etre vide")
  @NotBlank(message = "L'email ne doit pas etre vide")
  @Email(message = "L'email n'est conforme")
  private String email;

  @NotNull(message = "Le mot de passe ne doit pas etre vide")
  @NotEmpty(message = "Le mot de passe ne doit pas etre vide")
  @NotBlank(message = "Le mot de passe ne doit pas etre vide")
  @Size(min = 8, max = 16, message = "Le mot de passe doit etre entre 8 et 16 caracteres")
  private String password;



  @NotNull(message = "La date de naissance ne doit pas etre vide")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
  private String birthday;
//  private String identity;



//  private byte[] file;

  @Lob
  private byte[] files;

  private Geolocalisation geolocalisation;

  private String photo;


  private List<Role> roles;


  private boolean active;

  private boolean connected;
  private Set<Favorites> favorites;


  private Set<Favorites> favoriteUsers;

  public UserDto(String firstname, String lastname, String email, String birthday) {
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.birthday = birthday;


  }


//  private boolean active;



  public static UserDto fromEntity(User user) {
    // null check
    return UserDto.builder()
        .id(user.getId())
        .firstname(user.getFirstname())
        .lastname(user.getLastname())
        .email(user.getEmail())
            .birthday(user.getBirthday())
//            .files(user.getFiles())
            .geolocalisation(user.getGeolocalisation())
            .roles(user.getRoles())
            .active(user.isActive())
            .connected(user.isConnected())
            .photo(user.getPhoto())
            .favoriteUsers(user.getFavoriteUsers())
            .favorites(user.getFavorites())
        .build();
  }

  public static User toEntity(UserDto user) {
    // null check
    return User.builder()
        .id(user.getId())
        .firstname(user.getFirstname())
        .lastname(user.getLastname())
        .email(user.getEmail())

            .birthday(user.getBirthday())
//            .files(user.getFiles())
            .geolocalisation(user.getGeolocalisation())
            .roles(user.getRoles())
            .active(user.isActive())
            .connected(user.isConnected())
            .photo(user.getPhoto())
            .favoriteUsers(user.getFavoriteUsers())
            .favorites(user.getFavorites())

        .build();
  }

}
