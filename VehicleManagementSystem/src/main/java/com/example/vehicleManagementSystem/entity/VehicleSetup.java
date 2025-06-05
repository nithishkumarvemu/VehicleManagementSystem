package com.example.vehicleManagementSystem.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehicleSetup")
public class VehicleSetup {

    @Id
    private String nwVehicleNumber;

    private String vehicleIdNumber;
    private Integer modelYear;
    private String make;
    private String model;
    private String purchaseDate;
    private Integer startingMileage;
    private String vehicleWeight;
    private String vehicleType;
    private String vehicleDescription;
    private String exempt;

    @OneToMany(mappedBy = "vehicleSetup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // To prevent infinite recursion
    private List<Fuel> fuel;

}
