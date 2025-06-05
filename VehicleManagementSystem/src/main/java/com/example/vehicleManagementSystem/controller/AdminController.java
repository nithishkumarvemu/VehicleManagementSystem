package com.example.vehicleManagementSystem.controller;

import com.example.vehicleManagementSystem.entity.User;
import com.example.vehicleManagementSystem.repository.UserRepository;
import com.example.vehicleManagementSystem.service.FuelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.vehicleManagementSystem.dto.auth.UserDto;
import com.example.vehicleManagementSystem.dto.forms.*;
import com.example.vehicleManagementSystem.service.AuthService;

import java.util.List;

@Controller
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    AuthService authService;
    @Autowired
    FuelService fuelService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/signUp")
    public String signUpPage(HttpServletRequest request, HttpSession httpSession, HttpServletResponse response, Model model) {
        UserDto userDto = authService.authenticateAdmin(request, httpSession);
        if (userDto != null && userDto.getRole().equalsIgnoreCase("CUSTOMER"))
            return "redirect:/";
        
        return "Signup";
    }

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute(name = "SignUpForm") SignUpForm signUpForm,
						    		HttpServletRequest request,
						            HttpSession httpSession, HttpServletResponse response, Model model) {
    	 UserDto userDto = authService.authenticateAdmin(request, httpSession);
         if (userDto != null && userDto.getRole().equalsIgnoreCase("CUSTOMER"))
             return "redirect:/";
         
        try {
            authService.signUp(signUpForm);
            return "Login";

        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
            return "Signup";
        }
    }
    
    @GetMapping("/admin/dashboard")
    public String getDashboard(HttpServletRequest request, HttpSession httpSession, HttpServletResponse response, Model model) {
        UserDto userDto = authService.authenticateAdmin(request, httpSession);
        if (userDto != null && userDto.getRole().equalsIgnoreCase("CUSTOMER"))
            return "redirect:/";
        if (userDto != null) {
            model.addAttribute("maintenanceM", fuelService.searchMaintenanceAdmin(MaintenanceSearchForm.builder()
                    .vehicleSetupId(null).build()));
            model.addAttribute("fuelM", fuelService.searchFuelAdmin(FuelSearchForm.builder().vehicleSetupId(null).build()));
            model.addAttribute("user", userDto);
            model.addAttribute("vehicles", fuelService.getAllVehicles());
            model.addAttribute("session-token", userDto.getSessionToken());
            httpSession.setAttribute("session-token", userDto.getSessionToken());

            List<User> allUsers = userRepository.findAll();
            model.addAttribute("allUsers", allUsers);

            return "Admin-dashboard";
        }
        return "Login";
    }

    @PostMapping("/admin/fuel-search")
    public String searchFuel(@ModelAttribute(name = "FuelSearchForm") FuelSearchForm fuelSearchForm,
                                HttpSession httpSession,
                                HttpServletRequest request,
                                HttpServletResponse response, Model model) {
        UserDto userDto = authService.authenticateUser(request, httpSession);
        if (userDto != null && userDto.getRole().equalsIgnoreCase("CUSTOMER"))
            return "redirect:/";
        model.addAttribute("fuelM", fuelService.searchFuelAdmin(fuelSearchForm));
        model.addAttribute("vehicles", fuelService.getAllVehicles());
        model.addAttribute("success", "null");
        model.addAttribute("message", "");
        return "Admin-Fuel";
    }

    @GetMapping("/admin/vehicleSetupForm")
    public String getVehicleSetupPage(HttpServletRequest request, HttpSession httpSession, Model model) {
        UserDto userDto = authService.authenticateUser(request, httpSession);
        if (userDto != null && userDto.getRole().equalsIgnoreCase("CUSTOMER"))
            return "redirect:/";
        model.addAttribute("vehicles", fuelService.getAllVehiclesAdmin());
        model.addAttribute("success", "null");
        model.addAttribute("message", "");

        return "Admin-VehicleSetup";
    }

    @GetMapping("/admin/fuel")
    public String getFuelPage(HttpServletRequest request, HttpSession httpSession, Model model) {
//        UserDto userDto = authService.authenticateUser(request, httpSession);
//        if (userDto != null && userDto.getRole().equalsIgnoreCase("CUSTOMER"))
//            return "redirect:/";
        model.addAttribute("fuelM", fuelService.searchFuelAdmin(FuelSearchForm.builder().vehicleSetupId(null).build()));
        model.addAttribute("vehicles", fuelService.getAllVehiclesAdmin());
        model.addAttribute("success", "null");
        model.addAttribute("message", "");

        return "Admin-Fuel";
    }


    // New Code here
    @GetMapping("/admin/maintenance")
    public String getMaintenancePage(HttpServletRequest request, HttpSession httpSession, Model model) {
        UserDto userDto = authService.authenticateUser(request, httpSession);
        if (userDto != null && userDto.getRole().equalsIgnoreCase("CUSTOMER"))
            return "redirect:/";
        model.addAttribute("maintenanceM", fuelService.searchMaintenanceAdmin(MaintenanceSearchForm.builder()
                .vehicleSetupId(null).build()));
        model.addAttribute("vehicles", fuelService.getAllVehiclesAdmin());
        model.addAttribute("success", "null");
        model.addAttribute("message", "");

        return "Admin-Maintenance";
    }
    
    @PostMapping("/admin/add-vehicleSetup")
    public String addVehicleSetupForm(@ModelAttribute(name = "AddVehicleSetupForm") AddVehicleSetupForm addVehicleSetupForm,
                              @RequestParam(value = "file", required = false) MultipartFile file,
                              HttpServletRequest request,
                              HttpSession httpSession, HttpServletResponse response, Model model) {

        UserDto userDto = authService.authenticateUser(request, httpSession);
        if (userDto != null && userDto.getRole().equalsIgnoreCase("CUSTOMER"))
            return "redirect:/";

        try {
            fuelService.addVehicleSetup(addVehicleSetupForm, file);
            model.addAttribute("vehicles", fuelService.getAllVehiclesAdmin());
            model.addAttribute("success", true);
            model.addAttribute("message", "Vehicle added/updated successfully");
            return "Admin-VehicleSetup";

        } catch (Exception e) {
            model.addAttribute("vehicles", fuelService.getAllVehiclesAdmin());
            model.addAttribute("success", false);
            model.addAttribute("message", e.getMessage());
            return "Admin-VehicleSetup";
        }
    }

    @PostMapping("/admin/add-fuel")
    public String addFuel(@ModelAttribute(name = "AddFuelForm") AddFuelForm addFuelForm,
                             @RequestParam(value="file", required = false) MultipartFile file,
                             HttpServletRequest request,
                             HttpSession httpSession, HttpServletResponse response, Model model) {
        UserDto userDto = authService.authenticateUser(request, httpSession);
        if (userDto != null && userDto.getRole().equalsIgnoreCase("CUSTOMER"))
            return "redirect:/";

        try {
            fuelService.addFuel(addFuelForm, file);
            model.addAttribute("fuelM", fuelService.searchFuelAdmin(FuelSearchForm.builder()
                    .vehicleSetupId(null).build()));
            model.addAttribute("vehicles", fuelService.getAllVehiclesAdmin());
            model.addAttribute("success", true);
            model.addAttribute("message", "Fuel added/updated successfully");
            return "Admin-Fuel";

        } catch (Exception e) {
            model.addAttribute("fuelM", fuelService.searchFuelAdmin(FuelSearchForm.builder()
                    .vehicleSetupId(null).build()));
            model.addAttribute("vehicles", fuelService.getAllVehiclesAdmin());
            model.addAttribute("success", false);
            model.addAttribute("message", e.getMessage());
            return "Admin-Fuel";
        }
    }

    @PostMapping("/admin/add-maintenance")
    public String addMaintenance(@ModelAttribute(name = "AddMaintenanceForm") AddMaintenanceForm addMaintenanceForm,
                          @RequestParam(value="file", required = false) MultipartFile file,
                          HttpServletRequest request,
                          HttpSession httpSession, HttpServletResponse response, Model model) {
        UserDto userDto = authService.authenticateUser(request, httpSession);
        if (userDto != null && userDto.getRole().equalsIgnoreCase("CUSTOMER"))
            return "redirect:/";

        try {
            fuelService.addMaintenance(addMaintenanceForm, file);
            model.addAttribute("maintenanceM", fuelService.searchMaintenanceAdmin(MaintenanceSearchForm.builder()
                    .vehicleSetupId(null).build()));
            model.addAttribute("vehicles", fuelService.getAllVehiclesAdmin());
//          model.addAttribute("vehicles", Optional.ofNullable(fuelService.getAllVehiclesAdmin()).orElse(Collections.emptyList()));
            model.addAttribute("success", true);
            model.addAttribute("message", "Maintenance added/updated successfully");
            return "Admin-Maintenance";

        } catch (Exception e) {
            model.addAttribute("maintenanceM", fuelService.searchMaintenanceAdmin(MaintenanceSearchForm.builder()
                    .vehicleSetupId(null).build()));
            model.addAttribute("vehicles", fuelService.getAllVehiclesAdmin());
            model.addAttribute("success", false);
            model.addAttribute("message", e.getMessage());
            return "Admin-Maintenance";
        }
    }

    @PostMapping("/admin/delete-vehicleSetup")
    public String deleteVehicleSetup(@RequestParam("nwVehicleNumber") String nwVehicleNumber,
                                     HttpServletRequest request, HttpSession httpSession, Model model) {
        UserDto userDto = authService.authenticateAdmin(request, httpSession);
        if (userDto != null && userDto.getRole().equalsIgnoreCase("CUSTOMER")) {
            return "redirect:/";
        }

        try {
            fuelService.deleteVehicleSetup(nwVehicleNumber);  // Call service to delete
            model.addAttribute("vehicles", fuelService.getAllVehiclesAdmin());
            model.addAttribute("success", true);
            model.addAttribute("message", "Vehicle setup deleted successfully");
            return "Admin-VehicleSetup";  // Redirect to vehicle setup page
        } catch (Exception e) {
            model.addAttribute("vehicles", fuelService.getAllVehiclesAdmin());
            model.addAttribute("success", false);
            model.addAttribute("message", "Error deleting vehicle setup: " + e.getMessage());
            return "Admin-VehicleSetup";
        }
    }

    @PostMapping("/admin/delete-user")
    public String deleteUser(@RequestParam("userId") Long userId,
                             HttpServletRequest request, HttpSession httpSession, Model model) {
        UserDto userDto = authService.authenticateAdmin(request, httpSession);
        if (userDto != null && userDto.getRole().equalsIgnoreCase("CUSTOMER")) {
            return "redirect:/";
        }

        try {
            fuelService.deleteUser(userId);  // Call the service to delete the user
            model.addAttribute("allUsers", userRepository.findAll());  // Refresh the list of users
            model.addAttribute("success", true);
            model.addAttribute("message", "User deleted successfully");
            return "Admin-Dashboard";  // Redirect to the Admin Dashboard
        } catch (Exception e) {
            model.addAttribute("allUsers", userRepository.findAll());
            model.addAttribute("success", false);
            model.addAttribute("message", "Error deleting user: " + e.getMessage());
            return "Admin-Dashboard";  // Redirect back to the Admin Dashboard with error
        }
    }

}