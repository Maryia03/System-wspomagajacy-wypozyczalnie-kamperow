package pl.pollub.camp.Repositories;

import org.hibernate.query.Order;
import org.springframework.data.repository.CrudRepository;
import pl.pollub.camp.Models.Orders;

public interface OrderRepository extends CrudRepository<Orders, Integer> {

}
