package com.projet3.safertogether.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@SuperBuilder
@Table(name = "user_favorites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Favorites extends AbstractEntity {


   @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


   @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "favorite_user_id")
    private User favoriteUser;



}

   // nous utilisons une relation ManyToOne avec l'entité User pour faire référence à l'utilisateur qui a ajouté l'utilisateur favori, ainsi que l'utilisateur favori lui-même. La table intermédiaire "user_favorites" contient une clé étrangère vers l'utilisateur et une clé étrangère vers l'utilisateur favori. La contrainte unique garantit qu'un utilisateur ne peut pas ajouter plusieurs fois le même utilisateur en tant que favori.