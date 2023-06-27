package com.projet3.safertogether.controllers;


import com.projet3.safertogether.models.Geolocalisation;
import com.projet3.safertogether.services.GeolocalisationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;




@RestController
@RequestMapping("/geoloc")
@AllArgsConstructor
public class GeolocalisationController {



    private final GeolocalisationService geolocalisationService;

    @PostMapping("/create")
    public Geolocalisation create(@RequestBody Geolocalisation geolocalisation){
        return geolocalisationService.creer(geolocalisation);
    }

    @GetMapping("/read")
    public List<Geolocalisation> read(){
        return geolocalisationService.lire();

    }

    @PutMapping("/update/{id}") //modifier
    public Geolocalisation update(@PathVariable Integer id,@RequestBody Geolocalisation geolocalisation){
        return geolocalisationService.modifier(id, geolocalisation);
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        return geolocalisationService.supprimer(id);
    }
}
