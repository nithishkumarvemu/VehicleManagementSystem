package com.example.vehicleManagementSystem.dto.forms;

import lombok.Data;

@Data
public class LoginForm {

    private String username;
    private String password;
    private Boolean rememberMe = false;
}
