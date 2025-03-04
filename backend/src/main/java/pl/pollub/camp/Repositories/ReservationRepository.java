package pl.pollub.camp.Repositories;

import org.springframework.data.repository.CrudRepository;
import pl.pollub.camp.Models.Reservations;

import java.sql.Date;


public interface ReservationRepository extends CrudRepository<Reservations, Integer> {
    Iterable<Reservations> findByStartBetweenOrEndBetween(Date start1, Date end1, Date start2, Date end2);
    Iterable<Reservations> findByOrderUserId(int userid);
    Iterable<Reservations> findByVehicleId(int vehicleId);
}
