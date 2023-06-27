package com.projet3.safertogether.services.impl;


import com.projet3.safertogether.config.JwtUtils;
import com.projet3.safertogether.dto.AuthenticationRequest;
import com.projet3.safertogether.dto.AuthenticationResponse;
import com.projet3.safertogether.dto.LightUserDto;
import com.projet3.safertogether.dto.UserDto;
import com.projet3.safertogether.exceptions.ResourceNotFoundException;
import com.projet3.safertogether.models.Geolocalisation;
import com.projet3.safertogether.models.Role;
import com.projet3.safertogether.models.User;
import com.projet3.safertogether.repositories.GeolocalisationRepository;
import com.projet3.safertogether.repositories.RoleRepository;
import com.projet3.safertogether.repositories.UserRepository;
import com.projet3.safertogether.services.UserService;
import com.projet3.safertogether.storage.FileSystemStorageService;
import com.projet3.safertogether.storage.StorageProperties;
import com.projet3.safertogether.storage.StorageService;
import com.projet3.safertogether.validators.ObjectsValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository repository;
  private static final String ROLE_USER = "ROLE_USER";


  private final ObjectsValidator<UserDto> validator;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtils jwtUtils;
  private final AuthenticationManager authManager;
  private final RoleRepository roleRepository;
  private final StorageService storageService;

  private final GeolocalisationRepository geolocalisationRepository;



  private final  JavaMailSender mailSender;

  @Value("${app.reset-token-expiration-minutes}")
  private int resetTokenExpirationMinutes;

  @Override
  public Integer save(UserDto dto) {
    validator.validate(dto);
    User user = UserDto.toEntity(dto);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return repository.save(user).getId();
  }





  public User saveUserWithGeolocation(UserDto user) {
    Geolocalisation geolocalisation = new Geolocalisation();
   geolocalisation.setLatitude(user.getGeolocalisation().getLatitude());
   geolocalisation.setLongitude(user.getGeolocalisation().getLongitude());

    Geolocalisation savedGeolocalisation = geolocalisationRepository.save(geolocalisation);
    user.setGeolocalisation(savedGeolocalisation);



    User user1 = UserDto.toEntity(user);

    return repository.save(user1);
  }


  @Transactional
  public User findUserWithRole(Integer userId) {
    User user = repository.findById(userId).orElse(null);
    if (user != null) {
      user.getAuthorities(); // Charge le rôle en même temps que l'utilisateur
    }
    return user;
  }

  @Override
  public User uploadUserPhoto(Integer userId, MultipartFile file) {
    User user = repository.findById(userId)
            .orElseThrow(() -> new RuntimeException("L'utilisateur avec l'ID " + userId + " n'a pas été trouvé"));

    String filename = storageService.store(file);
    user.setPhoto(filename);

    try {
      storageService.saveFile(file, filename);
    } catch (IOException e) {
      throw new RuntimeException("Impossible de sauvegarder le fichier " + filename, e);
    }

    return repository.save(user);
  }



  @Override
  public User updateUserWithGeolocation(Integer userId, Geolocalisation geolocalisation) {
    // Étape 1 : récupérer l'utilisateur existant
    Optional<User> optionalUser = repository.findById(userId);

    // Étape 2 : vérifier si l'utilisateur existe
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();

      // Étape 3 : supprimer l'ancienne géolocalisation de l'utilisateur (si elle existe)
      if (user.getGeolocalisation() != null) {
        Geolocalisation deletedGeoloc = geolocalisationRepository.getById(user.getGeolocalisation().getId());
        user.setGeolocalisation(null);
        geolocalisationRepository.delete(deletedGeoloc);
        System.out.println("Ancienne géolocalisation supprimée: " + deletedGeoloc);
      }

      // Étape 4 : créer un nouvel objet Geolocalisation
      Geolocalisation geolocalisationUpdate = new Geolocalisation();
      geolocalisationUpdate.setLatitude(geolocalisation.getLatitude());
      geolocalisationUpdate.setLongitude(geolocalisation.getLongitude());

      // Étape 5 : enregistrer l'objet Geolocalisation
      Geolocalisation savedGeolocalisation = geolocalisationRepository.save(geolocalisationUpdate);

      // Étape 6 : associer l'objet Geolocalisation enregistré à l'utilisateur
      user.setGeolocalisation(savedGeolocalisation);

      // Étape 7 : mettre à jour l'utilisateur dans la base de données
      User updatedUser = repository.save(user);

      return updatedUser;
    } else {
      // Gérer le cas où l'utilisateur n'existe pas
      throw new RuntimeException("L'utilisateur avec l'ID " + userId + " n'a pas été trouvé");
    }
  }

  public User updateUserPhoto(UserDto userDto) {
    User user = repository.findById(userDto.getId()).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    user.setPhoto(userDto.getPhoto());
    return repository.save(user);
  }
  public UserDto saveUserPhoto(UserDto user, MultipartFile file) throws IOException {
    String fileName = file.getOriginalFilename();
    user.setFiles(file.getBytes());

    // Enregistrer le fichier dans le dossier "uploads"
    FileSystemStorageService storageService = new FileSystemStorageService(new StorageProperties());
    storageService.store(file);

    return user;
  }

  @Override
  @Transactional
  public List<UserDto> findAll() {
    return repository.findAll()
        .stream()
        .map(UserDto::fromEntity)
        .collect(Collectors.toList());
  }




  public User findById2(Integer id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public UserDto findById(Integer id) {
    return repository.findById(id)
        .map(UserDto::fromEntity)
        .orElseThrow(() -> new EntityNotFoundException("No user was found with the provided ID : " + id));
  }

  @Override
  public void delete(Integer id) {
    // todo check before delete
    repository.deleteById(id);
  }

  @Override
  @Transactional
  public User validateAccount(Integer id) {
    User user = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("No user was found for user account validation"));

    user.setActive(true);
    repository.save(user);
    return user;
  }

  @Override
  public User invalidateAccount(Integer id) {
    User user = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("No user was found for user account validation"));

    user.setActive(false);
    repository.save(user);
    return user;
  }




    @Override
  @Transactional
  public AuthenticationResponse register(UserDto dto) {
    validator.validate(dto);
    User user = UserDto.toEntity(dto);
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    // Créer une liste de rôles pour l'utilisateur
    List<Role> userRoles = new ArrayList<>();
    userRoles.add(findOrCreateRole(ROLE_USER));

    // Attribuer la liste des rôles à l'utilisateur
    user.setRoles(userRoles);

    var savedUser = repository.save(user);
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", savedUser.getId());
    claims.put("fullName", savedUser.getFirstname() + " " + savedUser.getLastname());
    String token = jwtUtils.generateToken(savedUser, claims);
    return AuthenticationResponse.builder()
            .token(token)
            .build();
  }





  public void saveFilenameName(int userId, String filename) {
    User user = repository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
//    user.setFile(filename);
    repository.save(user);
  }




