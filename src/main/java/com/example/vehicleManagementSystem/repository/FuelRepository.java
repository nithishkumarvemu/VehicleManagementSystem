package com.example.vehicleManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.vehicleManagementSystem.entity.VehicleSetup;
import com.example.vehicleManagementSystem.entity.Fuel;

import java.util.List;

@Repository
public interface FuelRepository extends JpaRepository<Fuel, Long> {

    List<Fuel> findByVehicleSetupAndFuelId(VehicleSetup vehicleSetup, Long fuelId);

    List<Fuel> findByFuelId(Long fuelId);

    Fuel findByFuelIdAndVehicleSetup(Long fuelId, VehicleSetup vehicleSetup);

    @Query("SELECT p FROM Fuel p")
    List<Fuel> findAllFuelM();

}
