package pl.pollub.camp.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
public class Services {
    @Id
    @GeneratedValue
    private int Id;
    @Setter
    private String Name;
    @Setter
    private String Description;
    @Setter
    private Date date;
    @Setter
    @ManyToOne
    private Vehicles vehicle;
}
