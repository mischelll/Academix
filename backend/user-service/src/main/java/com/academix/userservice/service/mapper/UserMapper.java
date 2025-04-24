package com.academix.userservice.service.mapper;


import com.academix.userservice.dao.User;
import com.academix.userservice.web.UserController;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserController.UserUpdateDTO toDto(User user);
    User toEntity(UserController.UserUpdateDTO userDto);
}
