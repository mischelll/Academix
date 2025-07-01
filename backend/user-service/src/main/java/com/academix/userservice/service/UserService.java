package com.academix.userservice.service;

import com.academix.userservice.dao.User;
import com.academix.userservice.repository.UserRepository;
import com.academix.userservice.service.dto.UserMetaDTO;
import com.academix.userservice.service.mapper.UserMapper;
import com.academix.userservice.web.UserController;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final UserMapper userMapper;


    @Transactional
    public User updateUser(UserController.UserUpdateDTO userUpdateDTO) {
        logger.info("Updating user");
        User entity = userMapper.toEntity(userUpdateDTO);
        return userRepository.save(entity);
    }

    @Transactional
    public UserMetaDTO getUserMeta(String email) {
        logger.info("Getting user meta for email: {}", email);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();
        return  new UserMetaDTO(
                user.getId(),
                user.getFirstName(),
                user.getEmail(),
                user.getPhone(),
                user.getRoles()
                .stream()
                .map(rl -> rl.getName().toString())
                .collect(Collectors.toSet())
        );
    }

    @Transactional
    public UserMetaDTO getUserMeta(Long userId) {
        logger.info("Getting user meta for id: {}", userId);
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();
        return  new UserMetaDTO(
                user.getId(),
                user.getFirstName(),
                user.getEmail(),
                user.getPhone(),
                user.getRoles()
                        .stream()
                        .map(rl -> rl.getName().toString())
                        .collect(Collectors.toSet())
        );
    }
}
