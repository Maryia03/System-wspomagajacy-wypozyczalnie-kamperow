package pl.pollub.camp.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Getter
public class Prices {
    @Id
    @GeneratedValue
    private int id;
    @Setter
    private Double price;
    @Setter
    private Date start;
    @Setter
    private Date end;
    @Setter
    @ManyToOne
    private VehicleType vehicleType;
}
