package pl.pollub.camp.Models.DTO;

import lombok.Getter;

import java.sql.Date;

@Getter
public class CreatePriceRequest {
    Double price;
    Date start;
    Date end;
    Integer vehicleTypeId;
}
