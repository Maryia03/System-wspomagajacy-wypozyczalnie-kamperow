package pl.pollub.camp.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pollub.camp.Models.DTO.DatesResponse;
import pl.pollub.camp.Models.DTO.FilterVehiclesRequset;
import pl.pollub.camp.Models.DTO.ReservationRequest;
import pl.pollub.camp.Models.Orders;
import pl.pollub.camp.Models.Reservations;
import pl.pollub.camp.Models.Vehicles;
import pl.pollub.camp.Repositories.ReservationRepository;
import pl.pollub.camp.Services.ReservationService;

import java.sql.Date;

@RestController
@RequestMapping(path = "/reservation")
@RequiredArgsConstructor
@CrossOrigin
public class ReservationController {
    private final ReservationService reservationService;
    @Autowired
    private ReservationRepository reservationRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> makeReservation(HttpServletRequest httpServletRequest, @RequestBody ReservationRequest reservationRequest){
        return ResponseEntity.ok(reservationService.makeReservation(httpServletRequest,reservationRequest));
    }
    @GetMapping(path = "/all")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    public @ResponseBody Iterable<Reservations> showAllReservations(){
        return reservationRepository.findAll();
    }

    @GetMapping(path = "/find")
    public @ResponseBody Iterable<Vehicles> showAvailableVehicles(@RequestParam Date begining, @RequestParam Date end){
        FilterVehiclesRequset f = new FilterVehiclesRequset(begining,end  );
        return reservationService.showAvailableCampers(f);
    }
    @GetMapping(path = "/{id}")
    public @ResponseBody Iterable<Reservations> showUserReservations(@PathVariable int id){
        return reservationService.showUserReservations(id);
    }

    @GetMapping(path = "/vehicle/{id}")
    public @ResponseBody Iterable<DatesResponse> showVehicleReservations(@PathVariable int id){
        return  reservationService.getReservationsByVehicleId(id);
    }
    @DeleteMapping(path = "/{id}")
    public @ResponseBody String deleteReservation(HttpServletRequest httpServletRequest, @PathVariable int id){
        return reservationService.removeReservation(httpServletRequest,id);
    }

    @GetMapping(path = "/accept/{id}")
    public @ResponseBody String acceptReservation(@PathVariable int id){
        return reservationService.acceptReservation(id);
    }
    @GetMapping(path = "/cancel/{id}")
    public @ResponseBody String cancelReservation(@PathVariable int id){
        return reservationService.cancelReservation(id);
    }

    @GetMapping(path = "/resign/{id}")
    public @ResponseBody String resignReservation(@PathVariable int id){
        return reservationService.resignReservation(id);
    }

}

