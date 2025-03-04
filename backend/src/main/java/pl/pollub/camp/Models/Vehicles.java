package pl.pollub.camp.Models;

import jakarta.persistence.*;
import lombok.Getter;

import lombok.Setter;

import java.util.List;

@Entity
@Getter@Setter
public class Vehicles {
    @Id
    @GeneratedValue
    private int Id;
    private String name;
    private String Description;
    private VehicleStatus vehicleStatus;
    private String Comment;
    private String imageLink;
    @ManyToOne
    private VehicleType vehicleType;
}
