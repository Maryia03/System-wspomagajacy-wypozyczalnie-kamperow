package pl.pollub.camp.Repositories;

import org.springframework.data.repository.CrudRepository;
import pl.pollub.camp.Models.Reports;

public interface ReportRepository extends CrudRepository<Reports, Integer> {
    Iterable<Reports> findByReservationId(int id);
}
