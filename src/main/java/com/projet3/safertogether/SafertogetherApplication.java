package com.projet3.safertogether;

import com.projet3.safertogether.storage.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(StorageProperties.class)
public class SafertogetherApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafertogetherApplication.class, args);
	}




}
