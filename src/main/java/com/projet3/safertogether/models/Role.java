package com.projet3.safertogether.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Role extends AbstractEntity {

  private String name;


  @JsonBackReference(value ="user_role")
  @ManyToMany(mappedBy = "roles")
  private List<User> users;
}
