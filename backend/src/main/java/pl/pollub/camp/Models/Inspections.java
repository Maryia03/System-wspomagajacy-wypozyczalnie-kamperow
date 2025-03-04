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
public class Inspections {
    @Id
    @GeneratedValue
    private int Id;
    @Setter
    private String name;
    @Setter
    private InspectionType inspectionType;
    @Setter
    private Date ValidUntil;
    @Setter
    private Date startDate;
    @Setter
    @ManyToOne
    private Vehicles vehicle;
}
