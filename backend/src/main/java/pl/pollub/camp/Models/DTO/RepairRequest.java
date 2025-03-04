package pl.pollub.camp.Models.DTO;

import lombok.Getter;

import java.sql.Date;

@Getter
public class RepairRequest {
    private Date startDate;
    private Date endDate;
    private String name;
    private int vehicleId;
}
