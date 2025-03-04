package pl.pollub.camp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.pollub.camp.Models.OrderStatus;
import pl.pollub.camp.Models.Orders;
import pl.pollub.camp.Models.Users;
import pl.pollub.camp.Models.Vehicles;
import pl.pollub.camp.Repositories.OrderRepository;
import pl.pollub.camp.Repositories.UserRepository;
import pl.pollub.camp.Repositories.VehicleRepository;

import java.time.LocalDateTime;

@Controller
@RequestMapping(path = "/order")
public class OrderController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    VehicleRepository vehicleRepository;

    @PostMapping(path = "/addOrder")
    public @ResponseBody String addOrder(
            @RequestParam String comment,
            @RequestParam int userId,
            @RequestParam OrderStatus orderStatus,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam int vehicleId) {

        Users u = userRepository.findById(userId).orElse(null);
        if (u == null) {
            return "User not found";
        }
        Vehicles vehicle = vehicleRepository.findById(vehicleId).orElse(null);
        if (vehicle == null) {
            return "Vehicle not found";
        }

        Orders o = new Orders();
        o.setComment(comment);
        o.setOrderStatus(orderStatus);
        o.setUser(u);
        o.setStartTime(startTime);
        o.setVehicle(vehicle);
        orderRepository.save(o);
        return "Order successfully created";
    }
    @GetMapping(path = "/allOrders")
    public @ResponseBody Iterable<Orders> getAllOrders(){
        return orderRepository.findAll();
    }

}
