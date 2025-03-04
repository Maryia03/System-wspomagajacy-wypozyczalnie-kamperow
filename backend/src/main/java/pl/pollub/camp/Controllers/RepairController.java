package pl.pollub.camp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.pollub.camp.Models.DTO.RepairRequest;
import pl.pollub.camp.Models.Repairs;
import pl.pollub.camp.Services.RepairService;

@Controller
@RequestMapping(path = "/repairs")
@CrossOrigin
public class RepairController {
    @Autowired
    private RepairService repairService;

    @GetMapping
    private @ResponseBody Iterable<Repairs> getAll(){
        return repairService.getAllRepairs();
    }

    @PostMapping
    private @ResponseBody Repairs createRepair(@RequestBody RepairRequest repairRequest){
        return repairService.createRepair(repairRequest);
    }
    @PutMapping(path = "/{id}")
    private @ResponseBody Repairs updateRepair(@PathVariable int id, @RequestBody RepairRequest repairRequest){
        return repairService.updateRepair(id,repairRequest);
    }
    @DeleteMapping(path = "/{id}")
    private @ResponseBody Repairs deleteRepair(@PathVariable int id){
        return repairService.deleteRepair(id);
    }

}
