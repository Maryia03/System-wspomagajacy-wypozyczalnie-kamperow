package pl.pollub.camp.Models.DTO;

import lombok.Getter;
import pl.pollub.camp.Models.Reservations;

import java.sql.Date;

@Getter
public class DatesResponse {
    private Date start;
    private Date end;


    public DatesResponse(Reservations reservation) {
        this.start = reservation.getStart();
        this.end = reservation.getEnd();
    }
}
