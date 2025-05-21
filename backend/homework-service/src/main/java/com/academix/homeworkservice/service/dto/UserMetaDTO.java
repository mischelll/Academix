package com.academix.homeworkservice.service.dto;

import java.util.List;

public record UserMetaDTO(Long id, String name, String email, List<String> roles) {
}
