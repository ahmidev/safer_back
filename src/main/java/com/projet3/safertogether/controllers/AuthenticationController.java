package com.projet3.safertogether.controllers;


import com.projet3.safertogether.dto.AuthenticationRequest;
import com.projet3.safertogether.dto.AuthenticationResponse;
import com.projet3.safertogether.dto.UserDto;
import com.projet3.safertogether.models.User;
import com.projet3.safertogether.services.UserService;
import com.projet3.safertogether.storage.FileSystemStorageService;
import com.projet3.safertogether.storage.StorageProperties;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "authentication")
public class AuthenticationController {

  private final UserService userService;



@PostMapping("/register")
public ResponseEntity<AuthenticationResponse> register(
        @RequestParam("file") MultipartFile file,
        @RequestParam("firstname") String firstname,
        @RequestParam("lastname") String lastname,
        @RequestParam("birthday") String birthday,
        @RequestParam("email") String email,
        @RequestParam("password") String password) throws IOException {

  UserDto user = new UserDto(firstname, lastname,  email, birthday);

  // Convert MultipartFile to InputStream
  InputStream is = file.getInputStream();

  // Create a ByteArrayOutputStream to hold the zipped file
  ByteArrayOutputStream baos = new ByteArrayOutputStream();

  // Create a ZipOutputStream from the ByteArrayOutputStream
  ZipOutputStream zos = new ZipOutputStream(baos);

  // Create a ZipEntry
  ZipEntry entry = new ZipEntry(file.getOriginalFilename());

  // Put the entry into the zip file
  zos.putNextEntry(entry);

  // Write the file data to the zip file
  byte[] bytes = new byte[1024];
  int length;
  while ((length = is.read(bytes)) >= 0) {
    zos.write(bytes, 0, length);
  }

  // Close ZipEntry and ZipOutputStream
  zos.closeEntry();
  zos.close();

  // Get the zipped file bytes
  byte[] compressedFileBytes = baos.toByteArray();

  // Set the compressed file bytes to the user
  user.setFiles(compressedFileBytes);

  return ResponseEntity.ok(userService.register(user));
}





  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(userService.authenticate(request));
  }


  @PutMapping("/{userId}/disconnected")
  public ResponseEntity<Void> setUserDisconnected(@PathVariable Integer userId) {
    userService.setUserDisconnected(userId);
    return ResponseEntity.ok().build();
  }
}
