package pl.pollub.camp.Models.DTO;

import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import pl.pollub.camp.Models.*;

import java.util.List;
@Getter@Setter
public class VehicleRequest {
        private String name;
        private String Description;
        private VehicleStatus vehicleStatus;
        private String Comment;
        private String imageLink;
        private int vehicleTypeId;

}
