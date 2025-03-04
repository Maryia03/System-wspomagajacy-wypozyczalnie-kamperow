package pl.pollub.camp;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.pollub.camp.Models.*;
import pl.pollub.camp.Models.DTO.FilterVehiclesRequset;
import pl.pollub.camp.Models.DTO.ReservationRequest;
import pl.pollub.camp.Repositories.*;
import pl.pollub.camp.Services.ReservationService;
import java.sql.Date;
import java.util.*;

class ReservationServiceTest{
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private VehicleRepository vehicleRepository;
    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should successfully create a reservation when user and vehicle exist")
    void testMakeReservation_Success(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("Username")).thenReturn("testuser");

        Users mockUser = new Users();
        Vehicles mockVehicle = new Vehicles();

        when(userRepository.findByName("testuser")).thenReturn(Optional.of(mockUser));
        when(vehicleRepository.findById(1)).thenReturn(Optional.of(mockVehicle));

        ReservationRequest reservationRequest = new ReservationRequest();
        reservationRequest.setVehicleId(1);
        reservationRequest.setReservationStartDate(new Date(System.currentTimeMillis()));
        reservationRequest.setReservationEndDate(new Date(System.currentTimeMillis()));
        reservationRequest.setComments("Test Comment");

        String result = reservationService.makeReservation(request, reservationRequest);
        assertEquals("Success", result);
        verify(orderRepository).save(any(Orders.class));
        verify(reservationRepository).save(any(Reservations.class));
    }

    @Test
    @DisplayName("Should return error message when user or vehicle is not found")
    void testMakeReservation_UserOrVehicleNotFound(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("Username")).thenReturn("testuser");
        when(userRepository.findByName("testuser")).thenReturn(Optional.empty());

        ReservationRequest reservationRequest = new ReservationRequest();
        reservationRequest.setVehicleId(1);

        String result = reservationService.makeReservation(request, reservationRequest);
        assertEquals("Could not find user or vehicle", result);
    }

    @Test
    @DisplayName("Should return available vehicles filtered by reservation dates")
    void testShowAvailableCampers(){
        FilterVehiclesRequset filterRequest = new FilterVehiclesRequset();
        filterRequest.setBegining(new Date(System.currentTimeMillis()));
        filterRequest.setEnd(new Date(System.currentTimeMillis()));

        Reservations mockReservation = new Reservations();
        Vehicles occupiedVehicle = new Vehicles();
        occupiedVehicle.setId(1);
        mockReservation.setVehicle(occupiedVehicle);
        when(reservationRepository.findByStartBetweenAndEndBetween(any(), any(), any(), any()))
                .thenReturn(List.of(mockReservation));

        Vehicles availableVehicle = new Vehicles();
        availableVehicle.setId(2);
        when(vehicleRepository.findByIdNotIn(any())).thenReturn(List.of(availableVehicle));
        Iterable<Vehicles> result = reservationService.showAvailableCampers(filterRequest);
        assertTrue(result.iterator().hasNext());
    }
}