@Override
public AuthenticationResponse authenticate(AuthenticationRequest request) {
  try {
    authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
  } catch (BadCredentialsException e) {
    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
  }

  // Récupérer l'utilisateur avec ses rôles
  final User user = repository.findUserWithRoles(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
  user.setConnected(true);
  repository.save(user);
  Map<String, Object> claims = new HashMap<>();
  claims.put("userId", user.getId());
  claims.put("fullName", user.getFirstname() + " " + user.getLastname());
  final String token = jwtUtils.generateToken(user, claims);
  return AuthenticationResponse.builder()
          .token(token)
          .user(user)
          .build();
}


  @Override
  public Integer update(UserDto userDto) {
    User user = LightUserDto.toEntity(userDto);
    return repository.save(user).getId();
  }




  private Role findOrCreateRole(String roleName) {
    Role role = roleRepository.findByName(roleName)
        .orElse(null);
    if (role == null) {
      return roleRepository.save(
          Role.builder()
              .name(roleName)
              .build()
      );
    }
    return role;
  }


  public List<User> getUsersByIds(List<Integer> ids) {
    return repository.findByIdIn(ids);
  }




  @Override
  public Optional<User> findUserWithRoles(String email) {
    return repository.findUserWithRoles(email);
  }

  @Override
  public void setUserDisconnected(Integer userId) {
    User user = repository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    user.setConnected(false);
    repository.save(user);
  }

  @Override
  public void requestPasswordReset(String email) {

    User user = repository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    String token = UUID.randomUUID().toString();
    LocalDateTime tokenExpiration = LocalDateTime.now().plusMinutes(resetTokenExpirationMinutes);

    user.setResetToken(token);
    user.setResetTokenExpiration(tokenExpiration);
    repository.save(user);

    String subject = "Réinitialisation de votre mot de passe";
    String content = String.format("Cliquez sur le lien suivant pour réinitialiser votre mot de passe : http://safertogether.fr/reset-password?token=%s", token);

    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("noreply@safertogether.fr");
    message.setTo(user.getEmail());
    System.out.println("Le mail du user : " + user.getEmail());
    message.setSubject(subject);
    message.setText(content);
    System.out.println("Le messaqe : " + message);
    mailSender.send(message);
  }

  @Override
  public void resetPassword(String token, String newPassword) {
    User user = repository.findByResetToken(token)
            .orElseThrow(() -> new NoSuchElementException("Token not found"));

    if (user.getResetTokenExpiration().isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("Token has expired");
    }

    String encodedPassword = passwordEncoder.encode(newPassword);
    user.setPassword(encodedPassword);
    user.setResetToken(null);
    user.setResetTokenExpiration(null);
    repository.save(user);
  }



}
