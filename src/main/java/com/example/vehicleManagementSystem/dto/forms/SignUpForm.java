package com.example.vehicleManagementSystem.dto.forms;

import lombok.Data;

@Data
public class SignUpForm {

    private String role;
    private String userName;
    private String password;
    private String confirmPassword;

}
