package com.projet3.safertogether.services;


import com.projet3.safertogether.models.Geolocalisation;

import java.util.List;

public interface GeolocalisationService {

    Geolocalisation creer(Geolocalisation geolocalisation);

    List<Geolocalisation> lire();

    Geolocalisation modifier(Integer id, Geolocalisation geolocalisation);

    String supprimer(Integer id);
}
