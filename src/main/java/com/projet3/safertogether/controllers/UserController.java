package com.projet3.safertogether.controllers;


import com.projet3.safertogether.dto.LightUserDto;
import com.projet3.safertogether.dto.UserDto;
import com.projet3.safertogether.exceptions.ResourceNotFoundException;
import com.projet3.safertogether.models.Geolocalisation;
import com.projet3.safertogether.models.User;
import com.projet3.safertogether.repositories.UserRepository;
import com.projet3.safertogether.services.UserService;
import com.projet3.safertogether.storage.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "user")
public class UserController {


  private final UserService userService;
  private final StorageService storageService;

  private  final UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @PutMapping("/{userId}/update")
  public ResponseEntity<?> updateUserProfile(
          @PathVariable Integer userId,
          @RequestParam(name = "email",required = false) String email,
          @RequestParam(name = "password", required = false) String password,
          @RequestParam(name = "birthday", required = false) String birthday,
          @RequestParam(name = "photo", required = false) MultipartFile photo) throws IOException {

    User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    //vérifier si un élément est vide ou null en utilisant la méthode StringUtils.isEmpty() de Apache Commons Lang. Cette méthode retourne true si la
    if (!StringUtils.isEmpty(email)) {
      user.setEmail(email);
    }

    if (!StringUtils.isEmpty(password)){
      user.setPassword(passwordEncoder.encode(password));
    }

    if (!StringUtils.isEmpty(birthday)) {
      user.setBirthday(birthday);
    }

    if (photo != null) {
      byte[] fileData = photo.getBytes();

      String fileDataBase64 = Base64.getEncoder().encodeToString(fileData);

//      FileSystemStorageService storageService = new FileSystemStorageService(new StorageProperties());
//      storageService.store(photo);
      user.setPhoto(fileDataBase64);
    }
    userRepository.save(user);

    return ResponseEntity.ok("User profile updated successfully");
  }


  @PutMapping("/geolocation/{userId}")
  public ResponseEntity<?> updateUserGeolocation(@PathVariable("userId") Integer userId,
                                                    @RequestBody Geolocalisation geolocation
                                                   ) {
    User updatedUser = userService.updateUserWithGeolocation(userId,geolocation);
    if (updatedUser == null) {
      return ResponseEntity.notFound().build();
    }
//    return ResponseEntity.ok(updatedUser);


    return ResponseEntity.ok(updatedUser);
  }



/****** ancien methode *******/
//  @PostMapping("/{userId}/save-photo")
//  public ResponseEntity<?> savePhoto(@PathVariable Integer userId, @RequestParam("file") MultipartFile file) throws IOException {
//    try {
//      UserDto userDto = userService.findById(userId);
//      if (userDto == null) {
//        return new ResponseEntity<>("Utilisateur non trouvé.", HttpStatus.NOT_FOUND);
//      }
//
//      byte[] fileData = file.getBytes();
//
//      String fileDataBase64 = Base64.getEncoder().encodeToString(fileData);
//
////      FileSystemStorageService storageService = new FileSystemStorageService(new StorageProperties());
////      storageService.store(photo);
//      userDto.setPhoto(fileDataBase64);
//
//      // Mettez à jour l'utilisateur dans la base de données
//      userService.updateUserPhoto(userDto);
//      return new ResponseEntity<>("Photo enregistrée avec succès.", HttpStatus.OK);
//    } catch (RuntimeException e) {
//      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//    }
//  }

/*** nouvelle methode de test ********/
@PostMapping("/{userId}/save-photo")
public ResponseEntity<?> savePhoto(@PathVariable Integer userId, @RequestParam("file") MultipartFile file) {
  try {
    UserDto userDto = userService.findById(userId);
    if (userDto == null) {
      return new ResponseEntity<>("Utilisateur non trouvé.", HttpStatus.NOT_FOUND);
    }

    byte[] fileData = file.getBytes();

    // Convertir le tableau de bytes en une image BufferedImage
    BufferedImage img = ImageIO.read(new ByteArrayInputStream(fileData));

    // Compresser l'image
    BufferedImage compressedImage = compressImage(img);

    // Convertir l'image compressée en un tableau de bytes
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(compressedImage, "jpg", baos);
    baos.flush();
    byte[] compressedImageBytes = baos.toByteArray();
    baos.close();

    // Encoder le tableau de bytes de l'image compressée en Base64
    String compressedImageBase64 = Base64.getEncoder().encodeToString(compressedImageBytes);

    userDto.setPhoto(compressedImageBase64);

    // Mettez à jour l'utilisateur dans la base de données
    userService.updateUserPhoto(userDto);
    return new ResponseEntity<>("Photo enregistrée avec succès.", HttpStatus.OK);
  } catch (IOException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  }
}


