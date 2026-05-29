package com.example.vehicleManagementSystem.service;

import org.springframework.web.multipart.MultipartFile;
import com.example.vehicleManagementSystem.dto.forms.*;
import com.example.vehicleManagementSystem.entity.*;
import java.io.IOException;
import java.util.List;

public interface FuelService {

    List<VehicleSetup> getAllVehiclesAdmin();

    List<VehicleSetup> getAllVehicles();

    List<Fuel> searchFuel(FuelSearchForm fuelSearchForm);

    List<Fuel> searchFuelAdmin(FuelSearchForm fuelSearchForm);

    List<Maintenance> searchMaintenanceAdmin(MaintenanceSearchForm maintenanceSearchForm);

    VehicleSetup addVehicleSetup(AddVehicleSetupForm addVehicleSetupForm, MultipartFile file) throws IOException;

    Fuel addFuel(AddFuelForm addFuelForm, MultipartFile file) throws IOException;

    Maintenance addMaintenance(AddMaintenanceForm addMaintenanceForm, MultipartFile file) throws IOException;

    void vehicleSetupStatusUpdate(String nwVehicleNumber);

    void fuelStatusUpdate(String fuelId);

    void maintenanceStatusUpdate(Long maintenanceId);

    List<Fuel> getFuelByVehicleSetup(String vehicleId);

    void deleteVehicleSetup(String nwVehicleNumber);

    void deleteUser(Long id);

}
