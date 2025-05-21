package com.academix.userservice.service.dto;

import java.util.Collection;
import java.util.List;

public record UserMetaDTO(Long id, String name, String email, Collection<String> roles) {
}
