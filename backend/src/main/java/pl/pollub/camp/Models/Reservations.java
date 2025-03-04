package pl.pollub.camp.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;


@Entity
@Getter
public class Reservations {
    @Id
    @GeneratedValue
    private int Id;
    @Setter
    private Date start;
    @Setter
    private Date end;
    @Setter
    @ManyToOne
    private Location location;
    @Setter
    @OneToOne
    private Orders order;
    @Setter
    @ManyToOne
    private Vehicles vehicle;

}
