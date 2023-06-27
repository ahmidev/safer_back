package com.projet3.safertogether.services.impl;


import com.projet3.safertogether.models.Geolocalisation;
import com.projet3.safertogether.repositories.GeolocalisationRepository;
import com.projet3.safertogether.services.GeolocalisationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class GeolocalisationServiceImpl implements GeolocalisationService {
    private final GeolocalisationRepository geolocalisationRepository;

    @Override
    public Geolocalisation creer(Geolocalisation geolocalisation) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'creer'");
        return geolocalisationRepository.save(geolocalisation);
    }

    @Override
    public List<Geolocalisation> lire() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'lire'");
        return geolocalisationRepository.findAll();
    }

    @Override
    public Geolocalisation modifier(Integer id, Geolocalisation geolocalisation) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'modifier'");
        return geolocalisationRepository.findById(id)
                .map(g-> {
                    g.setLatitude(geolocalisation.getLatitude());
                    g.setLongitude(geolocalisation.getLongitude());
                    return geolocalisationRepository.save(g);

                }).orElseThrow(() -> new RuntimeException(" echec et mat"));
    }

    @Override
    public String supprimer(Integer id) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'supprimer'");
        geolocalisationRepository.deleteById(id);
        return "geolocalisation supprimée";
    }

}