  private BufferedImage compressImage(BufferedImage image) throws IOException {
    // Obtenir les écrivains d'image pour le format jpeg
    Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
    if (!writers.hasNext()) throw new IllegalStateException("No writers found");

    ImageWriter writer = writers.next();

    // Create a ByteArrayOutputStream for the output
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ImageOutputStream ios = ImageIO.createImageOutputStream(byteArrayOutputStream);
    writer.setOutput(ios);

    ImageWriteParam param = writer.getDefaultWriteParam();

    // Vérifiez si la compression d'image est prise en charge
    if (param.canWriteCompressed()) {
      param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
      param.setCompressionQuality(0.3f);  // Changez cela : plus la valeur est faible, plus la qualité est faible et plus la taille en octets de l'image est réduite.
    }

    // Écrivez l'image avec la compression
    writer.write(null, new javax.imageio.IIOImage(image, null, null), param);

    // Convertir le tableau de bytes en BufferedImage
    byte[] byteArray = byteArrayOutputStream.toByteArray();
    BufferedImage compressedImage = ImageIO.read(new ByteArrayInputStream(byteArray));

    ios.close();
    writer.dispose();

    return compressedImage;
  }

//  @PostMapping("/geoloc")
//  public ResponseEntity<Integer> saveUserWithGeolocation(
//          @RequestBody LightUserDto userDto
//          ) {
////    return ResponseEntity.ok(service.saveUserWithGeolocation(userDto).getId());
//    return ResponseEntity.ok(service.update(userDto));
//  }



  @GetMapping("/")
  public ResponseEntity<List<UserDto>> findAll() {
    return ResponseEntity.ok(userService.findAll());
  }

  @GetMapping("/{user-id}")
  public ResponseEntity<UserDto> findById(
      @PathVariable("user-id") Integer userId
  ) {
    return ResponseEntity.ok(userService.findById(userId));
  }

  @PatchMapping("/validate/{user-id}")
  public ResponseEntity<User> validateAccount(
      @PathVariable("user-id") Integer userId
  ) {
    return ResponseEntity.ok(userService.validateAccount(userId));
  }

  @PatchMapping("/invalidate/{user-id}")
  public ResponseEntity<User> invalidateAccount(
      @PathVariable("user-id") Integer userId
  ) {
    return ResponseEntity.ok(userService.invalidateAccount(userId));
  }

  @DeleteMapping("/{user-id}")
  public ResponseEntity<Void> delete(
      @PathVariable("user-id") Integer userId
  ) {
    userService.delete(userId);
    return ResponseEntity.accepted().build();
  }



//  @GetMapping("/uploads/{filename:.+}")
//  public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws MalformedURLException, StorageFileNotFoundException {
//    Path file = storageService.getUploadDirectory().resolve(filename);
//    Resource resource = new UrlResource(file.toUri());
//
//    if (resource.exists() || resource.isReadable()) {
//      return ResponseEntity.ok()
//              .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
//              .body(resource);
//    } else {
//      throw new StorageFileNotFoundException("Could not read file: " + filename);
//    }
//  }


  @GetMapping("/uploads/{filename:.+}")
  public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
    try {
      Resource file = storageService.loadAsResource(filename);
      String contentType = getContentType(file.getFilename());

      return ResponseEntity.ok()
              .contentType(MediaType.parseMediaType(contentType))
              .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
              .body(file);
    } catch (StorageFileNotFoundException e) {
      // Gérer l'exception, par exemple en retournant une réponse d'erreur
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  private String getContentType(String filename) {
    try {
      return Files.probeContentType(Paths.get(filename));
    } catch (IOException e) {
      return "application/octet-stream";
    }
  }


  @PostMapping("/reset")
  public ResponseEntity<?> requestPasswordReset(@RequestParam("email") String email) {
    userService.requestPasswordReset(email);
    return ResponseEntity.ok().body(Map.of("message", "Un email de réinitialisation du mot de passe a été envoyé sur votre adresse " + email));
  }


  @PutMapping("/reset-password")
  public ResponseEntity<?> resetPassword(
          @RequestParam("token") String token,
          @RequestParam("newPassword") String newPassword) {
    userService.resetPassword(token, newPassword);
    return ResponseEntity.ok().body(Map.of("message","mot de passe modifié avec succès"));
  }


}
