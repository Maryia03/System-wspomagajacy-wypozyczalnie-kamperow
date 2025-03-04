package pl.pollub.camp.Repositories;

import org.springframework.data.repository.CrudRepository;
import pl.pollub.camp.Models.Inspections;

public interface InspectionRepository extends CrudRepository<Inspections, Integer> {
}
