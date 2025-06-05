package com.example.vehicleManagementSystem.controller;

import com.example.vehicleManagementSystem.service.FuelService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.vehicleManagementSystem.dto.auth.UserDto;
import com.example.vehicleManagementSystem.dto.forms.*;
import com.example.vehicleManagementSystem.service.AuthService;

@Controller
public class AuthController {

    @Autowired
    AuthService authService;
    @Autowired
    FuelService fuelService;

    @GetMapping("/")
    public String index(HttpServletRequest request, HttpSession httpSession,
                        HttpServletResponse response, Model model) {
        UserDto userDto = authService.authenticateUser(request, httpSession);
        model.addAttribute("orderPlaced", false);
        if (userDto != null && userDto.getRole().equalsIgnoreCase("ADMIN"))
            return "redirect:/admin/dashboard";
        if (userDto != null) {
            model.addAttribute("user", userDto);
            model.addAttribute("vehicles", fuelService.getAllVehicles());
            model.addAttribute("session-token", userDto.getSessionToken());
            httpSession.setAttribute("session-token", userDto.getSessionToken());
            return "Dashboard";
        }
        return "Login";
    }

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, HttpSession session, Model model) {
        System.out.println(model.getAttribute("session-token") != null ? model
                .getAttribute("session-token").toString() : null);
        UserDto userDto = authService.authenticateUser(request, session);
        if (userDto != null && userDto.getRole().equalsIgnoreCase("ADMIN"))
            return "redirect:/admin/dashboard";
        if (userDto != null) {
            return "redirect:/";
        }
        return "Login";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(HttpServletRequest request, HttpSession session, Model model) {
        UserDto userDto = authService.authenticateUser(request, session);
        if (userDto != null && userDto.getRole().equalsIgnoreCase("ADMIN"))
            return "redirect:/admin/dashboard";
        if (userDto != null) {
            return "redirect:/";
        }
        model.addAttribute("success", "");
        model.addAttribute("message", "");
        return "Forgot_password";
    }

    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute(name = "UserProfileForm") UserProfileForm userProfileForm,
                                 HttpServletRequest request,
                                 HttpSession httpSession, HttpServletResponse response, Model model) {
        try {
            authService.changePassword(userProfileForm);
            model.addAttribute("success", true);
            model.addAttribute("message", "User password updated successfully, Please proceed to login");
            return "Forgot_password";

        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("message", e.getMessage());
            return "Forgot_password";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpSession httpSession, HttpServletResponse response) {
        Cookie rememberMeCookie = new Cookie("remember-me-cookie", "");
        rememberMeCookie.setMaxAge(0); // 1 month in seconds
        response.addCookie(rememberMeCookie);
        httpSession.removeAttribute("session-token");
        return "Login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute(name = "LoginForm") LoginForm loginForm, HttpSession httpSession,
                        HttpServletResponse response, Model model) {
        try {
            UserDto userDto = authService.login(loginForm);

            if (loginForm.getRememberMe()) {
                Cookie rememberMeCookie = new Cookie("remember-me-cookie", userDto.getRememberMeToken());
                rememberMeCookie.setMaxAge(2628288); // 1 month in seconds
                response.addCookie(rememberMeCookie);
            } else {
                Cookie rememberMeCookie = new Cookie("remember-me-cookie", "");
                rememberMeCookie.setMaxAge(0); // 1 month in seconds
                response.addCookie(rememberMeCookie);
            }
            httpSession.setAttribute("session-token", userDto.getSessionToken());
            if (userDto != null && userDto.getRole().equalsIgnoreCase("ADMIN"))
                return "redirect:/admin/dashboard";
            return "redirect:/";

        } catch (Exception e) {
            if (e.getMessage().equals("Invalid Credentials")) {
                model.addAttribute("invalidCredentials", true);
                return "Login";
            }
        }
        return "Login";
    }

    @GetMapping("/fuel")
    public String getFuelPage(HttpServletRequest request, HttpSession httpSession, Model model) {
//        UserDto userDto = authService.authenticateUser(request, httpSession);
//        if (userDto != null && userDto.getRole().equalsIgnoreCase("CUSTOMER"))
//            return "redirect:/";
        model.addAttribute("fuelM", fuelService.searchFuelAdmin(FuelSearchForm.builder().vehicleSetupId(null).build()));
        model.addAttribute("vehicles", fuelService.getAllVehiclesAdmin());
        model.addAttribute("success", "null");
        model.addAttribute("message", "");

        return "Fuel";
    }

    @PostMapping("/add-fuel")
    public String addFuel(@ModelAttribute(name = "AddFuelForm") AddFuelForm addFuelForm,
                          @RequestParam(value="file", required = false) MultipartFile file,
                          HttpServletRequest request,
                          HttpSession httpSession, HttpServletResponse response, Model model) {
//        UserDto userDto = authService.authenticateUser(request, httpSession);
//        if (userDto != null && userDto.getRole().equalsIgnoreCase("CUSTOMER"))
//            return "redirect:/";

        try {
            fuelService.addFuel(addFuelForm, file);
            model.addAttribute("fuelM", fuelService.searchFuelAdmin(FuelSearchForm.builder()
                    .vehicleSetupId(null).build()));
            model.addAttribute("vehicles", fuelService.getAllVehiclesAdmin());
            model.addAttribute("success", true);
            model.addAttribute("message", "Fuel added/updated successfully");
            return "Fuel";

        } catch (Exception e) {
            model.addAttribute("fuelM", fuelService.searchFuelAdmin(FuelSearchForm.builder()
                    .vehicleSetupId(null).build()));
            model.addAttribute("vehicles", fuelService.getAllVehiclesAdmin());
            model.addAttribute("success", false);
            model.addAttribute("message", e.getMessage());
            return "Fuel";
        }
    }

    @GetMapping("/maintenance")
    public String getMaintenancePage(HttpServletRequest request, HttpSession httpSession, Model model) {
        model.addAttribute("maintenanceM", fuelService.searchMaintenanceAdmin(MaintenanceSearchForm.builder()
                .vehicleSetupId(null).build()));
        model.addAttribute("vehicles", fuelService.getAllVehiclesAdmin());
        model.addAttribute("success", "null");
        model.addAttribute("message", "");

        return "Maintenance";
    }

    @PostMapping("/add-maintenance")
    public String addMaintenance(@ModelAttribute(name = "AddMaintenanceForm") AddMaintenanceForm addMaintenanceForm,
                                 @RequestParam(value="file", required = false) MultipartFile file,
                                 HttpServletRequest request,
                                 HttpSession httpSession, HttpServletResponse response, Model model) {
        try {
            fuelService.addMaintenance(addMaintenanceForm, file);
            model.addAttribute("maintenanceM", fuelService.searchMaintenanceAdmin(MaintenanceSearchForm.builder()
                    .vehicleSetupId(null).build()));
            model.addAttribute("vehicles", fuelService.getAllVehiclesAdmin());
            model.addAttribute("success", true);
            model.addAttribute("message", "Maintenance added/updated successfully");
            return "Maintenance";

        } catch (Exception e) {
            model.addAttribute("maintenanceM", fuelService.searchMaintenanceAdmin(MaintenanceSearchForm.builder()
                    .vehicleSetupId(null).build()));
            model.addAttribute("vehicles", fuelService.getAllVehiclesAdmin());
            model.addAttribute("success", false);
            model.addAttribute("message", e.getMessage());
            return "Maintenance";
        }
    }

    @GetMapping("/user-profile")
    public String userProfile(HttpServletRequest request, HttpSession httpSession, Model model) {
        UserDto userDto = authService.authenticateUser(request, httpSession);
        if (userDto != null && userDto.getRole().equalsIgnoreCase("ADMIN"))
            return "redirect:/admin/dashboard";
        model.addAttribute("user", userDto);
        model.addAttribute("vehicles", fuelService.getAllVehicles());
        return "User-profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute(name = "UserProfileForm") UserProfileForm userProfileForm,
                                @RequestParam("file") MultipartFile file,
                                HttpServletRequest request,
                                HttpSession httpSession, HttpServletResponse response, Model model) {
        UserDto userDto = authService.authenticateUser(request, httpSession);
        if (userDto != null && userDto.getRole().equalsIgnoreCase("ADMIN"))
            return "redirect:/admin/dashboard";
        try {
            UserDto user = authService.updateUserProfile(userProfileForm, file);
            model.addAttribute("user", user);
            model.addAttribute("success", true);
            model.addAttribute("message", "User profile updated successfully");
            model.addAttribute("vehicles", fuelService.getAllVehicles());
            return "User-profile";
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
            model.addAttribute("user", userDto);
            model.addAttribute("vehicles", fuelService.getAllVehicles());
            return "User-profile";
        }
    }
}