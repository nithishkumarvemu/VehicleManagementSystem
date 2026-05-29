package com.example.vehicleManagementSystem.repository;

import com.example.vehicleManagementSystem.entity.Maintenance;
import com.example.vehicleManagementSystem.entity.VehicleSetup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
	
    List<Maintenance> findByVehicleSetupAndMaintenanceId(VehicleSetup vehicleSetup, Long maintenanceId);

    List<Maintenance> findByMaintenanceId(Long maintenanceId);

    Maintenance findByMaintenanceIdAndVehicleSetup(Long maintenanceId, VehicleSetup vehicleSetup);
    
    @Query("SELECT m FROM Maintenance m")
    List<Maintenance> findAllMaintenanceM();

}
