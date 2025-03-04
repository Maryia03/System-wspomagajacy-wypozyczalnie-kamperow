package pl.pollub.camp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.pollub.camp.Models.DTO.ReportRequest;
import pl.pollub.camp.Models.ReportType;
import pl.pollub.camp.Models.Reports;
import pl.pollub.camp.Services.ReportService;

@RestController
@RequestMapping(path = "/reports")
@CrossOrigin
public class ReportController {
    @Autowired
    private ReportService reportService;

    @PostMapping(path = "/pickup")
    public @ResponseBody Reports createPickupReport(@RequestBody ReportRequest reportRequest){
        return reportService.createReport(reportRequest, ReportType.PICKUP);
    }

    @PostMapping(path = "/return")
    public @ResponseBody Reports createReturnReport(@RequestBody ReportRequest reportRequest){
        return reportService.createReport(reportRequest, ReportType.RETURN);
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody Reports deleteReport(@PathVariable int id){
        return reportService.deleteReport(id);
    }

    @GetMapping
    public @ResponseBody Iterable<Reports> getAllReports(){
        return reportService.getAll();
    }

    @PutMapping(path = "/{id}")
    public @ResponseBody Reports updateReport(@RequestBody ReportRequest reportRequest, @PathVariable int id){
        return reportService.updateReport(id, reportRequest);
    }
}
