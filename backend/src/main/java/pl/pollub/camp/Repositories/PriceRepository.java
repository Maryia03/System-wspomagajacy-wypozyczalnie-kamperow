package pl.pollub.camp.Repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.pollub.camp.Models.Prices;
import pl.pollub.camp.Models.VehicleType;

import java.sql.Date;
import java.util.List;

public interface PriceRepository extends CrudRepository<Prices, Integer> {


    List<Prices> findByVehicleTypeAndStartBetweenAndEndBetweenOrderByPriceDesc(VehicleType vehicleType, Date start1, Date end1, Date start2, Date end2);


    @Query(value = "SELECT * FROM prices p WHERE p.vehicle_type_id = :vehicleType " +
            "AND :date1 BETWEEN p.start AND p.end " +
            "AND :date2 BETWEEN p.start AND p.end " +
            "ORDER BY p.price DESC", nativeQuery = true )
    List<Prices> findPricesByVehicleTypeAndDateRange(
            @Param("vehicleType") int vehicleType,
            @Param("date1") Date date1,
            @Param("date2") Date date2
    );
}
