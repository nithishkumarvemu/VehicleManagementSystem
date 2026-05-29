package com.example.vehicleManagementSystem.dto.forms;

import lombok.Data;

@Data
public class AddMaintenanceForm {

        private String nwMaintenanceVehicleNumber;
        private String date;
        private Integer currentMileage;
        private Float maintenanceCost;
        private String maintenanceDescription;

        private Long maintenanceId;
}
