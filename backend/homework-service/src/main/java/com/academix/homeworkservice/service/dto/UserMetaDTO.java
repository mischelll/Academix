package com.academix.homeworkservice.service.dto;

import java.util.List;

public record UserMetaDTO(Long id, String name, String email, String phone, List<String> roles) {
}
