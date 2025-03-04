package pl.pollub.camp.Services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import pl.pollub.camp.Models.*;
import pl.pollub.camp.Models.DTO.DatesResponse;
import pl.pollub.camp.Models.DTO.FilterVehiclesRequset;
import pl.pollub.camp.Models.DTO.ReservationRequest;
import pl.pollub.camp.Repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@NoArgsConstructor
public class ReservationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private PriceRepository priceRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private LocationRepository locationRepository;

    public String makeReservation(HttpServletRequest request, @RequestBody ReservationRequest reservationRequest) {
        Users u = userRepository.findByEmail((String) request.getAttribute("Email")).orElse(null);
        Vehicles v = vehicleRepository.findById(reservationRequest.getVehicleId()).orElse(null);
        Optional<Location> l = locationRepository.findByCity(reservationRequest.getLocation());
        System.out.println(v);
        System.out.println(u);
        System.out.println(reservationRequest.getVehicleId());
        //sprawdzdenie czy kamper jest dostępny w podanym terminie
        Iterable<Vehicles> availableVehicles =  showAvailableCampers(new FilterVehiclesRequset(reservationRequest.getReservationStartDate(),reservationRequest.getReservationEndDate()));
        Double total = 0.0;
        if(u == null || v==null){
            return "Could not find user or vehicle";
        }
        if(u.isAcive() == false){
            return "You can't make reservations";
        }
        if(l.isEmpty()){
            return "Invalid Location";
        }
        try {
            Prices p = priceRepository.findPricesByVehicleTypeAndDateRange(v.getVehicleType().getId(), reservationRequest.getReservationStartDate(), reservationRequest.getReservationEndDate()).get(0);
            total = Math.round((float) (reservationRequest.getReservationEndDate().getTime() - reservationRequest.getReservationStartDate().getTime()) / (24 * 60 * 60 * 1000)) * p.getPrice();
        }catch (IndexOutOfBoundsException e){
            return "Price is not available";
        }
        for (var veh : availableVehicles){
            if(veh.getId() == reservationRequest.getVehicleId()){
                Orders o = new Orders();
                o.setUser(u);
                o.setComment(reservationRequest.getComments());
                o.setOrderStatus(OrderStatus.PENDING);
                o.setTotalCost(total);


                Reservations r = new Reservations();
                r.setStart(reservationRequest.getReservationStartDate());
                r.setEnd(reservationRequest.getReservationEndDate());
                r.setLocation(l.get());
                r.setOrder(o);
                r.setVehicle(v);

                orderRepository.save(o);
                reservationRepository.save(r);

                return "Success";

            }
        }

        return "Camper not available";
    }

    public Iterable<Vehicles> showAvailableCampers(@RequestBody FilterVehiclesRequset filterVehiclesRequset) {
        Iterable<Reservations> reservations = reservationRepository.findByStartBetweenOrEndBetween(filterVehiclesRequset.getBegining(), filterVehiclesRequset.getEnd(), filterVehiclesRequset.getBegining(), filterVehiclesRequset.getEnd());
        List<Integer> occupiedVehiclesIds = new ArrayList<>();
        for (Reservations r : reservations) {
            occupiedVehiclesIds.add(r.getVehicle().getId());
        }
        Iterable<Vehicles> availableVehicles = vehicleRepository.findByIdNotIn(occupiedVehiclesIds);
        if(occupiedVehiclesIds.isEmpty()){
            availableVehicles = vehicleRepository.findAll();
        }
        return availableVehicles;
    }

    public Iterable<Reservations> showUserReservations(@RequestParam int userid) {
        return reservationRepository.findByOrderUserId(userid);
    }

    public String removeReservation(HttpServletRequest httpServletRequest, int id) {
        Reservations r = reservationRepository.findById(id).orElse(null);
        Iterable<Reports> reports = reportRepository.findByReservationId(id);
        if (r == null || reports == null
//                r.getOrder().getUser().getEmail() == httpServletRequest.getAttribute("email") ||
//                httpServletRequest.getAttribute("Role") == Role.ADMIN
                )
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
            reportRepository.deleteAll(reports);
            reservationRepository.delete(r);
            orderRepository.delete(r.getOrder());
            return "Success";
        }

    }

    public String acceptReservation(int id) {
        Optional<Reservations> r = reservationRepository.findById(id);
        if(r.isPresent()){
            Reservations rezerwacja = r.get();
            Orders order = rezerwacja.getOrder();
            order.setOrderStatus(OrderStatus.PAID);

            orderRepository.save(order);
            return "Sucess";
        }else{
            throw new EntityNotFoundException();
        }

    }

    public String cancelReservation(int id) {
        Optional<Reservations> reservationsOptional = reservationRepository.findById(id);
        if(reservationsOptional.isPresent()){
            Reservations r = reservationsOptional.get();
            r.getOrder().setOrderStatus(OrderStatus.CANCELED);

            orderRepository.save(r.getOrder());
            return "Suceess";
        }else{
            throw new EntityNotFoundException();
        }

    }

    public Iterable<DatesResponse> getReservationsByVehicleId(int id) {
        List<DatesResponse> res = new ArrayList<>();
        reservationRepository.findByVehicleId(id).forEach((reservations -> {
            res.add(new DatesResponse(reservations));
        }));

        return res;
    }

    public String resignReservation(int id) {
        Optional<Reservations> reservationsOptional = reservationRepository.findById(id);
        if(reservationsOptional.isPresent()){
            Reservations r = reservationsOptional.get();
            if(r.getOrder().getOrderStatus() == OrderStatus.PENDING){
            r.getOrder().setOrderStatus(OrderStatus.RESIGN_REQUEST);

            orderRepository.save(r.getOrder());
            return "Suceess";}
            else {
                return "You can't resign now";
            }
        }else{
            throw new EntityNotFoundException();
        }
    }
}
