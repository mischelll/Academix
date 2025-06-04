package com.academix.curriculumservice.service.apiclient.user;

import java.util.List;

public record UserMetaDTO(Long id, String name, String email, List<String> roles) {
}
