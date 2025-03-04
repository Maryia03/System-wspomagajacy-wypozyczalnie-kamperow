package pl.pollub.camp.Services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pollub.camp.Models.DTO.RepairRequest;
import pl.pollub.camp.Models.Repairs;
import pl.pollub.camp.Models.Reservations;
import pl.pollub.camp.Models.Vehicles;
import pl.pollub.camp.Repositories.RepairRepository;
import pl.pollub.camp.Repositories.ReservationRepository;
import pl.pollub.camp.Repositories.VehicleRepository;

import java.util.Optional;

@Service
public class RepairService {
    @Autowired
    private RepairRepository repairRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    public Repairs createRepair(RepairRequest repairRequest){
        Optional<Vehicles> optionalVehicle = vehicleRepository.findById(repairRequest.getVehicleId());
        if(optionalVehicle.isEmpty()){
            throw new EntityNotFoundException("vehicle with id: "+repairRequest.getVehicleId()+" not found");
        }

        if(repairRequest.getStartDate().before(repairRequest.getEndDate())){
            Reservations reservation = new Reservations();
            reservation.setStart(repairRequest.getStartDate());
            reservation.setEnd(repairRequest.getEndDate());
            reservation.setVehicle(optionalVehicle.get());

            Repairs repair = new Repairs();
            repair.setStartDate(repairRequest.getStartDate());
            repair.setEndDate(repairRequest.getEndDate());
            repair.setVehicle(optionalVehicle.get());
            repair.setReservation(reservation);
            repair.setName(repairRequest.getName());

            reservationRepository.save(reservation);
            repairRepository.save(repair);

            return repair;
        }else {
            throw new IllegalArgumentException("start date occurs after end date");
        }
    }

    public Repairs updateRepair(int id, RepairRequest repairRequest){
        Optional<Repairs> optionalRepair = repairRepository.findById(id);
        if(optionalRepair.isEmpty()){
            throw new EntityNotFoundException("repair with id: "+id+"not found");
        }
        Optional<Vehicles> optionalVehicle = vehicleRepository.findById(repairRequest.getVehicleId());
        if(optionalVehicle.isEmpty()){
            throw new EntityNotFoundException("vehicle with id: "+repairRequest.getVehicleId()+"not found");
        }

        if(repairRequest.getStartDate().before(repairRequest.getEndDate())){
            Reservations reservation = optionalRepair.get().getReservation();
            reservation.setStart(repairRequest.getStartDate());
            reservation.setEnd(repairRequest.getEndDate());
            reservation.setVehicle(optionalVehicle.get());

            Repairs repair = optionalRepair.get();
            repair.setStartDate(repairRequest.getStartDate());
            repair.setEndDate(repairRequest.getEndDate());
            repair.setVehicle(optionalVehicle.get());
            repair.setReservation(reservation);
            repair.setName(repairRequest.getName());

            reservationRepository.save(reservation);
            repairRepository.save(repair);

            return repair;
        }else {
            throw new IllegalArgumentException("start date occurs after end date");
        }
    }

    public Iterable<Repairs> getAllRepairs(){
        return repairRepository.findAll();
    }

    public Repairs deleteRepair(int id) {
        Optional<Repairs> r = repairRepository.findById(id);
        if(r.isPresent()){
            repairRepository.delete(r.get());
            return r.get();
        }else{
            throw new EntityNotFoundException("Could not find reapir with id: "+id);
        }
    }
}
