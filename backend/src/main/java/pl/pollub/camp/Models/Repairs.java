package pl.pollub.camp.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Getter
public class Repairs {
    @Id
    @GeneratedValue
    private int id;
    @Setter
    private Date startDate;
    @Setter
    private Date endDate;
    @Setter
    private String name;
    @ManyToOne
    @Setter
    private Vehicles vehicle;
    @OneToOne
    @Setter
    private Reservations reservation;
}
