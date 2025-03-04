package pl.pollub.camp.Models.DTO;

import lombok.Getter;

import java.sql.Date;

@Getter
public class GetPriceRequest {
    int vehicleTypeId;
    Date start;
    Date end;
}
