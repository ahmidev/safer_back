package com.projet3.safertogether.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor //La contrainte unique est ajoutée en utilisant l'annotation @UniqueConstraint sur la table review. Cette contrainte garantit que la combinaison des colonnes reviewer_id et reviewed_id est unique dans la table review. Ainsi, un utilisateur ne peut pas écrire plus d'un avis pour un autre utilisateur.
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"reviewer_id", "reviewed_id"}))
public class Reviews extends  AbstractEntity{



    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private User reviewer;


    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_id")
    private User reviewed;

    @Column(nullable = false)
    private int rating;

    @Column(length = 500)
    private String comment;






}

