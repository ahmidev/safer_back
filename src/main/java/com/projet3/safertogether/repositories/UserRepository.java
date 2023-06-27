package com.projet3.safertogether.repositories;


import com.projet3.safertogether.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);

  List<User> findByIdIn(List<Integer> ids);

  // Récupérer un utilisateur avec ses rôles en utilisant @Query
  @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = :email")
  Optional<User> findUserWithRoles(@Param("email") String email);

  Optional<User> findByResetToken(String resetToken);
}
