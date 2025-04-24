package com.academix.userservice.service;

import com.academix.userservice.dao.User;
import com.academix.userservice.repository.UserRepository;
import com.academix.userservice.service.mapper.UserMapper;
import com.academix.userservice.web.UserController;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
