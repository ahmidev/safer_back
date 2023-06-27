package com.projet3.safertogether.services;


import com.projet3.safertogether.dto.AuthenticationRequest;
import com.projet3.safertogether.dto.AuthenticationResponse;
import com.projet3.safertogether.dto.UserDto;
import com.projet3.safertogether.models.Geolocalisation;
import com.projet3.safertogether.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


public interface UserService extends AbstractService<UserDto> {



  Integer save(UserDto dto);
  User validateAccount(Integer id);

  User invalidateAccount(Integer id);

  AuthenticationResponse register(UserDto user);

  AuthenticationResponse authenticate(AuthenticationRequest request);

  Integer update(UserDto userDto);

  User saveUserWithGeolocation(UserDto user);

  User updateUserWithGeolocation(Integer userId, Geolocalisation geolocalisation);

  User findUserWithRole(Integer userId);


  User uploadUserPhoto(Integer userId, MultipartFile file);

  User updateUserPhoto(UserDto userDto);

  UserDto saveUserPhoto(UserDto user, MultipartFile file) throws IOException;

  List<User> getUsersByIds(List<Integer> ids);

  Optional<User> findUserWithRoles(String email);

  void setUserDisconnected(Integer userId);

  void requestPasswordReset(String email);
  void resetPassword(String token, String newPassword);


}
