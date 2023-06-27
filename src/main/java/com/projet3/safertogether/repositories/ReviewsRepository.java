package com.projet3.safertogether.repositories;


import com.projet3.safertogether.models.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewsRepository extends JpaRepository<Reviews, Long> {
}
