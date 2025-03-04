package pl.pollub.camp.Repositories;

import org.springframework.data.repository.CrudRepository;

import pl.pollub.camp.Models.Users;

import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<Users, Integer> {
    Optional<Users> findByName(String name);
    Optional<Users> findByEmail(String email);

}