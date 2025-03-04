package pl.pollub.camp.Services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pollub.camp.Models.DTO.InspectionRequest;
import pl.pollub.camp.Models.Inspections;
import pl.pollub.camp.Models.Users;
import pl.pollub.camp.Models.Vehicles;
import pl.pollub.camp.Repositories.InspectionRepository;
import pl.pollub.camp.Repositories.UserRepository;
import pl.pollub.camp.Repositories.VehicleRepository;

@Service
@NoArgsConstructor
public class InspectionService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InspectionRepository inspectionRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    public String addInspection(HttpServletRequest request, InspectionRequest inspectionRequest) {
        Users user = userRepository.findByName((String) request.getAttribute("Username")).orElse(null);
        Vehicles vehicle = vehicleRepository.findById(inspectionRequest.getVehicleId()).orElse(null);
        if (user != null && vehicle != null) {
            Inspections inspection = new Inspections();
            inspection.setName(inspectionRequest.getName());
            inspection.setInspectionType(inspectionRequest.getInspectionType());
            inspection.setValidUntil(inspectionRequest.getValidUntil());
            inspection.setStartDate(inspectionRequest.getStartDate());
            inspection.setVehicle(vehicle);
            inspectionRepository.save(inspection);
            return "Inspection added successfully";
        }
        return "Could not find user or vehicle";
    }
    public String deleteInspection(int id) {
        if (!inspectionRepository.existsById(id)) {
            throw new EntityNotFoundException("Inspection not found with id " + id);
        }
        inspectionRepository.deleteById(id);
        return "Inspection deleted successfully";
    }

    public Inspections updateInspection(int id, InspectionRequest updatedInspectionRequest) {
        Inspections inspection = inspectionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Inspection not found with id " + id));
        inspection.setName(updatedInspectionRequest.getName());
        inspection.setInspectionType(updatedInspectionRequest.getInspectionType());
        inspection.setValidUntil(updatedInspectionRequest.getValidUntil());
        if (updatedInspectionRequest.getVehicleId() != null) {
            Vehicles vehicle = vehicleRepository.findById(updatedInspectionRequest.getVehicleId())
                    .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with id " + updatedInspectionRequest.getVehicleId()));
            inspection.setVehicle(vehicle);
        }
        return inspectionRepository.save(inspection);
    }
}
