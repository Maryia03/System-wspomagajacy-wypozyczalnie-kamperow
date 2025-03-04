package pl.pollub.camp.Repositories;

import org.springframework.data.repository.CrudRepository;
import pl.pollub.camp.Models.Repairs;

public interface RepairRepository extends CrudRepository<Repairs,Integer> {
}
