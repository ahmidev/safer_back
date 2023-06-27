package com.projet3.safertogether.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@SuperBuilder
@Table(name = "geolocalisation")
@Getter
@Setter
@NoArgsConstructor
public class Geolocalisation extends AbstractEntity{





    private BigDecimal longitude;

    private BigDecimal latitude;

}

