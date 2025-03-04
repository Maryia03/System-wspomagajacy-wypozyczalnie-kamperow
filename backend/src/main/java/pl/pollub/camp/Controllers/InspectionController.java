package pl.pollub.camp.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pollub.camp.Models.DTO.InspectionRequest;
import pl.pollub.camp.Models.Inspections;
import pl.pollub.camp.Repositories.InspectionRepository;
import pl.pollub.camp.Services.InspectionService;

@RestController
@RequestMapping("/inspection")
@RequiredArgsConstructor
@CrossOrigin
public class InspectionController {
    private final InspectionService inspectionService;
    @Autowired
    private InspectionRepository inspectionRepository;
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> addInspection(HttpServletRequest request, @RequestBody InspectionRequest inspectionRequest) {
        return ResponseEntity.ok(inspectionService.addInspection(request, inspectionRequest));
    }

    @GetMapping("/all")
    public @ResponseBody Iterable<Inspections> showAllInspections() {
        return inspectionRepository.findAll();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteInspection(@PathVariable int id) {
        String result = inspectionService.deleteInspection(id);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Inspections> updateInspection(@PathVariable int id, @RequestBody InspectionRequest inspectionRequest) {
        Inspections updatedInspection = inspectionService.updateInspection(id, inspectionRequest);
        return ResponseEntity.ok(updatedInspection);
    }


}
