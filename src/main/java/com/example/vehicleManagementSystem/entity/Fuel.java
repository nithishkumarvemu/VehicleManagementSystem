package com.example.vehicleManagementSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "fuel")
public class Fuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fuelId;

    private String date;
    private Integer currentMileage;
    private Float fuelAdded;
    private Float fuelCost;

    private String nwFuelVehicleNumber;

    @ManyToOne
    @JoinColumn(name = "northwest_vehicle_no")
    @JsonIgnore // To prevent infinite recursion
    private VehicleSetup vehicleSetup;

}
