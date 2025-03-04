package pl.pollub.camp.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Location {
    @Id
    @GeneratedValue
    private  int id;
    @Setter
    private String city;
    @Setter
    private String address;
}
