package com.onlinepharmacy.identity.mapper;

import com.onlinepharmacy.identity.dto.response.AuthResponseDto;
import com.onlinepharmacy.identity.dto.request.SignupRequestDto;
import com.onlinepharmacy.identity.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

public final class IdentityMapper {

  private IdentityMapper() {}

  public static User toEntity(SignupRequestDto dto, PasswordEncoder encoder) {
    User user = new User();
    user.setEmail(dto.getEmail() != null ? dto.getEmail().toLowerCase() : null);
    user.setName(dto.getName());
    user.setMobile(dto.getMobile());
    if (dto.getPassword() != null) {
      user.setPasswordHash(encoder.encode(dto.getPassword()));
    }
    user.setDateOfBirth(dto.getDateOfBirth());
    user.setGender(dto.getGender());
    
    user.setRoles("CUSTOMER");
    user.setEnabled(true);
    return user;
  }

  public static AuthResponseDto toAuthResponseDto(User user, String token) {
    List<String> roles = user.rolesAsList();
    return new AuthResponseDto(token, roles);
  }
}
