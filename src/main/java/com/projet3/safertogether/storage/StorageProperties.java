package com.projet3.safertogether.storage;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Dossier de stockage des fichiers
     */
    private String location = "uploads";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


//    @Bean
//    public StorageProperties storageProperties() {
//        return new StorageProperties();
//    }
}

