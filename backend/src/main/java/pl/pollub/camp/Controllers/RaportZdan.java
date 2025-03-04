package pl.pollub.camp.Controllers;
import java.time.Instant;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.pollub.camp.Models.OrderStatus;
import pl.pollub.camp.Models.Orders;
import pl.pollub.camp.Repositories.OrderRepository;

@RestController
@RequestMapping("/orders")
public class RaportZdan {
    @Autowired
    private OrderRepository orderRepository;
    @PutMapping("/{id}/finished")
    public String markOrderAsFinished(@PathVariable int id, @RequestParam(required = false) String comment) {
        return orderRepository.findById(id).map(order -> {
            order.setOrderStatus(OrderStatus.FINISHED);
            order.setComment(comment != null ? comment : "Marked as FINISHED");
            orderRepository.save(order);
            return "Order with ID " + id + " has been marked as FINISHED.";
        }).orElse("Order with ID " + id + " not found.");
    }
}
