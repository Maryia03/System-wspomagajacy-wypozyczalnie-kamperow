package pl.pollub.camp;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.pollub.camp.Models.DTO.VehicleRequest;
import pl.pollub.camp.Models.VehicleType;
import pl.pollub.camp.Models.Vehicles;
import pl.pollub.camp.Repositories.VehicleRepository;
import pl.pollub.camp.Repositories.VehicleTypeRepository;
import pl.pollub.camp.Services.VehicleService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class VehicleServiceIntegrationTest{
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;
    @Autowired
    private VehicleService vehicleService;

    @BeforeEach
    void setUp(){
    }

    @Test
    @DisplayName("Should successfully add a new vehicle")
    void testAddVehicle_Success(){
        VehicleType vehicleType = new VehicleType();
        vehicleType.setName("Type A");
        vehicleType.setDescription("A comfortable camper.");
        vehicleType = vehicleTypeRepository.save(vehicleType);


        VehicleRequest vehicleRequest = new VehicleRequest();
        vehicleRequest.setName("Camper");
        vehicleRequest.setDescription("A comfortable camper.");
        vehicleRequest.setComment("Family camper");
        vehicleRequest.setVehicleTypeId(vehicleType.getId());

        String result = vehicleService.addVehicle(null, vehicleRequest);

        assertEquals("Vehicle added successfully", result);
        assertTrue(vehicleRepository.findAll().iterator().hasNext());
    }

    @Test
    @DisplayName("Should delete a vehicle by ID")
    void testDeleteVehicle_Success(){
        Vehicles vehicle = new Vehicles();
        vehicle.setName("Camper");
        vehicle = vehicleRepository.save(vehicle);

        String result = vehicleService.deleteVehicle(vehicle.getId());

        assertEquals("Vehicle deleted successfully", result);
        assertFalse(vehicleRepository.existsById(vehicle.getId()));
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existing vehicle")
    void testDeleteVehicle_NotFound(){
        Exception exception = assertThrows(EntityNotFoundException.class, () -> vehicleService.deleteVehicle(999));
        assertEquals("Vehicle not found with id 999", exception.getMessage());
    }

    @Test
    @DisplayName("Should retrieve all vehicles")
    void testGetAllVehicles(){
        Vehicles vehicle1 = new Vehicles();
        vehicle1.setName("Camper1");
        Vehicles vehicle2 = new Vehicles();
        vehicle2.setName("Camper2");

        vehicleRepository.save(vehicle1);
        vehicleRepository.save(vehicle2);

        Iterable<Vehicles> vehicles = vehicleService.getAllVehicles();
        assertNotNull(vehicles);
        assertTrue(vehicles.iterator().hasNext());
    }
}

