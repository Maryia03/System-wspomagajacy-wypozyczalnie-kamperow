package pl.pollub.camp.Models.DTO;

import lombok.Getter;

import java.sql.Date;
@Getter
public class ReservationRequest {
    int vehicleId;
    Date reservationStartDate;
    Date reservationEndDate;
    String location;
    String comments;
}
