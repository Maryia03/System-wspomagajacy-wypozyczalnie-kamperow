package pl.pollub.camp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.pollub.camp.Models.DTO.VehicleRequest;
import pl.pollub.camp.Models.Vehicles;
import pl.pollub.camp.Repositories.VehicleRepository;
import pl.pollub.camp.Models.VehicleType;
import pl.pollub.camp.Services.VehicleService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import jakarta.persistence.EntityNotFoundException;
import java.util.*;

class VehicleServiceTest{
    @Mock
    private VehicleRepository vehicleRepository;
    @InjectMocks
    private VehicleService vehicleService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should successfully add a new vehicle when all fields are valid")
    void testAddVehicle_Success(){
        VehicleRequest vehicleRequest = new VehicleRequest();
        vehicleRequest.setName("Camper");
        VehicleType vehicleType = new VehicleType();
        vehicleType.setName("Type A");
        vehicleRequest.setVehicleType(vehicleType);

        String result = vehicleService.addVehicle(vehicleRequest);
        assertEquals("Vehicle added successfully", result);
        verify(vehicleRepository).save(any(Vehicles.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when vehicle type is missing")
    void testAddVehicle_MissingType(){
        VehicleRequest vehicleRequest = new VehicleRequest();
        assertThrows(IllegalArgumentException.class, () -> vehicleService.addVehicle(vehicleRequest));
    }

    @Test
    @DisplayName("Should successfully delete a vehicle by ID")
    void testDeleteVehicle_Success(){
        when(vehicleRepository.existsById(1)).thenReturn(true);
        String result = vehicleService.deleteVehicle(1);
        assertEquals("Vehicle deleted successfully", result);
        verify(vehicleRepository).deleteById(1);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when vehicle ID does not exist")
    void testDeleteVehicle_NotFound(){
        when(vehicleRepository.existsById(1)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> vehicleService.deleteVehicle(1));
    }

    @Test
    @DisplayName("Should return a list of all vehicles")
    void testGetAllVehicles(){
        when(vehicleRepository.findAll()).thenReturn(List.of(new Vehicles()));
        Iterable<Vehicles> result = vehicleService.getAllVehicles();
        assertTrue(result.iterator().hasNext());
    }
}
