package com.example.vehicleManagementSystem.dto.forms;

import lombok.Data;

@Data
public class AddVehicleSetupForm {

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

}
