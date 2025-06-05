package com.example.vehicleManagementSystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "maintenance")
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maintenanceId;

    private String date;
    private Integer currentMileage;
    private Float maintenanceCost;
    private String maintenanceDescription;

    private String nwMaintenanceVehicleNumber;

    @ManyToOne
    @JoinColumn(name = "northwest_vehicle_no")
    private VehicleSetup vehicleSetup;

}
