package pl.pollub.camp.Controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.pollub.camp.Models.Vehicles;
import pl.pollub.camp.Services.VehicleService;

import pl.pollub.camp.Models.VehicleStatus;
import pl.pollub.camp.Services.VehicleService;
import pl.pollub.camp.Repositories.VehicleRepository;
import pl.pollub.camp.Models.DTO.VehicleRequest;

import java.util.Optional;

@Controller
@RequestMapping(path = "/vehicle")
@CrossOrigin
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @PostMapping(path = "/add")
    public @ResponseBody String addVehicle(HttpServletRequest request,@RequestBody VehicleRequest vehicleRequest) {
        try {
            return vehicleService.addVehicle(request,vehicleRequest);

        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }
    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Vehicles> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }
    @GetMapping(path = "/id/{id}")
    public @ResponseBody Optional<Vehicles> getVehicleById(@PathVariable int id){
        return vehicleService.getById(id);
    }

    @DeleteMapping(path = "/delete/{id}")
    public @ResponseBody String deleteVehicle(@PathVariable int id) {
        try {
            return vehicleService.deleteVehicle(id);
        } catch (EntityNotFoundException e) {
            return e.getMessage();
        }
    }

    @PatchMapping(path = "/update/{id}")
    public @ResponseBody String updateVehicle(@PathVariable int id, @RequestBody VehicleRequest updatedVehicleRequest) {
        try {
            Vehicles updatedVehicle = vehicleService.updateVehicle(id, updatedVehicleRequest);
            return "Vehicle updated: " + updatedVehicle.getName();
        } catch (EntityNotFoundException e) {
            return e.getMessage();
        }
    }
}
