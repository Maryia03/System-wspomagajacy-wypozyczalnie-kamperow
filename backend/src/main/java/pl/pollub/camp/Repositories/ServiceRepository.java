package pl.pollub.camp.Repositories;

import org.springframework.data.repository.CrudRepository;
import pl.pollub.camp.Models.Services;

public interface ServiceRepository extends CrudRepository<Services, Integer> {
}
