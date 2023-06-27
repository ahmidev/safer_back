package com.projet3.safertogether.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@SuperBuilder
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User  extends AbstractEntity implements UserDetails {



    private String resetToken;

    private LocalDateTime resetTokenExpiration;

    @OneToOne(cascade = CascadeType.ALL) // relation avec la table geoloc
    @JoinColumn(name = "geolocation_id", referencedColumnName = "id")
    private Geolocalisation geolocalisation;


    @JsonManagedReference
    @OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reviews> reviewsGiven;


    @JsonManagedReference
    @OneToMany(mappedBy = "reviewed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reviews> reviewsReceived;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Favorites> favorites;

    @JsonManagedReference
    @OneToMany(mappedBy = "favoriteUser", cascade = CascadeType.ALL)
    private Set<Favorites> favoriteUsers;

//    @JsonManagedReference("user-favorites")
//    @ManyToMany
//    @JoinTable(name = "user_favorites",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "favorite_user_id"),
//            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "favorite_user_id"})})
//    private Set<User> favorites;

    @Column(nullable = false)
    private String firstname;


    @Column(nullable = false)
    private String lastname;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private String birthday;

    @Column(nullable = false)
    private String password;
    private boolean active;

    @JsonManagedReference(value ="user_role")
    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;



//    @OneToOne
//    @JoinColumn(name = "role_id", referencedColumnName = "id")
//    private Role role;

    private boolean connected;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String photo;

    //@Column(nullable = false)
    @Lob
    private byte[] files;

    public User(String firstname, String lastname, String email, String password, LocalDate birthday, String filenamme) {
    }
//    @Lob
//    @Basic(fetch = FetchType.LAZY)
//    private byte[] file;

//    private String filenamme;


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.singletonList(new SimpleGrantedAuthority(role.getName()));
//    }
@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    for (Role role : roles) {
        authorities.add(new SimpleGrantedAuthority(role.getName()));
    }
    return authorities;
}

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}

