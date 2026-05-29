package com.example.vehicleManagementSystem.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;
import com.example.vehicleManagementSystem.dto.auth.UserDto;
import com.example.vehicleManagementSystem.dto.forms.LoginForm;
import com.example.vehicleManagementSystem.dto.forms.SignUpForm;
import com.example.vehicleManagementSystem.dto.forms.UserProfileForm;
import com.example.vehicleManagementSystem.entity.User;

public interface AuthService {

    UserDto authenticateUser(HttpServletRequest request, HttpSession httpSession);

    UserDto authenticateAdmin(HttpServletRequest request, HttpSession httpSession);

    User getUserByRememberMeToken(String rememberMeToken);

    UserDto getUserBySessionToken(String sessionToken);

    UserDto login(LoginForm loginForm);

    User logout(String rememberMeToken, String sessionToken);

    UserDto updateUserProfile(UserProfileForm userProfileForm, MultipartFile file);

    void signUp(SignUpForm signUpForm);

    void changePassword(UserProfileForm userProfileForm);

}
