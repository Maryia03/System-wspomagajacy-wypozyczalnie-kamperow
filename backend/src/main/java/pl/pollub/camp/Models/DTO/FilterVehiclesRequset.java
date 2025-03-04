package pl.pollub.camp.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.pollub.camp.Models.VehicleType;

import java.sql.Date;
@Getter
@AllArgsConstructor
public class FilterVehiclesRequset {
    Date begining;
    Date end;
}
