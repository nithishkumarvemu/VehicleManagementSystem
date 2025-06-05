package com.example.vehicleManagementSystem.repository;

import com.example.vehicleManagementSystem.entity.VehicleSetup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleSetupRepository extends JpaRepository<VehicleSetup, String> {

    @Query("SELECT v FROM VehicleSetup v")
    List<VehicleSetup> findAllVehiclesAdmin();

    // findAllCategories
//    @Query("SELECT c FROM VehicleSetup c where c.active = true")
    @Query("SELECT v FROM VehicleSetup v")
    List<VehicleSetup> findAllVehicles();

    // findByCategoryName
    @Query("SELECT v FROM VehicleSetup v WHERE v.nwVehicleNumber=?1")
    Optional<VehicleSetup> findBynwVehicleNumber(String nwVehicleNumber);

}
