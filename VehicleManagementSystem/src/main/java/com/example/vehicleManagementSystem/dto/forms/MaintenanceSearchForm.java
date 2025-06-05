package com.example.vehicleManagementSystem.dto.forms;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaintenanceSearchForm {

    private String vehicleSetupId;
    private String maintenanceId;

}
