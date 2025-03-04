package pl.pollub.camp.Services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pollub.camp.Models.DTO.LocationRequest;
import pl.pollub.camp.Models.Location;
import pl.pollub.camp.Repositories.LocationRepository;

import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    LocationRepository locationRepository;

    public Location createLocation(LocationRequest locationRequset){
        return  locationRepository.save(locationRequset.toEntity());
    }

    public Location deleteLocation(int id){
        Optional<Location> l = locationRepository.findById(id);
        if(l.isPresent()){
            locationRepository.delete(l.get());
            return l.get();
        }
        throw new EntityNotFoundException("Could not find location with id: "+id);
    }

    public Iterable<Location> getAllLocations(){
        return locationRepository.findAll();
    }

    public Location updateLocation(int id, LocationRequest locationRequest){
        Optional<Location> l = locationRepository.findById(id);
        if(l.isPresent()){
            Location updateLocation = l.get();
            updateLocation.setAddress(locationRequest.getAddress());
            updateLocation.setCity(locationRequest.getCity());
            return locationRepository.save(updateLocation);
        }
        throw new EntityNotFoundException("Could not find location with id:"+id);
    }
}
