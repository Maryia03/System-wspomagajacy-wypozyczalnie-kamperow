package pl.pollub.camp.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.pollub.camp.Models.DTO.LocationRequest;
import pl.pollub.camp.Models.Location;
import pl.pollub.camp.Services.LocationService;

@RestController
@RequestMapping(path = "/location")
@RequiredArgsConstructor
@CrossOrigin
public class LocationController {
    @Autowired
    LocationService locationService;

    @PostMapping
    public @ResponseBody Location createLocation(@RequestBody LocationRequest request){
        return locationService.createLocation(request);
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody Location deleteLocation(@PathVariable int id){
        return locationService.deleteLocation(id);
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Location> getAllLocations(){
        return locationService.getAllLocations();
    }

    @PatchMapping(path = "/{id}")
    public @ResponseBody Location updateLocation(@PathVariable int id, @RequestBody LocationRequest locationRequest){
        return locationService.updateLocation(id,locationRequest);
    }
}
