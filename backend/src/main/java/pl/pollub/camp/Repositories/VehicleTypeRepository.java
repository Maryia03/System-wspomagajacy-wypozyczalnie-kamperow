package pl.pollub.camp.Repositories;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.repository.CrudRepository;
import pl.pollub.camp.Models.Vehicles;
import pl.pollub.camp.Models.VehicleType;
import java.util.Collection;
import java.util.Optional;

public interface VehicleTypeRepository extends CrudRepository<VehicleType, Integer> {
}
