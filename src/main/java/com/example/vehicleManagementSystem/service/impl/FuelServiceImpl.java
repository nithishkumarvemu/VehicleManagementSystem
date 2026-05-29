package com.example.vehicleManagementSystem.service.impl;

import com.example.vehicleManagementSystem.service.FuelService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;
import com.example.vehicleManagementSystem.dto.forms.*;
import com.example.vehicleManagementSystem.entity.*;
import com.example.vehicleManagementSystem.repository.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FuelServiceImpl implements FuelService {

    @Autowired
    private final UserRepository userRepository;

    private final VehicleSetupRepository vehicleSetupRepository;
    private final FuelRepository fuelRepository;
    private final MaintenanceRepository maintenanceRepository;

    @Override
    public List<VehicleSetup> getAllVehiclesAdmin() {
        return vehicleSetupRepository.findAllVehiclesAdmin();
    }

    @Override
    public List<VehicleSetup> getAllVehicles() {
        return vehicleSetupRepository.findAllVehicles();
    }

    @Override
    @SneakyThrows
    @Transactional
    public VehicleSetup addVehicleSetup(AddVehicleSetupForm addVehicleSetupForm,
                                        MultipartFile file) throws IOException {
        VehicleSetup vehicleSetup;
        if (StringUtils.isEmpty(addVehicleSetupForm.getNwVehicleNumber()))
            vehicleSetup = new VehicleSetup();
        else
            vehicleSetup = vehicleSetupRepository.findBynwVehicleNumber(addVehicleSetupForm
                    .getNwVehicleNumber()).orElseGet(() -> { return new VehicleSetup();
            });

        vehicleSetup.setNwVehicleNumber(addVehicleSetupForm.getNwVehicleNumber());
        vehicleSetup.setVehicleIdNumber(addVehicleSetupForm.getVehicleIdNumber());
        vehicleSetup.setModelYear(addVehicleSetupForm.getModelYear());
        vehicleSetup.setMake(addVehicleSetupForm.getMake());
        vehicleSetup.setModel(addVehicleSetupForm.getModel());
        vehicleSetup.setPurchaseDate(addVehicleSetupForm.getPurchaseDate());
        vehicleSetup.setStartingMileage(addVehicleSetupForm.getStartingMileage());
        vehicleSetup.setVehicleWeight(addVehicleSetupForm.getVehicleWeight());
        vehicleSetup.setVehicleType(addVehicleSetupForm.getVehicleType());
        vehicleSetup.setVehicleDescription(addVehicleSetupForm.getVehicleDescription());
        vehicleSetup.setExempt(addVehicleSetupForm.getExempt());

        return vehicleSetupRepository.save(vehicleSetup);
    }

    @Override
    @SneakyThrows
    @Transactional
    public Fuel addFuel(AddFuelForm addFuelForm, MultipartFile file) throws IOException {
        VehicleSetup vehicleSetup = vehicleSetupRepository.findById(addFuelForm.getNwFuelVehicleNumber()).get();
        Fuel fuel = null;

        if (addFuelForm.getFuelId() == null) {
            fuel = new Fuel();

            fuel.setNwFuelVehicleNumber(addFuelForm.getNwFuelVehicleNumber());
            fuel.setDate(addFuelForm.getDate());
            fuel.setCurrentMileage(addFuelForm.getCurrentMileage());
            fuel.setFuelAdded(addFuelForm.getFuelAdded());
            fuel.setFuelCost(addFuelForm.getFuelCost());
        }
        if (addFuelForm.getFuelId() != null) {
            fuel = fuelRepository.findById(addFuelForm.getFuelId())
                    .orElseThrow(() -> new RuntimeException("Fuel record not found"));
            fuel.setNwFuelVehicleNumber(addFuelForm.getNwFuelVehicleNumber());
            fuel.setDate(addFuelForm.getDate());
            fuel.setCurrentMileage(addFuelForm.getCurrentMileage());
            fuel.setFuelAdded(addFuelForm.getFuelAdded());
            fuel.setFuelCost(addFuelForm.getFuelCost());
            fuel.setVehicleSetup(vehicleSetup);
        }

        return fuelRepository.save(fuel);
    }

    @Override
    @SneakyThrows
    @Transactional
    public Maintenance addMaintenance(AddMaintenanceForm addMaintenanceForm,
                                      MultipartFile file) throws IOException {
        VehicleSetup vehicleSetup = vehicleSetupRepository.findById(addMaintenanceForm
                .getNwMaintenanceVehicleNumber()).get();
        Maintenance maintenance = null;

        if (addMaintenanceForm.getMaintenanceId() == null) {
            maintenance = new Maintenance();

            maintenance.setNwMaintenanceVehicleNumber(addMaintenanceForm.getNwMaintenanceVehicleNumber());
            maintenance.setDate(addMaintenanceForm.getDate());
            maintenance.setCurrentMileage(addMaintenanceForm.getCurrentMileage());
            maintenance.setMaintenanceCost(addMaintenanceForm.getMaintenanceCost());
            maintenance.setMaintenanceDescription(addMaintenanceForm.getMaintenanceDescription());
        }
        if (addMaintenanceForm.getMaintenanceId() != null) {
            maintenance = maintenanceRepository.findById(addMaintenanceForm.getMaintenanceId())
                    .orElseThrow(() -> new RuntimeException("Maintenance record not found"));
            maintenance.setNwMaintenanceVehicleNumber(addMaintenanceForm.getNwMaintenanceVehicleNumber());
            maintenance.setDate(addMaintenanceForm.getDate());
            maintenance.setCurrentMileage(addMaintenanceForm.getCurrentMileage());
            maintenance.setMaintenanceCost(addMaintenanceForm.getMaintenanceCost());
            maintenance.setMaintenanceDescription(addMaintenanceForm.getMaintenanceDescription());
            maintenance.setVehicleSetup(vehicleSetup);
        }

        return maintenanceRepository.save(maintenance);
    }

    @Override
    public void vehicleSetupStatusUpdate(String nwVehicleNumber) {
        VehicleSetup vehicleSetup = vehicleSetupRepository.findById(nwVehicleNumber).get();
        vehicleSetupRepository.save(vehicleSetup);
    }

    @Override
    public void fuelStatusUpdate(String fuelId) {
        Fuel fuel = fuelRepository.findById(Long.valueOf(fuelId)).get();
        fuelRepository.save(fuel);
    }

    @Override
    public void maintenanceStatusUpdate(Long maintenanceId){
        Maintenance maintenance = maintenanceRepository.findById(maintenanceId).get();
        maintenanceRepository.save(maintenance);
    }

    @Override
    @SneakyThrows
    public List<Fuel> searchFuel(FuelSearchForm fuelSearchForm) {
        if (fuelSearchForm.getVehicleSetupId() != null) {
            if (StringUtils.isEmpty(fuelSearchForm.getFuelId())) {
                return fuelRepository.findByFuelId(Long.valueOf(fuelSearchForm.getVehicleSetupId()));
            }
            return fuelRepository.findByVehicleSetupAndFuelId(vehicleSetupRepository
                            .findById(fuelSearchForm.getVehicleSetupId())
                            .orElseThrow(() -> new Exception("Data not found")),
                            Long.valueOf(fuelSearchForm.getFuelId())).stream()
                    .collect(Collectors.toList());
        }
        if (StringUtils.isEmpty(fuelSearchForm.getFuelId())) {
            return fuelRepository.findAllFuelM().stream().collect(Collectors.toList());
        }
        return fuelRepository.findByFuelId(Long.valueOf(fuelSearchForm.getFuelId())).stream().collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    public List<Fuel> searchFuelAdmin(FuelSearchForm fuelSearchForm) {
        if (fuelSearchForm.getVehicleSetupId() != null) {
            if (StringUtils.isEmpty(fuelSearchForm.getFuelId())) {
                return fuelRepository.findByFuelId(Long.valueOf(fuelSearchForm.getVehicleSetupId()));
            }
            return fuelRepository.findByVehicleSetupAndFuelId(vehicleSetupRepository
                    .findById(fuelSearchForm.getVehicleSetupId())
                    .orElseThrow(() -> new Exception("Data not found")), Long.valueOf(fuelSearchForm.getFuelId()));
        }
        if (StringUtils.isEmpty(fuelSearchForm.getFuelId())) {
            return fuelRepository.findAllFuelM();
        }
        return fuelRepository.findByFuelId(Long.valueOf(fuelSearchForm.getFuelId()));
    }

    @Override
    @SneakyThrows
    public List<Maintenance> searchMaintenanceAdmin(MaintenanceSearchForm maintenanceSearchForm) {
        if (maintenanceSearchForm.getVehicleSetupId() != null) {
            if (StringUtils.isEmpty(maintenanceSearchForm.getMaintenanceId())) {
                return maintenanceRepository.findByMaintenanceId(Long.valueOf(maintenanceSearchForm.getVehicleSetupId()));
            }
            return maintenanceRepository.findByVehicleSetupAndMaintenanceId(vehicleSetupRepository
                    .findById(maintenanceSearchForm.getVehicleSetupId())
                    .orElseThrow(() -> new Exception("Data not found")), Long.valueOf(maintenanceSearchForm
                    .getMaintenanceId()));
        }
        if (StringUtils.isEmpty(maintenanceSearchForm.getMaintenanceId())) {
            return maintenanceRepository.findAllMaintenanceM();
        }
        return maintenanceRepository.findByMaintenanceId(Long.valueOf(maintenanceSearchForm.getMaintenanceId()));
    }


    @Override
    @SneakyThrows
    public List<Fuel> getFuelByVehicleSetup(String vehicleId) {
        VehicleSetup vehicleSetup = vehicleSetupRepository.findBynwVehicleNumber(vehicleId)
                .orElseThrow(() -> new Exception("Data not found"));
        return fuelRepository.findByFuelId(Long.valueOf(vehicleSetup.getNwVehicleNumber()));
    }

    @Override
    @Transactional
    public void deleteVehicleSetup(String nwVehicleNumber) {
        VehicleSetup vehicleSetup = vehicleSetupRepository.findById(nwVehicleNumber)
                .orElseThrow(() -> new RuntimeException("Vehicle Number not found with: " + nwVehicleNumber));
        vehicleSetupRepository.delete(vehicleSetup);
    }

    @Override
    public void deleteUser(Long id){
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
        }
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

}
