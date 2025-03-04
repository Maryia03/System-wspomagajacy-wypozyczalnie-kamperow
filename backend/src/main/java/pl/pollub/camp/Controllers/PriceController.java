package pl.pollub.camp.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.pollub.camp.Models.DTO.CreatePriceRequest;
import pl.pollub.camp.Models.DTO.GetPriceRequest;
import pl.pollub.camp.Models.Prices;
import pl.pollub.camp.Services.PriceService;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/prices")
@CrossOrigin
public class PriceController {
    @Autowired
    private PriceService pricesService;

    @PostMapping(path = "/add")
    public Prices addPrice(@RequestBody CreatePriceRequest createPriceRequest) {
        return pricesService.addPrice(createPriceRequest.getPrice(), createPriceRequest.getStart(), createPriceRequest.getEnd(), createPriceRequest.getVehicleTypeId());
    }

    @GetMapping(path = "/all")
    public List<Prices> getAllPrices() {
        return pricesService.getAllPrices();
    }

    @PatchMapping(path = "/update")
    public Prices updatePrice(@RequestParam int id, @RequestParam Double price, @RequestParam Date start, @RequestParam Date end) {
        return pricesService.updatePrice(id, price, start, end);
    }

    @DeleteMapping(path = "/delete")
    public String deletePrice(@RequestParam int id) {
        return pricesService.deletePrice(id);
    }

    @GetMapping(path = "/find")
    public Prices findPrice(@RequestParam int vehicleTypeId, @RequestParam Date start, @RequestParam Date end){
        return pricesService.getPrice(vehicleTypeId, start, end);
    }
}

