package com.onlinepharmacy.identity.service;

import com.onlinepharmacy.identity.dto.response.UserResponseDto;
import com.onlinepharmacy.identity.model.User;
import com.onlinepharmacy.identity.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto getUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return new UserResponseDto(user.getId(), user.getEmail(), user.getName());
    }
}
