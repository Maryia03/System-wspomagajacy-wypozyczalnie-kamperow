package pl.pollub.camp.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter@Setter
public class VehicleType {
    @Id
    @GeneratedValue
    private int id;
    private String description;
    private String name;
    private Integer vehicleId;

}
