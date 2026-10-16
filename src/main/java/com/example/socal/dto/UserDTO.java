package com.example.socal.dto;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class UserDTO {
    private String role;
    private String name;
    private String username;
}
