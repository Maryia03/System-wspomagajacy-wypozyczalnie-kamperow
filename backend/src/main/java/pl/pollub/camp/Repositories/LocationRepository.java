package pl.pollub.camp.Repositories;

import org.springframework.data.repository.CrudRepository;
import pl.pollub.camp.Models.Location;

import java.util.Optional;

public interface LocationRepository extends CrudRepository<Location, Integer> {
    Optional<Location> findByCity(String City);
}
