package com.example.vehicleManagementSystem.dto.forms;

import lombok.Data;

@Data
public class AddFuelForm {

	private String nwFuelVehicleNumber;
    private String date;
    private Integer currentMileage;
    private Float fuelAdded;
    private Float fuelCost;

    private Long fuelId;

}
