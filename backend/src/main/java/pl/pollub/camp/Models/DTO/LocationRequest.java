package pl.pollub.camp.Models.DTO;

import lombok.Getter;
import pl.pollub.camp.Models.Location;

@Getter
public class LocationRequest {
    String city;
    String address;


    public Location toEntity(){
        Location l = new Location();
        l.setCity(this.getCity());
        l.setAddress(this.getAddress());

        return l;
    }
}

