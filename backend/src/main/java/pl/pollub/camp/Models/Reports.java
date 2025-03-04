package pl.pollub.camp.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Getter
public class Reports {
    @Id
    @GeneratedValue
    private int id;
    @Setter
    private ReportType reportType;
    @ManyToOne
    @Setter
    private Reservations reservation;
    @Setter
    private Date reportDate;
    @Setter
    private String comment;
    @Setter
    private Condition technicalCondition;
    @Setter
    private Condition interiorCondition;
    @Setter
    private Condition visualCondition;



}
