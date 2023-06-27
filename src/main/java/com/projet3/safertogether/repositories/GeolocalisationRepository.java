package com.projet3.safertogether.repositories;


import com.projet3.safertogether.models.Geolocalisation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeolocalisationRepository extends JpaRepository<Geolocalisation, Integer> {
}
