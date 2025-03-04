package pl.pollub.camp.Models.DTO;

import lombok.Getter;
import lombok.Setter;
import pl.pollub.camp.Models.Condition;
import pl.pollub.camp.Models.Reports;
import pl.pollub.camp.Models.Reservations;

import java.sql.Date;

@Getter
public class ReportRequest {
    private int reservationid;
    private Date reportDate;
    private String comment;
    private Condition technicalCondition;
    private Condition interiorCondition;
    private Condition visualCondition;
}
