package pl.pollub.camp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pollub.camp.Models.VehicleType;
import pl.pollub.camp.Models.Vehicles;
import pl.pollub.camp.Repositories.VehicleTypeRepository;
import pl.pollub.camp.Repositories.VehicleRepository;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/vehicle-type")
public class VehicleTypeController {
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;
    @Autowired
    private VehicleRepository vehicleRepository;

    @GetMapping("/all")
    public ResponseEntity<List<VehicleType>> getAllVehicleTypes() {
        List<VehicleType> vehicleTypes = (List<VehicleType>) vehicleTypeRepository.findAll();
        return ResponseEntity.ok(vehicleTypes);
    }

    @PostMapping("/add")
    public ResponseEntity<VehicleType> createVehicleType(@RequestBody VehicleType vehicleType) {
        if (vehicleType.getVehicleId() != null) {
            Optional<Vehicles> vehicle = vehicleRepository.findById(vehicleType.getVehicleId());
            if (!vehicle.isPresent()) {
                return ResponseEntity.badRequest().body(null);
            }
        }
        VehicleType createdVehicleType = vehicleTypeRepository.save(vehicleType);
        return ResponseEntity.ok(createdVehicleType);
    }

    @PutMapping("/update")
    public ResponseEntity<VehicleType> updateVehicleType(
            @PathVariable int id,
            @RequestBody VehicleType vehicleTypeDetails) {
        Optional<VehicleType> existingVehicleType = vehicleTypeRepository.findById(id);
        if (existingVehicleType.isPresent()) {
            VehicleType vehicleType = existingVehicleType.get();
            vehicleType.setName(vehicleTypeDetails.getName());
            vehicleType.setDescription(vehicleTypeDetails.getDescription());
            if (vehicleTypeDetails.getVehicleId() != null) {
                Optional<Vehicles> vehicle = vehicleRepository.findById(vehicleTypeDetails.getVehicleId());
                if (vehicle.isPresent()) {
                    vehicleType.setVehicleId(vehicleTypeDetails.getVehicleId());  // Update vehicleId
                } else {
                    return ResponseEntity.badRequest().body(null);  // Vehicle not found with the provided ID
                }
            }
            VehicleType updatedVehicleType = vehicleTypeRepository.save(vehicleType);
            return ResponseEntity.ok(updatedVehicleType);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteVehicleType(@PathVariable int id) {
        Optional<VehicleType> existingVehicleType = vehicleTypeRepository.findById(id);
        if (existingVehicleType.isPresent()) {
            vehicleTypeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}


